package com.constructora.backend.service;

import com.constructora.backend.entity.ComprobantePago;
import com.constructora.backend.entity.Proforma;
import com.constructora.backend.entity.SolicitudProforma;
import com.constructora.backend.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.name:Constructora}")
    private String appName;
    
    @Value("${app.url:http://localhost:4200}")
    private String appUrl;
    
    @Value("${admin.email:admin@constructora.com}")
    private String adminEmail;
    
    // ============================================
    // CORREOS PARA CLIENTES
    // ============================================
    
    /**
     * Envía correo de bienvenida al registrarse
     */
    @Async
    public void enviarCorreoBienvenida(String destinatario, String nombreCompleto) {
        try {
            Context context = new Context();
            context.setVariable("nombreCompleto", nombreCompleto);
            context.setVariable("appName", appName);
            context.setVariable("appUrl", appUrl);
            
            String htmlContent = templateEngine.process("email/bienvenida", context);
            
            enviarEmailHtml(
                destinatario,
                "Bienvenido a " + appName,
                htmlContent
            );
            
            log.info("Correo de bienvenida enviado a: {}", destinatario);
            
        } catch (Exception e) {
            log.error("Error al enviar correo de bienvenida a {}: {}", destinatario, e.getMessage());
            throw new EmailSendingException("Error al enviar correo de bienvenida", e);
        }
    }
    
    /**
     * Envía la proforma al cliente
     */
    @Async
    public void enviarProforma(Proforma proforma) {
        try {
            Context context = new Context();
            context.setVariable("proforma", proforma);
            context.setVariable("cliente", proforma.getCliente().getNombreCompleto());
            context.setVariable("codigo", proforma.getCodigo());
            context.setVariable("total", formatearMoneda(proforma.getTotal()));
            context.setVariable("vigencia", formatearFecha(proforma.getVigenciaHasta()));
            context.setVariable("appUrl", appUrl);
            
            String htmlContent = templateEngine.process("email/proforma-enviada", context);
            
            enviarEmailHtml(
                proforma.getCliente().getUsuario().getCorreoElectronico(),
                "Proforma " + proforma.getCodigo() + " - " + appName,
                htmlContent
            );
            
            log.info("Proforma {} enviada a: {}", 
                proforma.getCodigo(), 
                proforma.getCliente().getUsuario().getCorreoElectronico());
            
        } catch (Exception e) {
            log.error("Error al enviar proforma {}: {}", proforma.getCodigo(), e.getMessage());
            throw new EmailSendingException("Error al enviar proforma por correo", e);
        }
    }
    
    /**
     * Notifica al cliente sobre solicitud rechazada
     */
    @Async
    public void notificarSolicitudRechazada(SolicitudProforma solicitud) {
        try {
            Context context = new Context();
            context.setVariable("cliente", solicitud.getCliente().getNombreCompleto());
            context.setVariable("titulo", solicitud.getTitulo());
            context.setVariable("motivo", solicitud.getMotivoRechazo());
            context.setVariable("appUrl", appUrl);
            
            String htmlContent = templateEngine.process("email/solicitud-rechazada", context);
            
            enviarEmailHtml(
                solicitud.getCliente().getUsuario().getCorreoElectronico(),
                "Solicitud Rechazada - " + appName,
                htmlContent
            );
            
            log.info("Notificación de rechazo enviada para solicitud ID: {}", solicitud.getId());
            
        } catch (Exception e) {
            log.error("Error al enviar notificación de rechazo: {}", e.getMessage());
        }
    }
    
    // ============================================
    // CORREOS PARA ADMINISTRADORES
    // ============================================
    
    /**
     * Notifica a los administradores sobre nueva solicitud
     */
    @Async
    public void notificarNuevaSolicitud(SolicitudProforma solicitud) {
        try {
            Context context = new Context();
            context.setVariable("cliente", solicitud.getCliente().getNombreCompleto());
            context.setVariable("titulo", solicitud.getTitulo());
            context.setVariable("descripcion", solicitud.getDescripcion());
            context.setVariable("fecha", formatearFechaHora(solicitud.getFechaSolicitud()));
            context.setVariable("appUrl", appUrl);
            context.setVariable("solicitudId", solicitud.getId());
            
            String htmlContent = templateEngine.process("email/nueva-solicitud", context);
            
            enviarEmailHtml(
                adminEmail,
                "Nueva Solicitud de Proforma - " + appName,
                htmlContent
            );
            
            log.info("Notificación de nueva solicitud enviada a administradores");
            
        } catch (Exception e) {
            log.error("Error al notificar nueva solicitud: {}", e.getMessage());
        }
    }
    
    /**
     * Notifica a los administradores sobre nuevo comprobante
     */
    @Async
    public void notificarNuevoComprobante(ComprobantePago comprobante) {
        try {
            Context context = new Context();
            context.setVariable("cliente", comprobante.getCliente().getNombreCompleto());
            context.setVariable("codigoProforma", comprobante.getProforma().getCodigo());
            context.setVariable("monto", formatearMoneda(comprobante.getMonto()));
            context.setVariable("numeroOperacion", comprobante.getNumeroOperacion());
            context.setVariable("banco", comprobante.getEntidadBancaria());
            context.setVariable("appUrl", appUrl);
            
            String htmlContent = templateEngine.process("email/nuevo-comprobante", context);
            
            enviarEmailHtml(
                adminEmail,
                "Nuevo Comprobante de Pago - " + appName,
                htmlContent
            );
            
            log.info("Notificación de nuevo comprobante enviada a administradores");
            
        } catch (Exception e) {
            log.error("Error al notificar nuevo comprobante: {}", e.getMessage());
        }
    }
    
    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================
    
    /**
     * Envía un email HTML
     */
    private void enviarEmailHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new EmailSendingException("Error al enviar correo electrónico", e);
        }
    }
    
    /**
     * Envía un email simple (texto plano)
     */
    public void enviarEmailSimple(String destinatario, String asunto, String mensaje) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(destinatario);
            mailMessage.setSubject(asunto);
            mailMessage.setText(mensaje);
            
            mailSender.send(mailMessage);
            
            log.info("Email simple enviado a: {}", destinatario);
            
        } catch (Exception e) {
            log.error("Error al enviar email simple: {}", e.getMessage());
            throw new EmailSendingException("Error al enviar correo", e);
        }
    }
    
    /**
     * Envía un email con archivo adjunto
     */
    public void enviarEmailConArchivo(String destinatario, String asunto, 
                                      String mensaje, String rutaArchivo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(mensaje, true);
            
            // Adjuntar archivo
            FileSystemResource file = new FileSystemResource(new File(rutaArchivo));
            helper.addAttachment(file.getFilename(), file);
            
            mailSender.send(message);
            
            log.info("Email con archivo adjunto enviado a: {}", destinatario);
            
        } catch (MessagingException e) {
            throw new EmailSendingException("Error al enviar correo con archivo", e);
        }
    }
    
    // Métodos de formateo
    private String formatearMoneda(BigDecimal monto) {
        return String.format("S/ %.2f", monto);
    }
    
    private String formatearFecha(java.time.LocalDate fecha) {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    private String formatearFechaHora(java.time.LocalDateTime fechaHora) {
        return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}