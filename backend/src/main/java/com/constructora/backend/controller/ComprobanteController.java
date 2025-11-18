// // ============================================
// // COMPROBANTE CONTROLLER
// // ============================================

// // ComprobanteController.java
// package com.constructora.backend.controller;

// import com.constructora.backend.controller.dto.ApiResponseDTO;
// import com.constructora.backend.controller.dto.comprobante.ComprobanteResponseDTO;
// import com.constructora.backend.controller.dto.comprobante.SubirComprobanteDTO;
// import com.constructora.backend.entity.enums.EstadoComprobante;
// import com.constructora.backend.service.ComprobanteService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.List;

// @RestController
// @RequestMapping("/api/comprobantes")
// @RequiredArgsConstructor
// @Slf4j
// @CrossOrigin(origins = "${cors.allowed-origins}")
// public class ComprobanteController {
    
//     private final ComprobanteService comprobanteService;
    
//     /**
//      * Subir comprobante de pago (CLIENTE)
//      * POST /api/comprobantes
//      */
//     @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//     @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
//     public ResponseEntity<ApiResponseDTO<ComprobanteResponseDTO>> subirComprobante(
//             @RequestParam("proformaId") Long proformaId,
//             @RequestParam("monto") BigDecimal monto,
//             @RequestParam(value = "numeroOperacion", required = false) String numeroOperacion,
//             @RequestParam(value = "entidadBancaria", required = false) String entidadBancaria,
//             @RequestParam("archivo") MultipartFile archivo,
//             Authentication authentication) {
        
//         Long clienteId = obtenerClienteId(authentication);
        
//         SubirComprobanteDTO dto = new SubirComprobanteDTO();
//         dto.setProformaId(proformaId);
//         dto.setMonto(monto);
//         dto.setNumeroOperacion(numeroOperacion);
//         dto.setEntidadBancaria(entidadBancaria);
//         dto.setArchivo(archivo);
        
//         log.info("Cliente {} subiendo comprobante para proforma {}", clienteId, proformaId);
        
//         ComprobanteResponseDTO response = comprobanteService.subirComprobante(clienteId, dto);
        
//         return ResponseEntity.status(HttpStatus.CREATED).body(
//             ApiResponseDTO.<ComprobanteResponseDTO>builder()
//                 .success(true)
//                 .message("Comprobante subido exitosamente")
//                 .data(response)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Obtener mis comprobantes (CLIENTE)
//      * GET /api/comprobantes/mis-comprobantes
//      */
//     @GetMapping("/mis-comprobantes")
//     @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO')")
//     public ResponseEntity<ApiResponseDTO<List<ComprobanteResponseDTO>>> obtenerMisComprobantes(
//             Authentication authentication) {
        
//         Long clienteId = obtenerClienteId(authentication);
        
//         log.info("Cliente {} obteniendo sus comprobantes", clienteId);
        
//         List<ComprobanteResponseDTO> comprobantes = 
//             comprobanteService.obtenerComprobantesPorCliente(clienteId);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<List<ComprobanteResponseDTO>>builder()
//                 .success(true)
//                 .message("Comprobantes obtenidos")
//                 .data(comprobantes)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Obtener comprobantes de una proforma (CLIENTE o ADMIN)
//      * GET /api/comprobantes/proforma/{proformaId}
//      */
//     @GetMapping("/proforma/{proformaId}")
//     @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<List<ComprobanteResponseDTO>>> obtenerComprobantesPorProforma(
//             @PathVariable Long proformaId) {
        
//         log.info("Obteniendo comprobantes de proforma {}", proformaId);
        
//         List<ComprobanteResponseDTO> comprobantes = 
//             comprobanteService.obtenerComprobantesPorProforma(proformaId);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<List<ComprobanteResponseDTO>>builder()
//                 .success(true)
//                 .message("Comprobantes obtenidos")
//                 .data(comprobantes)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Obtener comprobante por ID
//      * GET /api/comprobantes/{id}
//      */
//     @GetMapping("/{id}")
//     @PreAuthorize("hasAnyAuthority('CLIENTE_NATURAL', 'CLIENTE_JURIDICO', 'ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<ComprobanteResponseDTO>> obtenerComprobantePorId(
//             @PathVariable Long id) {
        
//         log.info("Obteniendo comprobante ID: {}", id);
        
