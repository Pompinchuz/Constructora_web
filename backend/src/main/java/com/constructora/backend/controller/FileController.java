// ============================================
// FILE CONTROLLER (Descarga de Archivos)
// ============================================

// FileController.java
package com.constructora.backend.controller;

import com.constructora.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class FileController {
    
    private final FileStorageService fileStorageService;
    
    /**
     * Descargar archivo
     * GET /api/files/download/{folder}/{filename}
     */
    @GetMapping("/download/{folder}/{filename:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> descargarArchivo(
            @PathVariable String folder,
            @PathVariable String filename) {
        
        String rutaArchivo = folder + "/" + filename;
        
        log.info("Descargando archivo: {}", rutaArchivo);
        
        Resource resource = fileStorageService.cargarArchivoComoResource(rutaArchivo);
        String contentType = fileStorageService.obtenerTipoContenido(rutaArchivo);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    /**
     * Ver archivo en línea (sin descargar)
     * GET /api/files/view/{folder}/{filename}
     */
    @GetMapping("/view/{folder}/{filename:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> verArchivo(
            @PathVariable String folder,
            @PathVariable String filename) {
        
        String rutaArchivo = folder + "/" + filename;
        
        log.info("Viendo archivo: {}", rutaArchivo);
        
        Resource resource = fileStorageService.cargarArchivoComoResource(rutaArchivo);
        String contentType = fileStorageService.obtenerTipoContenido(rutaArchivo);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    /**
     * Obtener imagen pública
     * GET /api/files/public/imagenes/{filename}
     */
    @GetMapping("/public/imagenes/{filename:.+}")
    public ResponseEntity<Resource> obtenerImagenPublica(@PathVariable String filename) {
        
        String rutaArchivo = "imagenes/" + filename;
        
        log.info("Obteniendo imagen pública: {}", rutaArchivo);
        
        Resource resource = fileStorageService.cargarArchivoComoResource(rutaArchivo);
        String contentType = fileStorageService.obtenerTipoContenido(rutaArchivo);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}