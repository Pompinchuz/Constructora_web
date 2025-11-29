package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import com.constructora.backend.controller.dto.ComprobantePagoRequestDTO;
import com.constructora.backend.controller.dto.ComprobantePagoResponseDTO;
import com.constructora.backend.entity.Administrador;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.Usuario;
import com.constructora.backend.entity.enums.EstadoComprobante;
import com.constructora.backend.repository.AdministradorRepository;
import com.constructora.backend.repository.ClienteRepository;
import com.constructora.backend.repository.UsuarioRepository;
import com.constructora.backend.service.ComprobantePagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ComprobantePagoController {

    private final ComprobantePagoService comprobanteService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;

    /**
     * Subir un comprobante de pago (CLIENTE)
     * POST /api/comprobantes
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
    public ResponseEntity<ApiResponseDTO<ComprobantePagoResponseDTO>> subirComprobante(
            @RequestParam("proformaId") Long proformaId,
            @RequestParam("monto") BigDecimal monto,
            @RequestParam(value = "numeroOperacion", required = false) String numeroOperacion,
            @RequestParam(value = "entidadBancaria", required = false) String entidadBancaria,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            @RequestParam("archivo") MultipartFile archivo,
            Authentication authentication) {

        Long clienteId = obtenerClienteId(authentication);

        // Crear DTO
        ComprobantePagoRequestDTO dto = new ComprobantePagoRequestDTO();
        dto.setProformaId(proformaId);
        dto.setMonto(monto);
        dto.setNumeroOperacion(numeroOperacion);
        dto.setEntidadBancaria(entidadBancaria);
        dto.setObservaciones(observaciones);
        dto.setArchivo(archivo);

        log.info("Cliente {} subiendo comprobante para proforma {}", clienteId, proformaId);

        ComprobantePagoResponseDTO response = comprobanteService.subirComprobante(clienteId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.<ComprobantePagoResponseDTO>builder()
                        .success(true)
                        .message("Comprobante subido exitosamente")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Obtener comprobante por ID (CLIENTE y ADMIN)
     * GET /api/comprobantes/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ComprobantePagoResponseDTO>> obtenerComprobantePorId(
            @PathVariable Long id) {

        log.info("Obteniendo comprobante ID: {}", id);

        ComprobantePagoResponseDTO response = comprobanteService.obtenerComprobantePorId(id);

        return ResponseEntity.ok(
                ApiResponseDTO.<ComprobantePagoResponseDTO>builder()
                        .success(true)
                        .message("Comprobante encontrado")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Obtener comprobantes de una proforma (CLIENTE y ADMIN)
     * GET /api/comprobantes/proforma/{proformaId}
     */
    @GetMapping("/proforma/{proformaId}")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<List<ComprobantePagoResponseDTO>>> obtenerComprobantesPorProforma(
            @PathVariable Long proformaId) {

        log.info("Obteniendo comprobantes de proforma {}", proformaId);

        List<ComprobantePagoResponseDTO> response = comprobanteService.obtenerComprobantesPorProforma(proformaId);

        return ResponseEntity.ok(
                ApiResponseDTO.<List<ComprobantePagoResponseDTO>>builder()
                        .success(true)
                        .message("Comprobantes obtenidos")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Obtener mis comprobantes (CLIENTE)
     * GET /api/comprobantes/mis-comprobantes
     */
    @GetMapping("/mis-comprobantes")
    @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
    public ResponseEntity<ApiResponseDTO<List<ComprobantePagoResponseDTO>>> obtenerMisComprobantes(
            Authentication authentication) {

        Long clienteId = obtenerClienteId(authentication);

        log.info("Cliente {} obteniendo sus comprobantes", clienteId);

        List<ComprobantePagoResponseDTO> response = comprobanteService.obtenerComprobantesPorCliente(clienteId);

        return ResponseEntity.ok(
                ApiResponseDTO.<List<ComprobantePagoResponseDTO>>builder()
                        .success(true)
                        .message("Comprobantes obtenidos")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Obtener comprobantes pendientes (ADMIN)
     * GET /api/comprobantes/admin/pendientes
     */
    @GetMapping("/admin/pendientes")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<List<ComprobantePagoResponseDTO>>> obtenerComprobantesPendientes() {

        log.info("Admin obteniendo comprobantes pendientes");

        List<ComprobantePagoResponseDTO> response = comprobanteService.obtenerComprobantesPendientes();

        return ResponseEntity.ok(
                ApiResponseDTO.<List<ComprobantePagoResponseDTO>>builder()
                        .success(true)
                        .message("Comprobantes pendientes obtenidos")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Obtener comprobantes por estado (ADMIN)
     * GET /api/comprobantes/admin/por-estado
     */
    @GetMapping("/admin/por-estado")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<List<ComprobantePagoResponseDTO>>> obtenerComprobantesPorEstado(
            @RequestParam EstadoComprobante estado) {

        log.info("Admin obteniendo comprobantes con estado: {}", estado);

        List<ComprobantePagoResponseDTO> response = comprobanteService.obtenerComprobantesPorEstado(estado);

        return ResponseEntity.ok(
                ApiResponseDTO.<List<ComprobantePagoResponseDTO>>builder()
                        .success(true)
                        .message("Comprobantes obtenidos")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Verificar comprobante (ADMIN)
     * POST /api/comprobantes/{id}/verificar
     */
    @PostMapping("/{id}/verificar")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ComprobantePagoResponseDTO>> verificarComprobante(
            @PathVariable Long id,
            Authentication authentication) {

        Long adminId = obtenerAdminId(authentication);

        log.info("Admin {} verificando comprobante {}", adminId, id);

        ComprobantePagoResponseDTO response = comprobanteService.verificarComprobante(id, adminId);

        return ResponseEntity.ok(
                ApiResponseDTO.<ComprobantePagoResponseDTO>builder()
                        .success(true)
                        .message("Comprobante verificado y pago confirmado")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Rechazar comprobante (ADMIN)
     * POST /api/comprobantes/{id}/rechazar
     */
    @PostMapping("/{id}/rechazar")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<ComprobantePagoResponseDTO>> rechazarComprobante(
            @PathVariable Long id,
            @RequestParam String motivo,
            Authentication authentication) {

        Long adminId = obtenerAdminId(authentication);

        log.info("Admin {} rechazando comprobante {}", adminId, id);

        ComprobantePagoResponseDTO response = comprobanteService.rechazarComprobante(id, motivo, adminId);

        return ResponseEntity.ok(
                ApiResponseDTO.<ComprobantePagoResponseDTO>builder()
                        .success(true)
                        .message("Comprobante rechazado")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Contar comprobantes pendientes (ADMIN)
     * GET /api/comprobantes/admin/pendientes/count
     */
    @GetMapping("/admin/pendientes/count")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponseDTO<Long>> contarComprobantesPendientes() {

        long count = comprobanteService.contarPorEstado(EstadoComprobante.PENDIENTE);

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
     * 🔒 SEGURIDAD: Obtiene el ID del cliente autenticado
     */
    private Long obtenerClienteId(Authentication authentication) {
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para usuario: " + email));

        log.debug("Cliente ID {} obtenido para usuario {}", cliente.getId(), email);

        return cliente.getId();
    }

    /**
     * 🔒 SEGURIDAD: Obtiene el ID del administrador autenticado
     */
    private Long obtenerAdminId(Authentication authentication) {
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        Administrador administrador = administradorRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado para usuario: " + email));

        log.debug("Administrador ID {} obtenido para usuario {}", administrador.getId(), email);

        return administrador.getId();
    }
}
