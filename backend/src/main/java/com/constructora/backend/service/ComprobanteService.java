package com.constructora.backend.service;

import com.constructora.backend.controller.dto.comprobante.ComprobanteResponseDTO;
import com.constructora.backend.controller.dto.comprobante.SubirComprobanteDTO;
import com.constructora.backend.exception.ForbiddenException;
import com.constructora.backend.exception.NotFoundException;

import com.constructora.backend.repository.ComprobantePagoRepository;
import com.constructora.backend.repository.ProformaRepository;
import com.constructora.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.constructora.backend.entity.ComprobantePago;
import com.constructora.backend.entity.Proforma;
import com.constructora.backend.entity.Cliente;
import com.constructora.backend.entity.enums.EstadoComprobante;

@Service
@RequiredArgsConstructor
public class ComprobanteService {
    
    private final ComprobantePagoRepository comprobanteRepository;
    private final ProformaRepository proformaRepository;
    private final ClienteRepository clienteRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    
    @Transactional
    public ComprobanteResponseDTO subirComprobante(Long clienteId, SubirComprobanteDTO dto) {
        
        Proforma proforma = proformaRepository.findById(dto.getProformaId())
                .orElseThrow(() -> new NotFoundException("Proforma no encontrada"));
        
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        
        if (!proforma.getCliente().getId().equals(clienteId)) {
            throw new ForbiddenException("No tiene permiso para esta proforma");
        }
        
        // Guardar archivo
        String rutaArchivo = fileStorageService.guardarArchivo(
                dto.getArchivo(), "comprobantes");
        
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setProforma(proforma);
        comprobante.setCliente(cliente);
        comprobante.setMonto(dto.getMonto());
        comprobante.setNumeroOperacion(dto.getNumeroOperacion());
        comprobante.setEntidadBancaria(dto.getEntidadBancaria());
        comprobante.setArchivoComprobante(rutaArchivo);
        comprobante.setEstado(EstadoComprobante.PENDIENTE);
        
        comprobante = comprobanteRepository.save(comprobante);
        
        // Notificar a administradores
        emailService.notificarNuevoComprobante(comprobante);
        
        return mapearAResponse(comprobante);
    }
    
    private ComprobanteResponseDTO mapearAResponse(ComprobantePago comprobante) {
        return ComprobanteResponseDTO.builder()
                .id(comprobante.getId())
                .proformaId(comprobante.getProforma().getId())
                .codigoProforma(comprobante.getProforma().getCodigo())
                .monto(comprobante.getMonto())
                .numeroOperacion(comprobante.getNumeroOperacion())
                .entidadBancaria(comprobante.getEntidadBancaria())
                .archivoComprobante(comprobante.getArchivoComprobante())
                .estado(comprobante.getEstado())
                .observaciones(comprobante.getObservaciones())
                .fechaSubida(comprobante.getFechaSubida())
                .verificadoPor(comprobante.getVerificadoPor() != null ?
                        comprobante.getVerificadoPor().getNombreCompleto() : null)
                .fechaVerificacion(comprobante.getFechaVerificacion())
                .build();
    }
}