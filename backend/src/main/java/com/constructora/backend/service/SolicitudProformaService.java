package com.constructora.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.constructora.backend.controller.dto.SolicitudProformaDTO;
import com.constructora.backend.controller.dto.SolicitudProformaResponseDTO;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.SolicitudProforma;
import com.constructora.backend.entity.enums.EstadoSolicitud;
import com.constructora.backend.exception.NotFoundException;
import com.constructora.backend.repository.ClienteRepository;
import com.constructora.backend.repository.SolicitudProformaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudProformaService {
    
    private final SolicitudProformaRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    
    /**
     * Crear nueva solicitud de proforma
     */
    @Transactional
    public SolicitudProformaResponseDTO crearSolicitud(Long clienteId, SolicitudProformaDTO dto) {
        
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        
        SolicitudProforma solicitud = new SolicitudProforma();
        solicitud.setCliente(cliente);
        solicitud.setTitulo(dto.getTitulo());
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        
        // Guardar archivo adjunto si existe
        if (dto.getArchivo() != null && !dto.getArchivo().isEmpty()) {
            String rutaArchivo = fileStorageService.guardarArchivo(dto.getArchivo(), "solicitudes");
            solicitud.setArchivoAdjunto(rutaArchivo);
        }
        
        solicitud = solicitudRepository.save(solicitud);
        
        log.info("Solicitud creada con ID: {}", solicitud.getId());
        
        // Notificar a administradores
        emailService.notificarNuevaSolicitud(solicitud);
        
        return mapearAResponse(solicitud);
    }
    
    /**
     * Obtener solicitud por ID
     */
    @Transactional(readOnly = true)
    public SolicitudProformaResponseDTO obtenerSolicitudPorId(Long id) {
        SolicitudProforma solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        
        log.info("Solicitud {} obtenida", id);
        
        return mapearAResponse(solicitud);
    }
    
    /**
     * Obtener solicitudes por cliente
     */
    @Transactional(readOnly = true)
    public List<SolicitudProformaResponseDTO> obtenerSolicitudesPorCliente(Long clienteId) {
        log.info("Obteniendo solicitudes del cliente {}", clienteId);
        
        return solicitudRepository.findByClienteIdOrderByFechaSolicitudDesc(clienteId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener todas las solicitudes (con filtro opcional por estado)
     */
    @Transactional(readOnly = true)
    public List<SolicitudProformaResponseDTO> obtenerTodasSolicitudes(EstadoSolicitud estado) {
        List<SolicitudProforma> solicitudes;
        
        if (estado != null) {
            log.info("Obteniendo solicitudes con estado: {}", estado);
            solicitudes = solicitudRepository.findByEstadoOrderByFechaSolicitudDesc(estado);
        } else {
            log.info("Obteniendo todas las solicitudes");
            solicitudes = solicitudRepository.findAllByOrderByFechaSolicitudDesc();
        }
        
        return solicitudes.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Cambiar estado de una solicitud
     */
    @Transactional
    public SolicitudProformaResponseDTO cambiarEstado(
            Long solicitudId, 
            EstadoSolicitud nuevoEstado,
            String motivoRechazo,
            Long adminId) {
        
        SolicitudProforma solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        
        EstadoSolicitud estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaRevision(LocalDateTime.now());
        
        if (nuevoEstado == EstadoSolicitud.RECHAZADA && motivoRechazo != null) {
            solicitud.setMotivoRechazo(motivoRechazo);
        }
        
        solicitud = solicitudRepository.save(solicitud);
        
        log.info("Solicitud {} cambió de estado {} a {}", 
                solicitudId, estadoAnterior, nuevoEstado);
        
        // Notificar al cliente si fue rechazada
        if (nuevoEstado == EstadoSolicitud.RECHAZADA) {
            emailService.notificarSolicitudRechazada(solicitud);
        }
        
        return mapearAResponse(solicitud);
    }
    
    /**
     * Contar solicitudes pendientes
     */
    @Transactional(readOnly = true)
    public long contarSolicitudesPendientes() {
        long count = solicitudRepository.countSolicitudesPendientes();
        log.info("Total de solicitudes pendientes: {}", count);
        return count;
    }
    
    /**
     * Contar solicitudes por estado
     */
    @Transactional(readOnly = true)
    public long contarPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.countByEstado(estado);
    }
    
    /**
     * Contar solicitudes de un cliente
     */
    @Transactional(readOnly = true)
    public long contarPorCliente(Long clienteId) {
        return solicitudRepository.countByClienteId(clienteId);
    }

    /**
     * 🔒 SEGURIDAD: Verifica que una solicitud pertenece a un cliente específico
     */
    @Transactional(readOnly = true)
    public boolean solicitudPerteneceACliente(Long solicitudId, Long clienteId) {
        SolicitudProforma solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        boolean pertenece = solicitud.getCliente().getId().equals(clienteId);

        log.debug("Solicitud {} pertenece a cliente {}: {}", solicitudId, clienteId, pertenece);

        return pertenece;
    }

    // ============================================
    // MÉTODO DE MAPEO
    // ============================================
    
    private SolicitudProformaResponseDTO mapearAResponse(SolicitudProforma solicitud) {
        return SolicitudProformaResponseDTO.builder()
                .id(solicitud.getId())
                .titulo(solicitud.getTitulo())
                .descripcion(solicitud.getDescripcion())
                .archivoAdjunto(solicitud.getArchivoAdjunto())
                .estado(solicitud.getEstado())
                .motivoRechazo(solicitud.getMotivoRechazo())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .fechaRevision(solicitud.getFechaRevision())
                .revisadoPor(solicitud.getRevisadoPor() != null ? 
                        solicitud.getRevisadoPor().getNombreCompleto() : null)
                .clienteNombre(solicitud.getCliente().getNombreCompleto())
                .build();
    }
}