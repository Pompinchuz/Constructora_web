package com.constructora.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración adicional de Spring MVC
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    /**
     * Configurar el manejo de recursos estáticos (archivos subidos)
     * Permite acceder a los archivos mediante URLs como: /uploads/archivo.jpg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer la carpeta de uploads como recurso estático
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}