// ============================================
// SOLICITUD CONTROLLER
// ============================================

// SolicitudController.java
package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import com.constructora.backend.controller.dto.SolicitudProformaDTO;
import com.constructora.backend.controller.dto.SolicitudProformaResponseDTO;
import com.constructora.backend.entity.enums.EstadoSolicitud;
import com.constructora.backend.service.SolicitudProformaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class SolicitudController {
    
    private final SolicitudProformaService solicitudService;
    
    /**
     * Crear nueva solicitud de proforma (CLIENTE)
     * POST /api/solicitudes
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
    public ResponseEntity<ApiResponseDTO<SolicitudProformaResponseDTO>> crearSolicitud(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            Authentication authentication) {
        
        Long clienteId = obtenerClienteId(authentication);
        
        SolicitudProformaDTO dto = new SolicitudProformaDTO();
        dto.setTitulo(titulo);
        dto.setDescripcion(descripcion);
        dto.setArchivo(archivo);
        
        log.info("Cliente {} creando solicitud: {}", clienteId, titulo);
        
        SolicitudProformaResponseDTO response = solicitudService.crearSolicitud(clienteId, dto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<SolicitudProformaResponseDTO>builder()
                .success(true)
                .message("Solicitud creada exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Obtener mis solicitudes (CLIENTE)
     * GET /api/solicitudes/mis-solicitudes
     */
    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasAnyRole('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
    public ResponseEntity<ApiResponseDTO<List<SolicitudProformaResponseDTO>>> obtenerMisSolicitudes(
            Authentication authentication) {
        
        Long clienteId = obtenerClienteId(authentication);
        
        log.info("Cliente {} obteniendo sus solicitudes", clienteId);
        
        List<SolicitudProformaResponseDTO> solicitudes = 
            solicitudService.obtenerSolicitudesPorCliente(clienteId);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<SolicitudProformaResponseDTO>>builder()
                .success(true)
                .message("Solicitudes obtenidas")
                .data(solicitudes)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Obtener solicitud por ID (CLIENTE - solo sus propias solicitudes)
     * GET /api/solicitudes/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<SolicitudProformaResponseDTO>> obtenerSolicitudPorId(
            @PathVariable Long id,
            Authentication authentication) {
        
        log.info("Obteniendo solicitud ID: {}", id);
        
        SolicitudProformaResponseDTO response = solicitudService.obtenerSolicitudPorId(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<SolicitudProformaResponseDTO>builder()
                .success(true)
                .message("Solicitud encontrada")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Listar todas las solicitudes (ADMIN)
     * GET /api/solicitudes/admin/todas
     */
    @GetMapping("/admin/todas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<List<SolicitudProformaResponseDTO>>> listarTodasSolicitudes(
            @RequestParam(required = false) EstadoSolicitud estado) {
        
        log.info("Admin listando solicitudes con estado: {}", estado);
        
        List<SolicitudProformaResponseDTO> solicitudes = 
            solicitudService.obtenerTodasSolicitudes(estado);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<SolicitudProformaResponseDTO>>builder()
                .success(true)
                .message("Solicitudes obtenidas")
                .data(solicitudes)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Cambiar estado de solicitud (ADMIN)
     * PATCH /api/solicitudes/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<SolicitudProformaResponseDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoSolicitud estado,
            @RequestParam(required = false) String motivoRechazo,
            Authentication authentication) {
        
        Long adminId = obtenerAdminId(authentication);
        
        log.info("Admin {} cambiando estado de solicitud {} a {}", adminId, id, estado);
        
        SolicitudProformaResponseDTO response = 
            solicitudService.cambiarEstado(id, estado, motivoRechazo, adminId);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<SolicitudProformaResponseDTO>builder()
                .success(true)
                .message("Estado actualizado correctamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Aprobar solicitud (ADMIN)
     * POST /api/solicitudes/{id}/aprobar
     */
    @PostMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<SolicitudProformaResponseDTO>> aprobarSolicitud(
            @PathVariable Long id,
            Authentication authentication) {
        
        Long adminId = obtenerAdminId(authentication);
        
        log.info("Admin {} aprobando solicitud {}", adminId, id);
        
        SolicitudProformaResponseDTO response = 
            solicitudService.cambiarEstado(id, EstadoSolicitud.APROBADA, null, adminId);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<SolicitudProformaResponseDTO>builder()
                .success(true)
                .message("Solicitud aprobada")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Rechazar solicitud (ADMIN)
     * POST /api/solicitudes/{id}/rechazar
     */
    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<SolicitudProformaResponseDTO>> rechazarSolicitud(
            @PathVariable Long id,
            @RequestParam String motivo,
            Authentication authentication) {
        
        Long adminId = obtenerAdminId(authentication);
        
        log.info("Admin {} rechazando solicitud {}", adminId, id);
        
        SolicitudProformaResponseDTO response = 
            solicitudService.cambiarEstado(id, EstadoSolicitud.RECHAZADA, motivo, adminId);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<SolicitudProformaResponseDTO>builder()
                .success(true)
                .message("Solicitud rechazada")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Contar solicitudes pendientes (ADMIN)
     * GET /api/solicitudes/admin/pendientes/count
     */
    @GetMapping("/admin/pendientes/count")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<Long>> contarSolicitudesPendientes() {
        
        long count = solicitudService.contarSolicitudesPendientes();
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Long>builder()
                .success(true)
                .message("Conteo realizado")
                .data(count)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }







    
    
    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================
    
    private Long obtenerClienteId(Authentication authentication) {
        // Extraer clienteId del token JWT o del contexto de seguridad
        // Implementación depende de cómo almacenas el clienteId en el token
        return 1L; // Placeholder - implementar correctamente
    }
    
    private Long obtenerAdminId(Authentication authentication) {
        // Extraer adminId del token JWT o del contexto de seguridad
        return 1L; // Placeholder - implementar correctamente
    }
}