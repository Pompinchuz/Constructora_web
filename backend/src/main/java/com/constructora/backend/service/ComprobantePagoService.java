package com.constructora.backend.service;

import com.constructora.backend.controller.dto.ComprobantePagoRequestDTO;
import com.constructora.backend.controller.dto.ComprobantePagoResponseDTO;
import com.constructora.backend.entity.*;
import com.constructora.backend.entity.enums.EstadoComprobante;
import com.constructora.backend.entity.enums.EstadoProforma;
import com.constructora.backend.exception.NotFoundException;
import com.constructora.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComprobantePagoService {

    private final ComprobantePagoRepository comprobanteRepository;
    private final ProformaRepository proformaRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;

    /**
     * Subir un nuevo comprobante de pago
     */
    @Transactional
    public ComprobantePagoResponseDTO subirComprobante(Long clienteId, ComprobantePagoRequestDTO dto) {

        // Validar que la proforma existe
        Proforma proforma = proformaRepository.findById(dto.getProformaId())
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));

        // Validar que el cliente es propietario de la proforma
        if (!proforma.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("No tienes permisos para subir comprobante a esta proforma");
        }

        // Validar que la proforma está en estado ACEPTADA
        if (proforma.getEstado() != EstadoProforma.ACEPTADA) {
            throw new RuntimeException("Solo se pueden subir comprobantes a proformas aceptadas");
        }

        // Buscar cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        // Guardar archivo del comprobante
        String rutaArchivo = fileStorageService.guardarArchivo(dto.getArchivo(), "comprobantes");

        // Crear comprobante
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setProforma(proforma);
        comprobante.setCliente(cliente);
        comprobante.setMonto(dto.getMonto());
        comprobante.setNumeroOperacion(dto.getNumeroOperacion());
        comprobante.setEntidadBancaria(dto.getEntidadBancaria());
        comprobante.setArchivoComprobante(rutaArchivo);
        comprobante.setObservaciones(dto.getObservaciones());
        comprobante.setEstado(EstadoComprobante.PENDIENTE);

        comprobante = comprobanteRepository.save(comprobante);

        log.info("Comprobante {} creado para proforma {} por cliente {}",
                comprobante.getId(), proforma.getCodigo(), clienteId);

        // Notificar a administradores
        emailService.notificarNuevoComprobante(comprobante);

        return mapearAResponse(comprobante);
    }

    /**
     * Obtener comprobante por ID
     */
    @Transactional(readOnly = true)
    public ComprobantePagoResponseDTO obtenerComprobantePorId(Long id) {
        ComprobantePago comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));

        return mapearAResponse(comprobante);
    }

    /**
     * Obtener comprobantes de una proforma
     */
    @Transactional(readOnly = true)
    public List<ComprobantePagoResponseDTO> obtenerComprobantesPorProforma(Long proformaId) {
        return comprobanteRepository.findByProformaIdOrderByFechaSubidaDesc(proformaId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener comprobantes de un cliente
     */
    @Transactional(readOnly = true)
    public List<ComprobantePagoResponseDTO> obtenerComprobantesPorCliente(Long clienteId) {
        return comprobanteRepository.findByClienteIdOrderByFechaSubidaDesc(clienteId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los comprobantes pendientes (ADMIN)
     */
    @Transactional(readOnly = true)
    public List<ComprobantePagoResponseDTO> obtenerComprobantesPendientes() {
        return comprobanteRepository.findComprobantesPendientes()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener comprobantes por estado (ADMIN)
     */
    @Transactional(readOnly = true)
    public List<ComprobantePagoResponseDTO> obtenerComprobantesPorEstado(EstadoComprobante estado) {
        return comprobanteRepository.findByEstadoOrderByFechaSubidaDesc(estado)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Verificar y aprobar un comprobante (ADMIN)
     */
    @Transactional
    public ComprobantePagoResponseDTO verificarComprobante(Long comprobanteId, Long adminId) {

        ComprobantePago comprobante = comprobanteRepository.findById(comprobanteId)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));

        if (comprobante.getEstado() != EstadoComprobante.PENDIENTE) {
            throw new RuntimeException("Solo se pueden verificar comprobantes pendientes");
        }

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));

        // Actualizar estado del comprobante
        comprobante.setEstado(EstadoComprobante.VERIFICADO);
        comprobante.setVerificadoPor(admin);
        comprobante.setFechaVerificacion(LocalDateTime.now());

        comprobante = comprobanteRepository.save(comprobante);

        // Actualizar estado de la proforma a PAGADA
        Proforma proforma = comprobante.getProforma();
        proforma.setEstado(EstadoProforma.PAGADA);
        proformaRepository.save(proforma);

        log.info("Comprobante {} verificado por admin {} - Proforma {} marcada como PAGADA",
                comprobanteId, adminId, proforma.getCodigo());

        // Notificar al cliente
        emailService.notificarComprobante VerificadoEmail(comprobante);

        return mapearAResponse(comprobante);
    }

    /**
     * Rechazar un comprobante (ADMIN)
     */
    @Transactional
    public ComprobantePagoResponseDTO rechazarComprobante(Long comprobanteId, String motivo, Long adminId) {

        ComprobantePago comprobante = comprobanteRepository.findById(comprobanteId)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));

        if (comprobante.getEstado() != EstadoComprobante.PENDIENTE) {
            throw new RuntimeException("Solo se pueden rechazar comprobantes pendientes");
        }

        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));

        // Actualizar estado del comprobante
        comprobante.setEstado(EstadoComprobante.RECHAZADO);
        comprobante.setVerificadoPor(admin);
        comprobante.setFechaVerificacion(LocalDateTime.now());
        comprobante.setObservaciones(motivo);

        comprobante = comprobanteRepository.save(comprobante);

        log.info("Comprobante {} rechazado por admin {}", comprobanteId, adminId);

        // Notificar al cliente
        emailService.notificarComprobanteRechazado(comprobante);

        return mapearAResponse(comprobante);
    }

    /**
     * Contar comprobantes por estado
     */
    @Transactional(readOnly = true)
    public long contarPorEstado(EstadoComprobante estado) {
        return comprobanteRepository.countByEstado(estado);
    }

    /**
     * Verificar si ya existe un comprobante verificado para una proforma
     */
    @Transactional(readOnly = true)
    public boolean existeComprobanteVerificado(Long proformaId) {
        return comprobanteRepository.existsByProformaIdAndEstado(proformaId, EstadoComprobante.VERIFICADO);
    }

    // ============================================
    // MÉTODO DE MAPEO
    // ============================================

    private ComprobantePagoResponseDTO mapearAResponse(ComprobantePago comprobante) {
        return ComprobantePagoResponseDTO.builder()
                .id(comprobante.getId())
                .proformaId(comprobante.getProforma().getId())
                .proformaCodigo(comprobante.getProforma().getCodigo())
                .clienteNombre(comprobante.getCliente().getNombreCompleto())
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
}
