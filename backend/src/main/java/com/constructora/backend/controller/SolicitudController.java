// ============================================
// SOLICITUD CONTROLLER
// ============================================

// SolicitudController.java
package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import com.constructora.backend.controller.dto.SolicitudProformaDTO;
import com.constructora.backend.controller.dto.SolicitudProformaResponseDTO;
import com.constructora.backend.entity.Administrador;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.Usuario;
import com.constructora.backend.entity.enums.EstadoSolicitud;
import com.constructora.backend.repository.AdministradorRepository;
import com.constructora.backend.repository.ClienteRepository;
import com.constructora.backend.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    
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

        // 🔒 SEGURIDAD: Validar que el cliente solo pueda ver sus propias solicitudes
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("CLIENTE_NATURAL") ||
                              a.getAuthority().equals("CLIENTE_JURIDICO"))) {

            Long clienteId = obtenerClienteId(authentication);

            // Verificar que la solicitud pertenece al cliente autenticado
            if (!solicitudService.solicitudPerteneceACliente(id, clienteId)) {
                log.warn("Cliente {} intentó acceder a solicitud {} que no le pertenece",
                        clienteId, id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    ApiResponseDTO.<SolicitudProformaResponseDTO>builder()
                        .success(false)
                        .message("No tiene permisos para acceder a esta solicitud")
                        .timestamp(LocalDateTime.now())
                        .build()
                );
            }
        }

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

    /**
     * 🔒 SEGURIDAD: Obtiene el ID del cliente autenticado desde el Authentication
     * @param authentication Objeto de autenticación de Spring Security
     * @return ID del cliente
     */
    private Long obtenerClienteId(Authentication authentication) {
        String email = authentication.getName(); // El correo está en el "name" del Authentication

        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        // Buscar cliente por usuarioId
        Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para usuario: " + email));

        log.debug("Cliente ID {} obtenido para usuario {}", cliente.getId(), email);

        return cliente.getId();
    }

    /**
     * 🔒 SEGURIDAD: Obtiene el ID del administrador autenticado desde el Authentication
     * @param authentication Objeto de autenticación de Spring Security
     * @return ID del administrador
     */
    private Long obtenerAdminId(Authentication authentication) {
        String email = authentication.getName(); // El correo está en el "name" del Authentication

        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        // Buscar administrador por usuarioId
        Administrador administrador = administradorRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado para usuario: " + email));

        log.debug("Administrador ID {} obtenido para usuario {}", administrador.getId(), email);

        return administrador.getId();
    }
}