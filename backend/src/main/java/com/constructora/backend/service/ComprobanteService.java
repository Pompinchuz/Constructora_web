package com.constructora.backend.service;

import com.constructora.backend.controller.dto.comprobante.ComprobanteResponseDTO;
import com.constructora.backend.controller.dto.comprobante.SubirComprobanteDTO;
import com.constructora.backend.entity.*;
import com.constructora.backend.entity.enums.EstadoComprobante;
import com.constructora.backend.entity.enums.EstadoProforma;
import com.constructora.backend.exception.BadRequestException;
import com.constructora.backend.exception.ForbiddenException;
import com.constructora.backend.exception.NotFoundException;
import com.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComprobanteService {

    private final ComprobantePagoRepository comprobanteRepository;
    private final ProformaRepository proformaRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;

    // ===================== SUBIR COMPROBANTE =====================
    @Transactional
    public ComprobanteResponseDTO subirComprobante(Long clienteId, SubirComprobanteDTO dto) {

        Proforma proforma = proformaRepository.findById(dto.getProformaId())
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (!proforma.getCliente().getId().equals(clienteId)) {
            throw new ForbiddenException("No tienes permiso para esta proforma");
        }

        if (proforma.getEstado() != EstadoProforma.ACEPTADA) {
            throw new BadRequestException("Solo se pueden subir comprobantes a proformas aceptadas");
        }

        String rutaArchivo = fileStorageService.guardarArchivo(dto.getArchivo(), "comprobantes");

        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setProforma(proforma);
        comprobante.setCliente(cliente);
        comprobante.setMonto(dto.getMonto());
        comprobante.setNumeroOperacion(dto.getNumeroOperacion());
        comprobante.setEntidadBancaria(dto.getEntidadBancaria());
        comprobante.setArchivoComprobante(rutaArchivo);
        comprobante.setEstado(EstadoComprobante.PENDIENTE);

        comprobante = comprobanteRepository.save(comprobante);

        emailService.notificarNuevoComprobante(comprobante);

        return mapearAResponse(comprobante);
    }

    // ===================== OBTENER COMPROBANTES =====================
    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> obtenerComprobantesPorCliente(Long clienteId) {
        return comprobanteRepository.findByClienteIdOrderByFechaSubidaDesc(clienteId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> obtenerComprobantesPorProforma(Long proformaId) {
        return comprobanteRepository.findByProformaIdOrderByFechaSubidaDesc(proformaId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComprobanteResponseDTO obtenerComprobantePorId(Long id) {
        ComprobantePago comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));
        return mapearAResponse(comprobante);
    }

    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> obtenerTodosComprobantes(EstadoComprobante estado) {
        List<ComprobantePago> comprobantes;
        if (estado != null) {
            comprobantes = comprobanteRepository.findByEstadoOrderByFechaSubidaDesc(estado);
        } else {
            comprobantes = comprobanteRepository.findAll();
        }
        return comprobantes.stream().map(this::mapearAResponse).collect(Collectors.toList());
    }

    // ===================== VERIFICAR Y RECHAZAR =====================
    @Transactional
    public ComprobanteResponseDTO verificarComprobante(Long id, Long adminId, String observaciones) {
        ComprobantePago comprobante = comprobarPendiente(id);

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));

        comprobante.setEstado(EstadoComprobante.VERIFICADO);
        comprobante.setVerificadoPor(admin);
        comprobante.setFechaVerificacion(LocalDateTime.now());
        comprobante.setObservaciones(observaciones);

        comprobanteRepository.save(comprobante);

        Proforma proforma = comprobante.getProforma();
        if (proforma.getEstado() != EstadoProforma.PAGADA) {
            proforma.setEstado(EstadoProforma.PAGADA);
            proformaRepository.save(proforma);
        }

        emailService.notificarComprobanteVerificadoEmail(comprobante);

        return mapearAResponse(comprobante);
    }

    @Transactional
    public ComprobanteResponseDTO rechazarComprobante(Long id, Long adminId, String observaciones) {
        ComprobantePago comprobante = comprobarPendiente(id);

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));

        comprobante.setEstado(EstadoComprobante.RECHAZADO);
        comprobante.setVerificadoPor(admin);
        comprobante.setFechaVerificacion(LocalDateTime.now());
        comprobante.setObservaciones(observaciones);

        comprobanteRepository.save(comprobante);

        emailService.notificarComprobanteRechazado(comprobante);

        return mapearAResponse(comprobante);
    }

    @Transactional
    public ComprobanteResponseDTO actualizarEstado(Long id, EstadoComprobante estado, Long adminId, String observaciones) {
        ComprobantePago comprobante = comprobarPendiente(id);

        if (adminId != null) {
            Administrador admin = administradorRepository.findById(adminId)
                    .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
            comprobante.setVerificadoPor(admin);
            comprobante.setFechaVerificacion(LocalDateTime.now());
        }

        comprobante.setEstado(estado);
        if (observaciones != null) {
            comprobante.setObservaciones(observaciones);
        }

        comprobanteRepository.save(comprobante);

        return mapearAResponse(comprobante);
    }

    // ===================== CONTADORES =====================
    public long contarComprobantesPendientes() {
        return comprobanteRepository.countByEstado(EstadoComprobante.PENDIENTE);
    }

    // ===================== MÉTODOS PRIVADOS =====================
    private ComprobanteResponseDTO mapearAResponse(ComprobantePago comprobante) {
        return ComprobanteResponseDTO.builder()
                .id(comprobante.getId())
                .proformaId(comprobante.getProforma().getId())
                .codigoProforma(comprobante.getProforma().getCodigo())
            
                .monto(comprobante.getMonto())
                .numeroOperacion(comprobante.getNumeroOperacion())
                .entidadBancaria(comprobante.getEntidadBancaria())
                .archivoComprobante(comprobante.getArchivoComprobante())
                .estado(comprobante.getEstado())
                .observaciones(comprobante.getObservaciones())
                .fechaSubida(comprobante.getFechaSubida())
                .verificadoPor(comprobante.getVerificadoPor() != null ?
                        comprobante.getVerificadoPor().getNombreCompleto() : null)
                .fechaVerificacion(comprobante.getFechaVerificacion())
                .build();
    }

    private ComprobantePago comprobarPendiente(Long id) {
        ComprobantePago comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));
        if (comprobante.getEstado() != EstadoComprobante.PENDIENTE) {
            throw new BadRequestException("Solo se pueden actualizar comprobantes pendientes");
        }
        return comprobante;
    }
}
