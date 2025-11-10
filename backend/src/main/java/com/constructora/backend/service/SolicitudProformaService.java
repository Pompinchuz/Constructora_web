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


@Service
@RequiredArgsConstructor
public class SolicitudProformaService {
    
    private final SolicitudProformaRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    
    @Transactional
    public SolicitudProformaResponseDTO crearSolicitud(
            Long clienteId, 
            SolicitudProformaDTO dto) {
        
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        
        SolicitudProforma solicitud = new SolicitudProforma();
        solicitud.setCliente(cliente);
        solicitud.setTitulo(dto.getTitulo());
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        
        // Guardar archivo adjunto si existe
        if (dto.getArchivo() != null && !dto.getArchivo().isEmpty()) {
            String rutaArchivo = fileStorageService.guardarArchivo(
                    dto.getArchivo(), "solicitudes");
            solicitud.setArchivoAdjunto(rutaArchivo);
        }
        
        solicitud = solicitudRepository.save(solicitud);
        
        // Notificar a administradores
        emailService.notificarNuevaSolicitud(solicitud);
        
        return mapearAResponse(solicitud);
    }
    
    @Transactional
    public List<SolicitudProformaResponseDTO> obtenerSolicitudesPorCliente(Long clienteId) {
        return solicitudRepository.findByClienteIdOrderByFechaSolicitudDesc(clienteId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<SolicitudProformaResponseDTO> obtenerTodasSolicitudes(EstadoSolicitud estado) {
        List<SolicitudProforma> solicitudes;
        
        if (estado != null) {
            solicitudes = solicitudRepository.findByEstadoOrderByFechaSolicitudDesc(estado);
        } else {
            solicitudes = solicitudRepository.findAllByOrderByFechaSolicitudDesc();
        }
        
        return solicitudes.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SolicitudProformaResponseDTO cambiarEstado(
            Long solicitudId, 
            EstadoSolicitud nuevoEstado,
            String motivoRechazo,
            Long adminId) {
        
        SolicitudProforma solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        
        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaRevision(LocalDateTime.now());
        
        if (nuevoEstado == EstadoSolicitud.RECHAZADA && motivoRechazo != null) {
            solicitud.setMotivoRechazo(motivoRechazo);
        }
        
        solicitud = solicitudRepository.save(solicitud);
        
        // Notificar al cliente
        if (nuevoEstado == EstadoSolicitud.RECHAZADA) {
            emailService.notificarSolicitudRechazada(solicitud);
        }
        
        return mapearAResponse(solicitud);
    }
    
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