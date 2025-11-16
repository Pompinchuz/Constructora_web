package com.constructora.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.constructora.backend.controller.dto.CrearProformaDTO;
import com.constructora.backend.controller.dto.GastoProformaDTO;
import com.constructora.backend.controller.dto.GastoProformaResponseDTO;
import com.constructora.backend.controller.dto.ProformaEstadisticasDTO;
import com.constructora.backend.controller.dto.ProformaResponseDTO;
import com.constructora.backend.entity.Administrador;
import com.constructora.backend.entity.Proforma;

import com.constructora.backend.entity.SolicitudProforma;
import com.constructora.backend.entity.GastoProforma;
import com.constructora.backend.entity.enums.EstadoProforma;
import com.constructora.backend.entity.enums.EstadoSolicitud;
import com.constructora.backend.exception.BadRequestException;
import com.constructora.backend.exception.ConflictException;
import com.constructora.backend.exception.NotFoundException;
import com.constructora.backend.repository.AdministradorRepository;
import com.constructora.backend.repository.GastoProformaRepository;
import com.constructora.backend.repository.ProformaRepository;
import com.constructora.backend.repository.SolicitudProformaRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProformaService {
    
    private final ProformaRepository proformaRepository;
    private final SolicitudProformaRepository solicitudRepository;
    private final GastoProformaRepository gastoRepository;
    private final AdministradorRepository administradorRepository;
    private final EmailService emailService;
    
    @Transactional
    public ProformaResponseDTO crearProforma(CrearProformaDTO dto, Long adminId) {
        
        SolicitudProforma solicitud = solicitudRepository.findById(dto.getSolicitudId())
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        
        if (solicitud.getEstado() != EstadoSolicitud.APROBADA) {
            throw new BadRequestException("La solicitud debe estar aprobada");
        }
        
        if (solicitud.getProforma() != null) {
            throw new ConflictException("Ya existe una proforma para esta solicitud");
        }
        
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
        
        // Crear proforma
        Proforma proforma = new Proforma();
        proforma.setSolicitud(solicitud);
        proforma.setCodigo(generarCodigoProforma());
        proforma.setCliente(solicitud.getCliente());
        proforma.setVigenciaHasta(dto.getVigenciaHasta());
        proforma.setObservaciones(dto.getObservaciones());
        proforma.setCreadoPor(admin);
        proforma.setEstado(EstadoProforma.ENVIADA);
        
        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        List<GastoProforma> gastos = new ArrayList<>();
        
        for (GastoProformaDTO gastoDTO : dto.getGastos()) {
            GastoProforma gasto = new GastoProforma();
            gasto.setProforma(proforma);
            gasto.setConcepto(gastoDTO.getConcepto());
            gasto.setDescripcion(gastoDTO.getDescripcion());
            gasto.setCantidad(gastoDTO.getCantidad());
            gasto.setUnidad(gastoDTO.getUnidad());
            gasto.setPrecioUnitario(gastoDTO.getPrecioUnitario());
            
            BigDecimal subtotalGasto = gastoDTO.getCantidad()
                    .multiply(gastoDTO.getPrecioUnitario());
            gasto.setSubtotal(subtotalGasto);
            gasto.setOrden(gastoDTO.getOrden());
            
            subtotal = subtotal.add(subtotalGasto);
            gastos.add(gasto);
        }
        
        // Calcular IGV (18%)
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(igv);
        
        proforma.setSubtotal(subtotal);
        proforma.setIgv(igv);
        proforma.setTotal(total);
        
        proforma = proformaRepository.save(proforma);
        
        // Guardar gastos
        for (GastoProforma gasto : gastos) {
            gasto.setProforma(proforma);
        }
        gastoRepository.saveAll(gastos);
        proforma.setGastos(gastos);
        
        return mapearAResponse(proforma);
    }
    
    @Transactional
    public void enviarProforma(Long proformaId) {
        Proforma proforma = proformaRepository.findById(proformaId)
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        
        if (proforma.getFechaEnvio() != null) {
            throw new BadRequestException("La proforma ya fue enviada");
        }
        
        proforma.setFechaEnvio(LocalDateTime.now());
        proformaRepository.save(proforma);
        
        // Enviar por correo
        emailService.enviarProforma(proforma);
    }
    
    @Transactional(readOnly = true)
    public ProformaResponseDTO obtenerProformaPorId(Long id) {
        Proforma proforma = proformaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        return mapearAResponse(proforma);
    }
    
    @Transactional(readOnly = true)
    public ProformaResponseDTO obtenerProformaPorCodigo(String codigo) {
        Proforma proforma = proformaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        return mapearAResponse(proforma);
    }
    
    @Transactional(readOnly = true)
    public List<ProformaResponseDTO> obtenerProformasPorCliente(Long clienteId) {
        return proformaRepository.findByClienteIdOrderByFechaCreacionDesc(clienteId)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProformaResponseDTO> obtenerTodasProformas(EstadoProforma estado) {
        List<Proforma> proformas;
        
        if (estado != null) {
            proformas = proformaRepository.findByEstadoOrderByFechaCreacionDesc(estado);
        } else {
            proformas = proformaRepository.findAll();
        }
        
        return proformas.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ProformaResponseDTO actualizarEstado(Long id, EstadoProforma estado) {
        Proforma proforma = proformaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        
        proforma.setEstado(estado);
        proforma = proformaRepository.save(proforma);
        
        return mapearAResponse(proforma);
    }
    
    @Transactional
    public void eliminarProforma(Long id) {
        Proforma proforma = proformaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        
        if (proforma.getFechaEnvio() != null) {
            throw new BadRequestException("No se puede eliminar una proforma ya enviada");
        }
        
        gastoRepository.deleteByProformaId(id);
        proformaRepository.delete(proforma);
    }
    
    @Transactional(readOnly = true)
    public ProformaEstadisticasDTO obtenerEstadisticas() {
        Long totalProformas = proformaRepository.count();
        Long proformasEnviadas = proformaRepository.countByEstado(EstadoProforma.ENVIADA);
        Long proformasPagadas = proformaRepository.countByEstado(EstadoProforma.PAGADA);
        Long proformasRechazadas = proformaRepository.countByEstado(EstadoProforma.RECHAZADA);
        
        Double montoFacturado = proformaRepository.calcularTotalFacturado();
        
        return ProformaEstadisticasDTO.builder()
                .totalProformas(totalProformas)
                .proformasEnviadas(proformasEnviadas)
                .proformasPagadas(proformasPagadas)
                .proformasRechazadas(proformasRechazadas)
                .montoTotal(BigDecimal.ZERO)
                .montoFacturado(montoFacturado != null ? 
                        BigDecimal.valueOf(montoFacturado) : BigDecimal.ZERO)
                .build();
    }
    
    private String generarCodigoProforma() {
        String anio = String.valueOf(LocalDate.now().getYear());
        Long ultimoNumero = proformaRepository.contarProformasDelAnio(anio);
        String numero = String.format("%05d", ultimoNumero + 1);
        return "PRF-" + anio + "-" + numero;
    }
    
    private ProformaResponseDTO mapearAResponse(Proforma proforma) {
        List<GastoProformaResponseDTO> gastosDTO = proforma.getGastos().stream()
                .map(g -> GastoProformaResponseDTO.builder()
                        .id(g.getId())
                        .concepto(g.getConcepto())
                        .descripcion(g.getDescripcion())
                        .cantidad(g.getCantidad())
                        .unidad(g.getUnidad())
                        .precioUnitario(g.getPrecioUnitario())
                        .subtotal(g.getSubtotal())
                        .orden(g.getOrden())
                        .build())
                .collect(Collectors.toList());
        
        return ProformaResponseDTO.builder()
                .id(proforma.getId())
                .codigo(proforma.getCodigo())
                .clienteNombre(proforma.getCliente().getNombreCompleto())
                .clienteCorreo(proforma.getCliente().getUsuario().getCorreoElectronico())
                .subtotal(proforma.getSubtotal())
                .igv(proforma.getIgv())
                .total(proforma.getTotal())
                .vigenciaHasta(proforma.getVigenciaHasta())
                .observaciones(proforma.getObservaciones())
                .estado(proforma.getEstado())
                .fechaCreacion(proforma.getFechaCreacion())
                .fechaEnvio(proforma.getFechaEnvio())
                .creadoPor(proforma.getCreadoPor().getNombreCompleto())
                .gastos(gastosDTO)
                .build();
    }
}