//         ComprobanteResponseDTO response = comprobanteService.obtenerComprobantePorId(id);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<ComprobanteResponseDTO>builder()
//                 .success(true)
//                 .message("Comprobante encontrado")
//                 .data(response)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Listar todos los comprobantes (ADMIN)
//      * GET /api/comprobantes/admin/todos
//      */
//     @GetMapping("/admin/todos")
//     @PreAuthorize("hasAuthority('ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<List<ComprobanteResponseDTO>>> listarTodosComprobantes(
//             @RequestParam(required = false) EstadoComprobante estado) {
        
//         log.info("Admin listando comprobantes con estado: {}", estado);
        
//         List<ComprobanteResponseDTO> comprobantes = 
//             comprobanteService.obtenerTodosComprobantes(estado);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<List<ComprobanteResponseDTO>>builder()
//                 .success(true)
//                 .message("Comprobantes obtenidos")
//                 .data(comprobantes)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Verificar comprobante (ADMIN)
//      * POST /api/comprobantes/{id}/verificar
//      */
//     @PostMapping("/{id}/verificar")
//     @PreAuthorize("hasAuthority('ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<ComprobanteResponseDTO>> verificarComprobante(
//             @PathVariable Long id,
//             @RequestParam(required = false) String observaciones,
//             Authentication authentication) {
        
//         Long adminId = obtenerAdminId(authentication);
        
//         log.info("Admin {} verificando comprobante {}", adminId, id);
        
//         ComprobanteResponseDTO response = 
//             comprobanteService.verificarComprobante(id, adminId, observaciones);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<ComprobanteResponseDTO>builder()
//                 .success(true)
//                 .message("Comprobante verificado")
//                 .data(response)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Rechazar comprobante (ADMIN)
//      * POST /api/comprobantes/{id}/rechazar
//      */
//     @PostMapping("/{id}/rechazar")
//     @PreAuthorize("hasAuthority('ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<ComprobanteResponseDTO>> rechazarComprobante(
//             @PathVariable Long id,
//             @RequestParam String observaciones,
//             Authentication authentication) {
        
//         Long adminId = obtenerAdminId(authentication);
        
//         log.info("Admin {} rechazando comprobante {}", adminId, id);
        
//         ComprobanteResponseDTO response = 
//             comprobanteService.rechazarComprobante(id, adminId, observaciones);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<ComprobanteResponseDTO>builder()
//                 .success(true)
//                 .message("Comprobante rechazado")
//                 .data(response)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Actualizar estado de comprobante (ADMIN)
//      * PATCH /api/comprobantes/{id}/estado
//      */
//     @PatchMapping("/{id}/estado")
//     @PreAuthorize("hasAuthority('ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<ComprobanteResponseDTO>> actualizarEstado(
//             @PathVariable Long id,
//             @RequestParam EstadoComprobante estado,
//             @RequestParam(required = false) String observaciones,
//             Authentication authentication) {
        
//         Long adminId = obtenerAdminId(authentication);
        
//         log.info("Admin {} actualizando estado de comprobante {} a {}", adminId, id, estado);
        
//         ComprobanteResponseDTO response = 
//             comprobanteService.actualizarEstado(id, estado, adminId, observaciones);
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<ComprobanteResponseDTO>builder()
//                 .success(true)
//                 .message("Estado actualizado")
//                 .data(response)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     /**
//      * Contar comprobantes pendientes (ADMIN)
//      * GET /api/comprobantes/admin/pendientes/count
//      */
//     @GetMapping("/admin/pendientes/count")
//     @PreAuthorize("hasAuthority('ADMINISTRADOR')")
//     public ResponseEntity<ApiResponseDTO<Long>> contarComprobantesPendientes() {
        
//         long count = comprobanteService.contarComprobantesPendientes();
        
//         return ResponseEntity.ok(
//             ApiResponseDTO.<Long>builder()
//                 .success(true)
//                 .message("Conteo realizado")
//                 .data(count)
//                 .timestamp(LocalDateTime.now())
//                 .build()
//         );
//     }
    
//     // ============================================
//     // MÉTODOS AUXILIARES
//     // ============================================
    
//     private Long obtenerClienteId(Authentication authentication) {
//         return 1L; // Placeholder - implementar correctamente
//     }
    
//     private Long obtenerAdminId(Authentication authentication) {
//         return 1L; // Placeholder - implementar correctamente
//     }
// }