package com.constructora.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private String path;
    private Integer status;
}