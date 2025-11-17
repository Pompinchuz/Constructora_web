// ============================================
// FILE CONTROLLER - Para servir archivos subidos
// ============================================

package com.constructora.backend.controller;

import com.constructora.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class FileController {
    
    private final FileStorageService fileStorageService;
    
    /**
     * Servir archivos desde el directorio uploads
     * GET /uploads/{tipo}/{nombreArchivo}
     */
    @GetMapping("/{tipo}/{nombreArchivo:.+}")
    public ResponseEntity<Resource> servirArchivo(
            @PathVariable String tipo,
            @PathVariable String nombreArchivo) {
        
        String rutaArchivo = tipo + "/" + nombreArchivo;
        log.debug("Sirviendo archivo: {}", rutaArchivo);
        
        try {
            Resource resource = fileStorageService.cargarArchivoComoResource(rutaArchivo);
            
            // Determinar el tipo de contenido
            String contentType = fileStorageService.obtenerTipoContenido(rutaArchivo);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("Error sirviendo archivo {}: {}", rutaArchivo, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Servir archivos sin subdirectorio
     * GET /uploads/{nombreArchivo}
     */
    @GetMapping("/{nombreArchivo:.+}")
    public ResponseEntity<Resource> servirArchivoSimple(@PathVariable String nombreArchivo) {
        
        log.debug("Sirviendo archivo simple: {}", nombreArchivo);
        
        try {
            Resource resource = fileStorageService.cargarArchivoComoResource(nombreArchivo);
            
            String contentType = fileStorageService.obtenerTipoContenido(nombreArchivo);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("Error sirviendo archivo {}: {}", nombreArchivo, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}