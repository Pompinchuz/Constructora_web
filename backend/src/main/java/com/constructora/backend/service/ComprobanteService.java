package com.constructora.backend.service;

import com.constructora.backend.controller.dto.comprobante.ComprobanteResponseDTO;
import com.constructora.backend.controller.dto.comprobante.SubirComprobanteDTO;
import com.constructora.backend.exception.ForbiddenException;
import com.constructora.backend.exception.NotFoundException;

import com.constructora.backend.repository.ComprobantePagoRepository;
import com.constructora.backend.repository.ProformaRepository;
import com.constructora.backend.repository.AdministradorRepository;
import com.constructora.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.constructora.backend.entity.ComprobantePago;
import com.constructora.backend.entity.Proforma;
import com.constructora.backend.entity.Administrador;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.enums.EstadoComprobante;
import com.constructora.backend.entity.enums.EstadoProforma;

@Service
@RequiredArgsConstructor
public class ComprobanteService {
    
    private final ComprobantePagoRepository comprobanteRepository;
    private final ProformaRepository proformaRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    
    @Transactional
    public ComprobanteResponseDTO subirComprobante(Long clienteId, SubirComprobanteDTO dto) {
        
        Proforma proforma = proformaRepository.findById(dto.getProformaId())
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        
        if (!proforma.getCliente().getId().equals(clienteId)) {
            throw new ForbiddenException("No tiene permiso para esta proforma");
        }
        
        // Guardar archivo
        String rutaArchivo = fileStorageService.guardarArchivo(
                dto.getArchivo(), "comprobantes");
        
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setProforma(proforma);
        comprobante.setCliente(cliente);
        comprobante.setMonto(dto.getMonto());
        comprobante.setNumeroOperacion(dto.getNumeroOperacion());
        comprobante.setEntidadBancaria(dto.getEntidadBancaria());
        comprobante.setArchivoComprobante(rutaArchivo);
        comprobante.setEstado(EstadoComprobante.PENDIENTE);
        
        comprobante = comprobanteRepository.save(comprobante);
        
        // Notificar a administradores
        emailService.notificarNuevoComprobante(comprobante);
        
        return mapearAResponse(comprobante);
    }
    
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
        
        return comprobantes.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ComprobanteResponseDTO verificarComprobante(Long id, Long adminId, String observaciones) {
        ComprobantePago comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));
        
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
        
        comprobante.setEstado(EstadoComprobante.VERIFICADO);
        comprobante.setObservaciones(observaciones);
        comprobante.setVerificadoPor(admin);
        comprobante.setFechaVerificacion(LocalDateTime.now());
        
        comprobante = comprobanteRepository.save(comprobante);
        
        // Actualizar estado de la proforma si corresponde
        Proforma proforma = comprobante.getProforma();
        if (proforma.getEstado() != EstadoProforma.PAGADA) {
            proforma.setEstado(EstadoProforma.PAGADA);
            proformaRepository.save(proforma);
        }
        
        return mapearAResponse(comprobante);
    }
    
    @Transactional
    public ComprobanteResponseDTO rechazarComprobante(Long id, Long adminId, String observaciones) {
        ComprobantePago comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));
        
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
        
        comprobante.setEstado(EstadoComprobante.RECHAZADO);
        comprobante.setObservaciones(observaciones);
        comprobante.setVerificadoPor(admin);
        comprobante.setFechaVerificacion(LocalDateTime.now());
        
        comprobante = comprobanteRepository.save(comprobante);
        
        return mapearAResponse(comprobante);
    }
    
    @Transactional
    public ComprobanteResponseDTO actualizarEstado(Long id, EstadoComprobante estado, 
                                                    Long adminId, String observaciones) {
        ComprobantePago comprobante = comprobanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comprobante no encontrado"));
        
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
        
        comprobante = comprobanteRepository.save(comprobante);
        
        return mapearAResponse(comprobante);
    }
    
    public long contarComprobantesPendientes() {
        return comprobanteRepository.countByEstado(EstadoComprobante.PENDIENTE);
    }
    
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
}