// ============================================
// CONTENIDO SERVICE
// ============================================

// ContenidoService.java
package com.constructora.backend.service;

import com.constructora.backend.controller.dto.ImagenResponseDTO;
import com.constructora.backend.controller.dto.ProyectoExitosoDTO;
import com.constructora.backend.controller.dto.ProyectoExitosoResponseDTO;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.Imagen;
import com.constructora.backend.entity.ImagenProyecto;
import com.constructora.backend.entity.ProyectoExitoso;
import com.constructora.backend.entity.enums.EstadoAprobacionProyecto;
import com.constructora.backend.entity.enums.TipoImagen;
import com.constructora.backend.exception.BadRequestException;
import com.constructora.backend.exception.NotFoundException;
import com.constructora.backend.repository.ClienteRepository;
import com.constructora.backend.repository.ImagenProyectoRepository;
import com.constructora.backend.repository.ImagenRepository;
import com.constructora.backend.repository.ProyectoExitosoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContenidoService {

    private final ImagenRepository imagenRepository;
    private final ProyectoExitosoRepository proyectoRepository;
    private final ImagenProyectoRepository imagenProyectoRepository;
    private final FileStorageService fileStorageService;
    private final ClienteRepository clienteRepository;
    
    // ============================================
    // GESTIÓN DE IMÁGENES
    // ============================================
    
    @Transactional
    public ImagenResponseDTO subirImagen(TipoImagen tipo, String titulo, String descripcion, 
                                         MultipartFile archivo, Integer orden) {
        
        if (archivo == null || archivo.isEmpty()) {
            throw new BadRequestException("El archivo es obligatorio");
        }
        
        // Guardar archivo
        String rutaArchivo = fileStorageService.guardarArchivo(archivo, "imagenes");
        
        Imagen imagen = new Imagen();
        imagen.setTipo(tipo);
        imagen.setTitulo(titulo);
        imagen.setDescripcion(descripcion);
        imagen.setUrlImagen(rutaArchivo);
        imagen.setOrden(orden != null ? orden : 0);
        imagen.setActivo(true);
        
        imagen = imagenRepository.save(imagen);
        
        log.info("Imagen creada con ID: {}", imagen.getId());
        
        return mapearImagenAResponse(imagen);
    }
    
    @Transactional(readOnly = true)
    public List<ImagenResponseDTO> obtenerImagenesActivasPorTipo(TipoImagen tipo) {
        return imagenRepository.findByTipoAndActivoTrueOrderByOrdenAsc(tipo)
                .stream()
                .map(this::mapearImagenAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ImagenResponseDTO> obtenerImagenesPorTipo(TipoImagen tipo) {
        return imagenRepository.findByTipoOrderByOrdenAsc(tipo)
                .stream()
                .map(this::mapearImagenAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ImagenResponseDTO> obtenerTodasImagenesActivas() {
        return imagenRepository.findByActivoTrueOrderByOrdenAsc()
                .stream()
                .map(this::mapearImagenAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ImagenResponseDTO> obtenerTodasImagenes() {
        return imagenRepository.findAll()
                .stream()
                .map(this::mapearImagenAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ImagenResponseDTO actualizarImagen(Long id, String titulo, String descripcion, 
                                               Integer orden, Boolean activo) {
        
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Imagen no encontrada"));
        
        if (titulo != null) {
            imagen.setTitulo(titulo);
        }
        if (descripcion != null) {
            imagen.setDescripcion(descripcion);
        }
        if (orden != null) {
            imagen.setOrden(orden);
        }
        if (activo != null) {
            imagen.setActivo(activo);
        }
        
        imagen = imagenRepository.save(imagen);
        
        log.info("Imagen {} actualizada", id);
        
        return mapearImagenAResponse(imagen);
    }
    
    @Transactional
    public void eliminarImagen(Long id) {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Imagen no encontrada"));
        
        // Eliminar archivo físico
        fileStorageService.eliminarArchivo(imagen.getUrlImagen());
        
        // Eliminar registro
        imagenRepository.delete(imagen);
        
        log.info("Imagen {} eliminada", id);
    }
    
    // ============================================
    // GESTIÓN DE PROYECTOS
    // ============================================
    
    @Transactional
    public ProyectoExitosoResponseDTO crearProyecto(ProyectoExitosoDTO dto) {

        // Verificar que el cliente existe
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        ProyectoExitoso proyecto = new ProyectoExitoso();
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setUbicacion(dto.getUbicacion());
        proyecto.setFechaInicio(dto.getFechaInicio());
        proyecto.setFechaFinalizacion(dto.getFechaFinalizacion());

        // Establecer estado de aprobación
        proyecto.setCliente(cliente);
        proyecto.setEstadoAprobacion(EstadoAprobacionProyecto.PENDIENTE_APROBACION);
        proyecto.setFechaSolicitudAprobacion(LocalDateTime.now());
        proyecto.setActivo(false);  // Inactivo hasta que el cliente apruebe

        // Guardar imagen principal si existe
        if (dto.getImagenPrincipal() != null && !dto.getImagenPrincipal().isEmpty()) {
            String rutaImagenPrincipal = fileStorageService.guardarArchivo(
                    dto.getImagenPrincipal(), "proyectos");
            proyecto.setImagenPrincipal(rutaImagenPrincipal);
        }

        proyecto = proyectoRepository.save(proyecto);
        
        // Guardar imágenes adicionales
        List<String> rutasImagenes = new ArrayList<>();
        if (dto.getImagenesAdicionales() != null && !dto.getImagenesAdicionales().isEmpty()) {
            for (int i = 0; i < dto.getImagenesAdicionales().size(); i++) {
                MultipartFile archivo = dto.getImagenesAdicionales().get(i);
                if (!archivo.isEmpty()) {
                    String rutaImagen = fileStorageService.guardarArchivo(archivo, "proyectos");
                    
                    ImagenProyecto imagenProyecto = new ImagenProyecto();
                    imagenProyecto.setProyecto(proyecto);
                    imagenProyecto.setUrlImagen(rutaImagen);
                    imagenProyecto.setOrden(i);
                    
                    imagenProyectoRepository.save(imagenProyecto);
                    rutasImagenes.add(rutaImagen);
                }
            }
        }
        
        log.info("Proyecto creado con ID: {}", proyecto.getId());
        
        return mapearProyectoAResponse(proyecto, rutasImagenes);
    }
    
    @Transactional(readOnly = true)
    public ProyectoExitosoResponseDTO obtenerProyectoPorId(Long id) {
        ProyectoExitoso proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));
        
        List<String> imagenes = imagenProyectoRepository.findByProyectoIdOrderByOrdenAsc(id)
                .stream()
                .map(ImagenProyecto::getUrlImagen)
                .collect(Collectors.toList());
        
        return mapearProyectoAResponse(proyecto, imagenes);
    }
    
    @Transactional(readOnly = true)
    public List<ProyectoExitosoResponseDTO> obtenerProyectosActivos() {
        return proyectoRepository.findByActivoTrueOrderByFechaCreacionDesc()
                .stream()
                .map(proyecto -> {
                    List<String> imagenes = imagenProyectoRepository
                            .findByProyectoIdOrderByOrdenAsc(proyecto.getId())
                            .stream()
                            .map(ImagenProyecto::getUrlImagen)
                            .collect(Collectors.toList());
                    return mapearProyectoAResponse(proyecto, imagenes);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProyectoExitosoResponseDTO> obtenerTodosProyectos() {
        return proyectoRepository.findAllByOrderByFechaCreacionDesc()
                .stream()
                .map(proyecto -> {
                    List<String> imagenes = imagenProyectoRepository
                            .findByProyectoIdOrderByOrdenAsc(proyecto.getId())
                            .stream()
                            .map(ImagenProyecto::getUrlImagen)
                            .collect(Collectors.toList());
                    return mapearProyectoAResponse(proyecto, imagenes);
                })
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ProyectoExitosoResponseDTO actualizarProyecto(Long id, ProyectoExitosoDTO dto) {
        ProyectoExitoso proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));
        
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setUbicacion(dto.getUbicacion());
        proyecto.setFechaInicio(dto.getFechaInicio());
        proyecto.setFechaFinalizacion(dto.getFechaFinalizacion());
        
        // Actualizar imagen principal si se proporciona una nueva
        if (dto.getImagenPrincipal() != null && !dto.getImagenPrincipal().isEmpty()) {
            // Eliminar imagen anterior si existe
            if (proyecto.getImagenPrincipal() != null) {
                fileStorageService.eliminarArchivo(proyecto.getImagenPrincipal());
            }
            
            String rutaImagenPrincipal = fileStorageService.guardarArchivo(
                    dto.getImagenPrincipal(), "proyectos");
            proyecto.setImagenPrincipal(rutaImagenPrincipal);
        }
        
        proyecto = proyectoRepository.save(proyecto);
        
        List<String> imagenes = imagenProyectoRepository
                .findByProyectoIdOrderByOrdenAsc(id)
                .stream()
                .map(ImagenProyecto::getUrlImagen)
                .collect(Collectors.toList());
        
        log.info("Proyecto {} actualizado", id);
        
        return mapearProyectoAResponse(proyecto, imagenes);
    }
    
    @Transactional
    public void cambiarEstadoProyecto(Long id, Boolean activo) {
        ProyectoExitoso proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));
        
        proyecto.setActivo(activo);
        proyectoRepository.save(proyecto);
        
        log.info("Estado de proyecto {} cambiado a {}", id, activo);
    }
    
    @Transactional
    public void eliminarProyecto(Long id) {
        ProyectoExitoso proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));
        
        // Eliminar imagen principal
        if (proyecto.getImagenPrincipal() != null) {
            fileStorageService.eliminarArchivo(proyecto.getImagenPrincipal());
        }
        
        // Eliminar imágenes adicionales
        List<ImagenProyecto> imagenes = imagenProyectoRepository.findByProyectoIdOrderByOrdenAsc(id);
        for (ImagenProyecto imagen : imagenes) {
            fileStorageService.eliminarArchivo(imagen.getUrlImagen());
        }
        imagenProyectoRepository.deleteByProyectoId(id);
        
        // Eliminar proyecto
        proyectoRepository.delete(proyecto);
        
        log.info("Proyecto {} eliminado", id);
    }
    
    // ============================================
    // GESTIÓN DE APROBACIONES DE PROYECTOS
    // ============================================

    /**
     * Obtener proyectos pendientes de aprobación para un cliente específico
     */
    @Transactional(readOnly = true)
    public List<ProyectoExitosoResponseDTO> obtenerProyectosPendientesCliente(Long clienteId) {
        return proyectoRepository.findByClienteIdAndEstadoAprobacionOrderByFechaCreacionDesc(
                        clienteId, EstadoAprobacionProyecto.PENDIENTE_APROBACION)
                .stream()
                .map(proyecto -> {
                    List<String> imagenes = imagenProyectoRepository
                            .findByProyectoIdOrderByOrdenAsc(proyecto.getId())
                            .stream()
                            .map(ImagenProyecto::getUrlImagen)
                            .collect(Collectors.toList());
                    return mapearProyectoAResponse(proyecto, imagenes);
                })
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los proyectos de un cliente (sin importar el estado)
     */
    @Transactional(readOnly = true)
    public List<ProyectoExitosoResponseDTO> obtenerProyectosCliente(Long clienteId) {
        return proyectoRepository.findByClienteIdOrderByFechaCreacionDesc(clienteId)
                .stream()
                .map(proyecto -> {
                    List<String> imagenes = imagenProyectoRepository
                            .findByProyectoIdOrderByOrdenAsc(proyecto.getId())
                            .stream()
                            .map(ImagenProyecto::getUrlImagen)
                            .collect(Collectors.toList());
                    return mapearProyectoAResponse(proyecto, imagenes);
                })
                .collect(Collectors.toList());
    }

    /**
     * Aprobar la publicación de un proyecto (solo puede hacerlo el cliente dueño)
     */
    @Transactional
    public ProyectoExitosoResponseDTO aprobarProyecto(Long proyectoId, Long clienteId) {
        ProyectoExitoso proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));

        // Verificar que el proyecto pertenece al cliente
        if (proyecto.getCliente() == null || !proyecto.getCliente().getId().equals(clienteId)) {
            throw new BadRequestException("No tienes permiso para aprobar este proyecto");
        }

        // Verificar que está en estado pendiente
        if (proyecto.getEstadoAprobacion() != EstadoAprobacionProyecto.PENDIENTE_APROBACION) {
            throw new BadRequestException("El proyecto no está pendiente de aprobación");
        }

        // Aprobar el proyecto
        proyecto.setEstadoAprobacion(EstadoAprobacionProyecto.APROBADO);
        proyecto.setFechaRespuestaCliente(LocalDateTime.now());
        proyecto.setActivo(true);  // Hacer el proyecto público
        proyecto.setMotivoRechazo(null);

        proyecto = proyectoRepository.save(proyecto);

        List<String> imagenes = imagenProyectoRepository
                .findByProyectoIdOrderByOrdenAsc(proyectoId)
                .stream()
                .map(ImagenProyecto::getUrlImagen)
                .collect(Collectors.toList());

        log.info("Proyecto {} aprobado por cliente {}", proyectoId, clienteId);

        return mapearProyectoAResponse(proyecto, imagenes);
    }

    /**
     * Rechazar la publicación de un proyecto (solo puede hacerlo el cliente dueño)
     */
    @Transactional
    public ProyectoExitosoResponseDTO rechazarProyecto(Long proyectoId, Long clienteId, String motivoRechazo) {
        ProyectoExitoso proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));

        // Verificar que el proyecto pertenece al cliente
        if (proyecto.getCliente() == null || !proyecto.getCliente().getId().equals(clienteId)) {
            throw new BadRequestException("No tienes permiso para rechazar este proyecto");
        }

        // Verificar que está en estado pendiente
        if (proyecto.getEstadoAprobacion() != EstadoAprobacionProyecto.PENDIENTE_APROBACION) {
            throw new BadRequestException("El proyecto no está pendiente de aprobación");
        }

        if (motivoRechazo == null || motivoRechazo.trim().isEmpty()) {
            throw new BadRequestException("El motivo de rechazo es obligatorio");
        }

        // Rechazar el proyecto
        proyecto.setEstadoAprobacion(EstadoAprobacionProyecto.RECHAZADO);
        proyecto.setFechaRespuestaCliente(LocalDateTime.now());
        proyecto.setActivo(false);  // Mantener el proyecto inactivo
        proyecto.setMotivoRechazo(motivoRechazo);

        proyecto = proyectoRepository.save(proyecto);

        List<String> imagenes = imagenProyectoRepository
                .findByProyectoIdOrderByOrdenAsc(proyectoId)
                .stream()
                .map(ImagenProyecto::getUrlImagen)
                .collect(Collectors.toList());

        log.info("Proyecto {} rechazado por cliente {}", proyectoId, clienteId);

        return mapearProyectoAResponse(proyecto, imagenes);
    }

    // ============================================
    // MÉTODOS DE MAPEO
    // ============================================

    private ImagenResponseDTO mapearImagenAResponse(Imagen imagen) {
        // Construir URL completa para acceder a la imagen
        String urlCompleta = "/uploads/" + imagen.getUrlImagen();

        return ImagenResponseDTO.builder()
                .id(imagen.getId())
                .tipo(imagen.getTipo())
                .titulo(imagen.getTitulo())
                .descripcion(imagen.getDescripcion())
                .urlImagen(urlCompleta)
                .orden(imagen.getOrden())
                .activo(imagen.getActivo())
                .fechaSubida(imagen.getFechaSubida())
                .build();
    }
    
    private ProyectoExitosoResponseDTO mapearProyectoAResponse(ProyectoExitoso proyecto,
                                                                List<String> imagenes) {
        // Construir URL completa para imagen principal
        String imagenPrincipalUrl = proyecto.getImagenPrincipal() != null
            ? "/uploads/" + proyecto.getImagenPrincipal()
            : null;

        // Construir URLs completas para imágenes adicionales
        List<String> imagenesUrls = imagenes.stream()
            .map(img -> "/uploads/" + img)
            .collect(Collectors.toList());

        return ProyectoExitosoResponseDTO.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .descripcion(proyecto.getDescripcion())
                .ubicacion(proyecto.getUbicacion())
                .fechaInicio(proyecto.getFechaInicio())
                .fechaFinalizacion(proyecto.getFechaFinalizacion())
                .imagenPrincipal(imagenPrincipalUrl)
                .imagenes(imagenesUrls)
                .activo(proyecto.getActivo())
                .clienteId(proyecto.getCliente() != null ? proyecto.getCliente().getId() : null)
                .clienteNombre(proyecto.getCliente() != null ? proyecto.getCliente().getNombreCompleto() : null)
                .estadoAprobacion(proyecto.getEstadoAprobacion())
                .motivoRechazo(proyecto.getMotivoRechazo())
                .fechaSolicitudAprobacion(proyecto.getFechaSolicitudAprobacion())
                .fechaRespuestaCliente(proyecto.getFechaRespuestaCliente())
                .build();
    }
}