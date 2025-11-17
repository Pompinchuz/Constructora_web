// ============================================
// CONTENIDO CONTROLLER - CORREGIDO
// ============================================

package com.constructora.backend.controller;

import com.constructora.backend.controller.dto.ApiResponseDTO;
import com.constructora.backend.controller.dto.ImagenResponseDTO;
import com.constructora.backend.controller.dto.ProyectoExitosoDTO;
import com.constructora.backend.controller.dto.ProyectoExitosoResponseDTO;
import com.constructora.backend.entity.enums.TipoImagen;
import com.constructora.backend.service.ContenidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contenido")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ContenidoController {
    
    private final ContenidoService contenidoService;
    
    // ============================================
    // ENDPOINTS PÚBLICOS
    // ============================================
    
    @GetMapping("/imagenes/publico/{tipo}")
    public ResponseEntity<ApiResponseDTO<List<ImagenResponseDTO>>> obtenerImagenesPublicas(
            @PathVariable TipoImagen tipo) {
        
        log.info("Obteniendo imágenes públicas de tipo: {}", tipo);
        List<ImagenResponseDTO> imagenes = contenidoService.obtenerImagenesActivasPorTipo(tipo);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ImagenResponseDTO>>builder()
                .success(true)
                .message("Imágenes obtenidas")
                .data(imagenes)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @GetMapping("/imagenes/publico")
    public ResponseEntity<ApiResponseDTO<List<ImagenResponseDTO>>> obtenerTodasImagenesPublicas() {
        
        log.info("Obteniendo todas las imágenes públicas");
        List<ImagenResponseDTO> imagenes = contenidoService.obtenerTodasImagenesActivas();
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ImagenResponseDTO>>builder()
                .success(true)
                .message("Imágenes obtenidas")
                .data(imagenes)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @GetMapping("/proyectos/publico")
    public ResponseEntity<ApiResponseDTO<List<ProyectoExitosoResponseDTO>>> obtenerProyectosPublicos() {
        
        log.info("Obteniendo proyectos exitosos públicos");
        List<ProyectoExitosoResponseDTO> proyectos = contenidoService.obtenerProyectosActivos();
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ProyectoExitosoResponseDTO>>builder()
                .success(true)
                .message("Proyectos obtenidos")
                .data(proyectos)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @GetMapping("/proyectos/publico/{id}")
    public ResponseEntity<ApiResponseDTO<ProyectoExitosoResponseDTO>> obtenerProyectoPublicoPorId(
            @PathVariable Long id) {
        
        log.info("Obteniendo proyecto público ID: {}", id);
        ProyectoExitosoResponseDTO proyecto = contenidoService.obtenerProyectoPorId(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ProyectoExitosoResponseDTO>builder()
                .success(true)
                .message("Proyecto encontrado")
                .data(proyecto)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    // ============================================
    // GESTIÓN DE IMÁGENES (ADMIN) - USANDO hasAuthority
    // ============================================
    
    @PostMapping(value = "/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<ImagenResponseDTO>> subirImagen(
            @RequestParam("tipo") TipoImagen tipo,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "orden", required = false, defaultValue = "0") Integer orden) {
        
        log.info("Admin subiendo imagen de tipo: {}", tipo);
        ImagenResponseDTO response = contenidoService.subirImagen(tipo, titulo, descripcion, archivo, orden);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<ImagenResponseDTO>builder()
                .success(true)
                .message("Imagen subida exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @GetMapping("/imagenes/admin")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<List<ImagenResponseDTO>>> listarTodasImagenes(
            @RequestParam(required = false) TipoImagen tipo) {
        
        log.info("Admin listando imágenes");
        List<ImagenResponseDTO> imagenes = tipo != null 
            ? contenidoService.obtenerImagenesPorTipo(tipo)
            : contenidoService.obtenerTodasImagenes();
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ImagenResponseDTO>>builder()
                .success(true)
                .message("Imágenes obtenidas")
                .data(imagenes)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @PutMapping("/imagenes/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<ImagenResponseDTO>> actualizarImagen(
            @PathVariable Long id,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Integer orden,
            @RequestParam(required = false) Boolean activo) {
        
        log.info("Admin actualizando imagen ID: {}", id);
        ImagenResponseDTO response = contenidoService.actualizarImagen(id, titulo, descripcion, orden, activo);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ImagenResponseDTO>builder()
                .success(true)
                .message("Imagen actualizada")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @DeleteMapping("/imagenes/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<Void>> eliminarImagen(@PathVariable Long id) {
        
        log.info("Admin eliminando imagen ID: {}", id);
        contenidoService.eliminarImagen(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Imagen eliminada")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    // ============================================
    // GESTIÓN DE PROYECTOS (ADMIN) - USANDO hasAuthority
    // ============================================
    
    @PostMapping(value = "/proyectos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<ProyectoExitosoResponseDTO>> crearProyecto(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "ubicacion", required = false) String ubicacion,
            @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(value = "fechaFinalizacion", required = false) String fechaFinalizacion,
            @RequestParam(value = "imagenPrincipal", required = false) MultipartFile imagenPrincipal,
            @RequestParam(value = "imagenesAdicionales", required = false) List<MultipartFile> imagenesAdicionales) {
        
        log.info("Admin creando proyecto: {}", nombre);
        
        ProyectoExitosoDTO dto = new ProyectoExitosoDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setUbicacion(ubicacion);
        
        if (fechaInicio != null) dto.setFechaInicio(LocalDate.parse(fechaInicio));
        if (fechaFinalizacion != null) dto.setFechaFinalizacion(LocalDate.parse(fechaFinalizacion));
        
        dto.setImagenPrincipal(imagenPrincipal);
        dto.setImagenesAdicionales(imagenesAdicionales);
        
        ProyectoExitosoResponseDTO response = contenidoService.crearProyecto(dto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<ProyectoExitosoResponseDTO>builder()
                .success(true)
                .message("Proyecto creado exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @GetMapping("/proyectos/admin")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<List<ProyectoExitosoResponseDTO>>> listarTodosProyectos() {
        
        log.info("Admin listando todos los proyectos");
        List<ProyectoExitosoResponseDTO> proyectos = contenidoService.obtenerTodosProyectos();
        
        return ResponseEntity.ok(
            ApiResponseDTO.<List<ProyectoExitosoResponseDTO>>builder()
                .success(true)
                .message("Proyectos obtenidos")
                .data(proyectos)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @PutMapping("/proyectos/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<ProyectoExitosoResponseDTO>> actualizarProyecto(
            @PathVariable Long id,
            @RequestBody ProyectoExitosoDTO request) {
        
        log.info("Admin actualizando proyecto ID: {}", id);
        ProyectoExitosoResponseDTO response = contenidoService.actualizarProyecto(id, request);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<ProyectoExitosoResponseDTO>builder()
                .success(true)
                .message("Proyecto actualizado")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @PatchMapping("/proyectos/{id}/activo")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<Void>> cambiarEstadoProyecto(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        
        log.info("Admin cambiando estado de proyecto {} a {}", id, activo);
        contenidoService.cambiarEstadoProyecto(id, activo);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Estado actualizado")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
    
    @DeleteMapping("/proyectos/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")  // ← CAMBIADO
    public ResponseEntity<ApiResponseDTO<Void>> eliminarProyecto(@PathVariable Long id) {
        
        log.info("Admin eliminando proyecto ID: {}", id);
        contenidoService.eliminarProyecto(id);
        
        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Proyecto eliminado")
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
}