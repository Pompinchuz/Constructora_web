// ============================================
// PROFORMA CONTROLLER
// ============================================

// ProformaController.java
package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import com.constructora.backend.controller.dto.CrearProformaDTO;
import com.constructora.backend.controller.dto.ProformaEstadisticasDTO;
import com.constructora.backend.controller.dto.ProformaResponseDTO;
import com.constructora.backend.entity.Administrador;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.Usuario;
import com.constructora.backend.entity.enums.EstadoProforma;
import com.constructora.backend.repository.AdministradorRepository;
import com.constructora.backend.repository.ClienteRepository;
import com.constructora.backend.repository.UsuarioRepository;
import com.constructora.backend.service.ProformaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/proformas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ProformaController {

    private final ProformaService proformaService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    
    /**
     * Crear nueva proforma (ADMIN)
     * POST /api/proformas
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ProformaResponseDTO>> crearProforma(
            @Valid @RequestBody CrearProformaDTO request,
            Authentication authentication) {
        
        Long adminId = obtenerAdminId(authentication);
        
        log.info("Admin {} creando proforma para solicitud {}", adminId, request.getSolicitudId());
        
        ProformaResponseDTO response = proformaService.crearProforma(request, adminId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<ProformaResponseDTO>builder()
                .success(true)
                .message("Proforma creada exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Enviar proforma por correo (ADMIN)
     * POST /api/proformas/{id}/enviar
     */
    @PostMapping("/{id}/enviar")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<Void>> enviarProforma(@PathVariable Long id) {
        
        log.info("Enviando proforma ID: {}", id);
        
        proformaService.enviarProforma(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Proforma enviada por correo exitosamente")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Obtener proforma por ID
     * GET /api/proformas/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ProformaResponseDTO>> obtenerProformaPorId(
            @PathVariable Long id) {
        
        log.info("Obteniendo proforma ID: {}", id);
        
        ProformaResponseDTO response = proformaService.obtenerProformaPorId(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ProformaResponseDTO>builder()
                .success(true)
                .message("Proforma encontrada")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Obtener proforma por código
     * GET /api/proformas/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ProformaResponseDTO>> obtenerProformaPorCodigo(
            @PathVariable String codigo) {
        
        log.info("Obteniendo proforma con código: {}", codigo);
        
        ProformaResponseDTO response = proformaService.obtenerProformaPorCodigo(codigo);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ProformaResponseDTO>builder()
                .success(true)
                .message("Proforma encontrada")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Obtener mis proformas (CLIENTE)
     * GET /api/proformas/mis-proformas
     */
    @GetMapping("/mis-proformas")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
    public ResponseEntity<ApiResponseDTO<List<ProformaResponseDTO>>> obtenerMisProformas(
            Authentication authentication) {
        
        Long clienteId = obtenerClienteId(authentication);
        
        log.info("Cliente {} obteniendo sus proformas", clienteId);
        
        List<ProformaResponseDTO> proformas = 
            proformaService.obtenerProformasPorCliente(clienteId);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ProformaResponseDTO>>builder()
                .success(true)
                .message("Proformas obtenidas")
                .data(proformas)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Listar todas las proformas (ADMIN)
     * GET /api/proformas/admin/todas
     */
    @GetMapping("/admin/todas")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<List<ProformaResponseDTO>>> listarTodasProformas(
            @RequestParam(required = false) EstadoProforma estado) {
        
        log.info("Admin listando proformas con estado: {}", estado);
        
        List<ProformaResponseDTO> proformas = proformaService.obtenerTodasProformas(estado);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ProformaResponseDTO>>builder()
                .success(true)
                .message("Proformas obtenidas")
                .data(proformas)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Actualizar estado de proforma (ADMIN)
     * PATCH /api/proformas/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ProformaResponseDTO>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoProforma estado) {
        
        log.info("Actualizando estado de proforma {} a {}", id, estado);
        
        ProformaResponseDTO response = proformaService.actualizarEstado(id, estado);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ProformaResponseDTO>builder()
                .success(true)
                .message("Estado actualizado")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Marcar proforma como vista (CLIENTE)
     * POST /api/proformas/{id}/marcar-vista
     */
    @PostMapping("/{id}/marcar-vista")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
    public ResponseEntity<ApiResponseDTO<Void>> marcarComoVista(@PathVariable Long id) {
        
        log.info("Marcando proforma {} como vista", id);
        
        proformaService.actualizarEstado(id, EstadoProforma.VISTA);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Proforma marcada como vista")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Eliminar proforma (ADMIN - solo si no ha sido enviada)
     * DELETE /api/proformas/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarProforma(@PathVariable Long id) {
        
        log.info("Eliminando proforma ID: {}", id);
        
        proformaService.eliminarProforma(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Proforma eliminada")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    /**
     * Obtener estadísticas de proformas (ADMIN)
     * GET /api/proformas/admin/estadisticas
     */
    @GetMapping("/admin/estadisticas")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ProformaEstadisticasDTO>> obtenerEstadisticas() {
        
        log.info("Obteniendo estadísticas de proformas");
        
        ProformaEstadisticasDTO estadisticas = proformaService.obtenerEstadisticas();
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ProformaEstadisticasDTO>builder()
                .success(true)
                .message("Estadísticas obtenidas")
                .data(estadisticas)
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