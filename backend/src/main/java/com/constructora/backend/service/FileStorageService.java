package com.constructora.backend.service;

import com.constructora.backend.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileStorageService {
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    private Path fileStorageLocation;
    
    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException(
                "No se pudo crear el directorio de almacenamiento", ex);
        }
    }
    
    /**
     * Guarda un archivo en el sistema de archivos
     * @param file Archivo a guardar
     * @param subDir Subdirectorio (ej: "solicitudes", "comprobantes", "imagenes")
     * @return Ruta relativa del archivo guardado
     */
    public String guardarArchivo(MultipartFile file, String subDir) {
        // Validar archivo
        validarArchivo(file);
        
        // Limpiar nombre del archivo
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Generar nombre único
        String nombreArchivo = generarNombreUnico(originalFilename);
        
        try {
            // Verificar caracteres inválidos
            if (nombreArchivo.contains("..")) {
                throw new FileStorageException(
                    "El nombre del archivo contiene una secuencia de ruta inválida: " + nombreArchivo);
            }
            
            // Crear subdirectorio si no existe
            Path targetLocation = this.fileStorageLocation.resolve(subDir);
            Files.createDirectories(targetLocation);
            
            // Guardar archivo
            Path destinoArchivo = targetLocation.resolve(nombreArchivo);
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinoArchivo, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Retornar ruta relativa
            return subDir + "/" + nombreArchivo;
            
        } catch (IOException ex) {
            throw new FileStorageException(
                "No se pudo almacenar el archivo " + nombreArchivo, ex);
        }
    }
    
    /**
     * Carga un archivo como Resource
     * @param rutaArchivo Ruta relativa del archivo
     * @return Resource del archivo
     */
    public Resource cargarArchivoComoResource(String rutaArchivo) {
        try {
            Path filePath = this.fileStorageLocation.resolve(rutaArchivo).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("Archivo no encontrado: " + rutaArchivo);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("Archivo no encontrado: " + rutaArchivo, ex);
        }
    }
    
    /**
     * Elimina un archivo del sistema
     * @param rutaArchivo Ruta relativa del archivo
     * @return true si se eliminó correctamente
     */
    public boolean eliminarArchivo(String rutaArchivo) {
        try {
            Path filePath = this.fileStorageLocation.resolve(rutaArchivo).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo eliminar el archivo: " + rutaArchivo, ex);
        }
    }
    
    /**
     * Obtiene la ruta completa de un archivo
     * @param rutaArchivo Ruta relativa
     * @return Path completo
     */
    public Path obtenerRutaCompleta(String rutaArchivo) {
        return this.fileStorageLocation.resolve(rutaArchivo).normalize();
    }
    
    /**
     * Valida que el archivo sea válido
     * @param file Archivo a validar
     */
    private void validarArchivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("No se puede almacenar un archivo vacío");
        }
        
        // Validar tamaño (10MB máximo)
        long maxSize = 10 * 1024 * 1024; // 10MB en bytes
        if (file.getSize() > maxSize) {
            throw new FileStorageException(
                "El archivo es demasiado grande. Tamaño máximo: 10MB");
        }
        
        // Validar tipo de archivo (opcional - puedes personalizar)
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new FileStorageException("Tipo de archivo no válido");
        }
    }
    
    /**
     * Genera un nombre único para el archivo
     * @param originalFilename Nombre original
     * @return Nombre único con timestamp y UUID
     */
    private String generarNombreUnico(String originalFilename) {
        // Obtener extensión
        String extension = "";
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalFilename.substring(lastDot);
        }
        
        // Generar nombre único: timestamp_uuid.ext
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return timestamp + "_" + uuid + extension;
    }
    
    /**
     * Obtiene el tipo MIME de un archivo
     * @param rutaArchivo Ruta del archivo
     * @return Tipo MIME
     */
    public String obtenerTipoContenido(String rutaArchivo) {
        try {
            Path filePath = this.fileStorageLocation.resolve(rutaArchivo).normalize();
            return Files.probeContentType(filePath);
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }
}