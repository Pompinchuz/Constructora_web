
package com.constructora.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuración para tareas asíncronas (envío de emails)
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Número de hilos principales
        executor.setCorePoolSize(2);
        
        // Número máximo de hilos
        executor.setMaxPoolSize(5);
        
        // Capacidad de la cola
        executor.setQueueCapacity(100);
        
        // Prefijo para los nombres de hilos
        executor.setThreadNamePrefix("Async-");
        
        executor.initialize();
        return executor;
    }
}