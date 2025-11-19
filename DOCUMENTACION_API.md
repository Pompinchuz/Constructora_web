# 📚 Documentación API - Sistema de Proformas Constructora

> Documentación completa de la comunicación Backend (Spring Boot) - Frontend (Angular)
> Versión: 1.0.0
> Fecha: 2024-11-19

---

## 📑 Tabla de Contenidos

1. [Arquitectura General](#arquitectura-general)
2. [Endpoints Backend](#endpoints-backend)
3. [DTOs (Data Transfer Objects)](#dtos-data-transfer-objects)
4. [Servicios Angular](#servicios-angular)
5. [Autenticación JWT](#autenticación-jwt)
6. [Modelos e Interfaces](#modelos-e-interfaces)
7. [Ejemplos de Request/Response](#ejemplos-de-requestresponse)
8. [Manejo de Errores](#manejo-de-errores)
9. [Roles y Permisos](#roles-y-permisos)

---

## 🏗️ Arquitectura General

### Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| **Backend** | Spring Boot 3.5.5 (Java) |
| **Frontend** | Angular 19.2 |
| **Autenticación** | JWT (JSON Web Tokens) |
| **Base de Datos** | MySQL con JPA/Hibernate |
| **Almacenamiento** | sessionStorage (cliente) |

### URLs Base

```
Backend API:  http://localhost:8080
Frontend:     http://localhost:4200
Upload URL:   http://localhost:8080/uploads
```

---

## 🔌 Endpoints Backend

### 1. AuthController (`/api/auth`)

#### POST `/api/auth/login`
**Descripción:** Autenticación de usuario
**Acceso:** PÚBLICO
**Request Body:**
```json
{
  "correoElectronico": "usuario@example.com",
  "contrasena": "Password123"
}
```
**Response:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "tipoToken": "Bearer",
    "expiraEn": 86400000,
    "correoElectronico": "usuario@example.com",
    "tipoUsuario": "CLIENTE_NATURAL",
    "nombreCompleto": "Juan Pérez"
  }
}
```

#### POST `/api/auth/registro/persona-natural`
**Descripción:** Registro de cliente persona natural
**Acceso:** PÚBLICO
**Request Body:**
```json
{
  "correoElectronico": "cliente@example.com",
  "contrasena": "Password123",
  "nombres": "Juan",
  "apellidos": "Pérez García",
  "dni": "12345678",
  "fechaNacimiento": "1990-01-15",
  "telefono": "987654321",
  "direccion": "Av. Principal 123"
}
```

#### POST `/api/auth/registro/persona-juridica`
**Descripción:** Registro de cliente persona jurídica
**Acceso:** PÚBLICO
**Request Body:**
```json
{
  "correoElectronico": "empresa@example.com",
  "contrasena": "Password123",
  "razonSocial": "Constructora ABC S.A.C.",
  "ruc": "20123456789",
  "representanteLegal": "María López",
  "telefono": "987654321",
  "direccion": "Jr. Comercio 456"
}
```

#### GET `/api/auth/validate`
**Descripción:** Validar token JWT
**Acceso:** AUTENTICADO
**Headers:** `Authorization: Bearer {token}`

#### GET `/api/auth/check-email?email={email}`
**Descripción:** Verificar disponibilidad de email
**Acceso:** PÚBLICO

#### GET `/api/auth/check-dni?dni={dni}`
**Descripción:** Verificar disponibilidad de DNI
**Acceso:** PÚBLICO

#### GET `/api/auth/check-ruc?ruc={ruc}`
**Descripción:** Verificar disponibilidad de RUC
**Acceso:** PÚBLICO

---

### 2. SolicitudController (`/api/solicitudes`)

#### POST `/api/solicitudes`
**Descripción:** Crear solicitud de proforma
**Acceso:** CLIENTE
**Content-Type:** `multipart/form-data`
**Form Data:**
- `titulo`: string (obligatorio)
- `descripcion`: string (obligatorio)
- `archivo`: File (opcional)

**Response:**
```json
{
  "success": true,
  "message": "Solicitud creada exitosamente",
  "data": {
    "id": 1,
    "titulo": "Solicitud de Remodelación",
    "descripcion": "Necesito presupuesto para remodelación",
    "archivoAdjunto": "solicitudes/archivo_123.pdf",
    "estado": "PENDIENTE",
    "fechaSolicitud": "2024-11-19T10:30:00",
    "clienteNombre": "Juan Pérez"
  }
}
```

#### GET `/api/solicitudes/mis-solicitudes`
**Descripción:** Obtener solicitudes del cliente autenticado
**Acceso:** CLIENTE

#### GET `/api/solicitudes/{id}`
**Descripción:** Obtener solicitud por ID
**Acceso:** CLIENTE (propia) / ADMINISTRADOR (todas)
**Path Params:** `id` - Long

#### GET `/api/solicitudes/admin/todas?estado={estado}`
**Descripción:** Listar todas las solicitudes (con filtro opcional)
**Acceso:** ADMINISTRADOR
**Query Params:**
- `estado` (opcional): PENDIENTE | EN_REVISION | APROBADA | RECHAZADA

#### PATCH `/api/solicitudes/{id}/estado?estado={estado}&motivoRechazo={motivo}`
**Descripción:** Cambiar estado de solicitud
**Acceso:** ADMINISTRADOR
**Path Params:** `id` - Long
**Query Params:**
- `estado`: EstadoSolicitud (obligatorio)
- `motivoRechazo`: String (obligatorio si estado=RECHAZADA)

#### POST `/api/solicitudes/{id}/aprobar`
**Descripción:** Aprobar solicitud
**Acceso:** ADMINISTRADOR

#### POST `/api/solicitudes/{id}/rechazar?motivo={motivo}`
**Descripción:** Rechazar solicitud
**Acceso:** ADMINISTRADOR
**Query Params:** `motivo` - String (obligatorio)

#### GET `/api/solicitudes/admin/pendientes/count`
**Descripción:** Contar solicitudes pendientes
**Acceso:** ADMINISTRADOR

---

### 3. ProformaController (`/api/proformas`)

#### POST `/api/proformas`
**Descripción:** Crear proforma
**Acceso:** ADMINISTRADOR
**Request Body:**
```json
{
  "solicitudId": 1,
  "vigenciaHasta": "2024-12-19",
  "observaciones": "Validez: 30 días",
  "gastos": [
    {
      "concepto": "Materiales",
      "descripcion": "Cemento, arena, ladrillos",
      "cantidad": 50,
      "unidad": "KG",
      "precioUnitario": 10.00,
      "orden": 1
    }
  ]
}
```

**Response:**
```json
{
  "success": true,
  "message": "Proforma creada exitosamente",
  "data": {
    "id": 1,
    "codigo": "PRF-0001-2024",
    "clienteNombre": "Juan Pérez",
    "clienteCorreo": "cliente@example.com",
    "subtotal": 500.00,
    "igv": 90.00,
    "total": 590.00,
    "vigenciaHasta": "2024-12-19",
    "observaciones": "Validez: 30 días",
    "estado": "ENVIADA",
    "fechaCreacion": "2024-11-19T10:40:00",
    "creadoPor": "admin@example.com",
    "gastos": [...]
  }
}
```

#### POST `/api/proformas/{id}/enviar`
**Descripción:** Enviar proforma por email al cliente
**Acceso:** ADMINISTRADOR

#### GET `/api/proformas/{id}`
**Descripción:** Obtener proforma por ID
**Acceso:** CLIENTE (propia) / ADMINISTRADOR (todas)

#### GET `/api/proformas/codigo/{codigo}`
**Descripción:** Obtener proforma por código
**Acceso:** CLIENTE (propia) / ADMINISTRADOR (todas)

#### GET `/api/proformas/mis-proformas`
**Descripción:** Obtener proformas del cliente autenticado
**Acceso:** CLIENTE

#### GET `/api/proformas/admin/todas?estado={estado}`
**Descripción:** Listar todas las proformas
**Acceso:** ADMINISTRADOR
**Query Params:** `estado` (opcional): ENVIADA | VISTA | ACEPTADA | RECHAZADA | PAGADA

#### PATCH `/api/proformas/{id}/estado?estado={estado}`
**Descripción:** Cambiar estado de proforma
**Acceso:** ADMINISTRADOR

#### POST `/api/proformas/{id}/marcar-vista`
**Descripción:** Marcar proforma como vista
**Acceso:** CLIENTE

#### DELETE `/api/proformas/{id}`
**Descripción:** Eliminar proforma
**Acceso:** ADMINISTRADOR

#### GET `/api/proformas/admin/estadisticas`
**Descripción:** Obtener estadísticas de proformas
**Acceso:** ADMINISTRADOR

---

### 4. ComprobantePagoController (`/api/comprobantes`)

#### POST `/api/comprobantes`
**Descripción:** Subir comprobante de pago
**Acceso:** CLIENTE
**Content-Type:** `multipart/form-data`
**Form Data:**
- `proformaId`: Long (obligatorio)
- `monto`: BigDecimal (obligatorio)
- `archivo`: File (obligatorio)
- `numeroOperacion`: String (opcional)
- `entidadBancaria`: String (opcional)
- `observaciones`: String (opcional)

**Response:**
```json
{
  "success": true,
  "message": "Comprobante subido exitosamente",
  "data": {
    "id": 1,
    "proformaId": 1,
    "proformaCodigo": "PRF-0001-2024",
    "clienteNombre": "Juan Pérez",
    "monto": 590.00,
    "numeroOperacion": "TRF-001",
    "entidadBancaria": "Banco del Perú",
    "archivoComprobante": "comprobantes/comp_123.pdf",
    "estado": "PENDIENTE",
    "fechaSubida": "2024-11-19T11:00:00"
  }
}
```

#### GET `/api/comprobantes/{id}`
**Descripción:** Obtener comprobante por ID
**Acceso:** CLIENTE (propio) / ADMINISTRADOR (todos)

#### GET `/api/comprobantes/proforma/{proformaId}`
**Descripción:** Obtener comprobantes de una proforma
**Acceso:** CLIENTE (propia) / ADMINISTRADOR (todas)

#### GET `/api/comprobantes/mis-comprobantes`
**Descripción:** Obtener comprobantes del cliente autenticado
**Acceso:** CLIENTE

#### GET `/api/comprobantes/admin/pendientes`
**Descripción:** Obtener comprobantes pendientes de verificación
**Acceso:** ADMINISTRADOR

#### GET `/api/comprobantes/admin/por-estado?estado={estado}`
**Descripción:** Obtener comprobantes por estado
**Acceso:** ADMINISTRADOR
**Query Params:** `estado`: PENDIENTE | VERIFICADO | RECHAZADO

#### POST `/api/comprobantes/{id}/verificar`
**Descripción:** Verificar comprobante de pago
**Acceso:** ADMINISTRADOR

#### POST `/api/comprobantes/{id}/rechazar?motivo={motivo}`
**Descripción:** Rechazar comprobante de pago
**Acceso:** ADMINISTRADOR
**Query Params:** `motivo` - String (obligatorio)

#### GET `/api/comprobantes/admin/pendientes/count`
**Descripción:** Contar comprobantes pendientes
**Acceso:** ADMINISTRADOR

---

### 5. ContenidoController (`/api/contenido`)

#### 📷 Imágenes

##### POST `/api/contenido/imagenes`
**Descripción:** Subir imagen
**Acceso:** ADMINISTRADOR
**Content-Type:** `multipart/form-data`
**Form Data:**
- `tipo`: PORTADA | SERVICIO | GALERIA | SOBRE_NOSOTROS
- `titulo`: String
- `descripcion`: String
- `archivo`: File (obligatorio)
- `orden`: Integer

##### GET `/api/contenido/imagenes/publico/{tipo}`
**Descripción:** Obtener imágenes públicas por tipo
**Acceso:** PÚBLICO
**Path Params:** `tipo` - TipoImagen

##### GET `/api/contenido/imagenes/publico`
**Descripción:** Obtener todas las imágenes públicas
**Acceso:** PÚBLICO

##### GET `/api/contenido/imagenes/admin?tipo={tipo}`
**Descripción:** Listar todas las imágenes (admin)
**Acceso:** ADMINISTRADOR
**Query Params:** `tipo` (opcional)

##### PUT `/api/contenido/imagenes/{id}?titulo={titulo}&descripcion={desc}&orden={orden}&activo={activo}`
**Descripción:** Actualizar metadatos de imagen
**Acceso:** ADMINISTRADOR

##### DELETE `/api/contenido/imagenes/{id}`
**Descripción:** Eliminar imagen
**Acceso:** ADMINISTRADOR

#### 🏗️ Proyectos

##### POST `/api/contenido/proyectos`
**Descripción:** Crear proyecto exitoso
**Acceso:** ADMINISTRADOR
**Content-Type:** `multipart/form-data`
**Form Data:**
- `nombre`: String (obligatorio)
- `descripcion`: String
- `ubicacion`: String
- `fechaInicio`: Date
- `fechaFinalizacion`: Date
- `imagenPrincipal`: File
- `imagenesAdicionales`: File[]

##### GET `/api/contenido/proyectos/publico`
**Descripción:** Obtener proyectos públicos activos
**Acceso:** PÚBLICO

##### GET `/api/contenido/proyectos/publico/{id}`
**Descripción:** Obtener proyecto público por ID
**Acceso:** PÚBLICO

##### GET `/api/contenido/proyectos/admin`
**Descripción:** Listar todos los proyectos (admin)
**Acceso:** ADMINISTRADOR

##### PUT `/api/contenido/proyectos/{id}`
**Descripción:** Actualizar proyecto
**Acceso:** ADMINISTRADOR

##### PATCH `/api/contenido/proyectos/{id}/activo?activo={activo}`
**Descripción:** Cambiar estado activo de proyecto
**Acceso:** ADMINISTRADOR

##### DELETE `/api/contenido/proyectos/{id}`
**Descripción:** Eliminar proyecto
**Acceso:** ADMINISTRADOR

---

### 6. FileController (`/uploads`)

#### GET `/uploads/{tipo}/{nombreArchivo}`
**Descripción:** Servir archivo estático
**Acceso:** PÚBLICO
**Path Params:**
- `tipo`: carpeta (solicitudes, comprobantes, imagenes, proyectos)
- `nombreArchivo`: nombre del archivo

#### GET `/uploads/{nombreArchivo}`
**Descripción:** Servir archivo estático simple
**Acceso:** PÚBLICO

---

## 📦 DTOs (Data Transfer Objects)

### ApiResponse<T> (Genérico)

```typescript
{
  success: boolean;      // true si operación exitosa
  message: string;       // Mensaje descriptivo
  data?: T;              // Datos de respuesta (genérico)
  timestamp: Date;       // Timestamp de la respuesta
}
```

### Estados

```typescript
// Estados de Solicitud
enum EstadoSolicitud {
  PENDIENTE = 'PENDIENTE',
  EN_REVISION = 'EN_REVISION',
  APROBADA = 'APROBADA',
  RECHAZADA = 'RECHAZADA'
}

// Estados de Proforma
enum EstadoProforma {
  ENVIADA = 'ENVIADA',
  VISTA = 'VISTA',
  ACEPTADA = 'ACEPTADA',
  RECHAZADA = 'RECHAZADA',
  PAGADA = 'PAGADA'
}

// Estados de Comprobante
enum EstadoComprobante {
  PENDIENTE = 'PENDIENTE',
  VERIFICADO = 'VERIFICADO',
  RECHAZADO = 'RECHAZADO'
}

// Tipos de Usuario
enum TipoUsuario {
  CLIENTE_NATURAL = 'CLIENTE_NATURAL',
  CLIENTE_JURIDICO = 'CLIENTE_JURIDICO',
  ADMINISTRADOR = 'ADMINISTRADOR'
}

// Tipos de Imagen
enum TipoImagen {
  PORTADA = 'PORTADA',
  SERVICIO = 'SERVICIO',
  GALERIA = 'GALERIA',
  SOBRE_NOSOTROS = 'SOBRE_NOSOTROS'
}
```

---

## 🔐 Autenticación JWT

### Flujo de Autenticación

```
┌─────────────┐                 ┌─────────────┐                 ┌─────────────┐
│   Cliente   │                 │   Backend   │                 │  Database   │
└──────┬──────┘                 └──────┬──────┘                 └──────┬──────┘
       │                               │                               │
       │ 1. POST /api/auth/login       │                               │
       │ { email, password }            │                               │
       ├──────────────────────────────>│                               │
       │                               │ 2. Validar credenciales       │
       │                               ├──────────────────────────────>│
       │                               │<──────────────────────────────┤
       │                               │ 3. Generar JWT                │
       │                               │    (userId, tipoUsuario, exp) │
       │ 4. Return { token, ... }      │                               │
       │<──────────────────────────────┤                               │
       │                               │                               │
       │ 5. Almacenar token en         │                               │
       │    sessionStorage              │                               │
       │                               │                               │
       │ 6. Siguientes requests        │                               │
       │ Header: Authorization: Bearer │                               │
       ├──────────────────────────────>│                               │
       │                               │ 7. Validar JWT                │
       │                               │    y cargar usuario           │
       │                               │                               │
```

### Configuración JWT

| Propiedad | Valor |
|-----------|-------|
| **Algoritmo** | HS256 (HMAC SHA-256) |
| **Expiración** | 24 horas (86400000 ms) |
| **Tipo Token** | Bearer |
| **Header** | Authorization: Bearer {token} |
| **Almacenamiento** | sessionStorage (frontend) |

### Claims del JWT

```json
{
  "userId": 1,
  "tipoUsuario": "CLIENTE_NATURAL",
  "correo": "cliente@example.com",
  "sub": "cliente@example.com",
  "iat": 1700000000,
  "exp": 1700086400
}
```

### Interceptores Angular

#### AuthInterceptor
- Agrega automáticamente el token JWT a cada request
- Excluye rutas públicas: `/auth/**`, `/contenido/**/publico/**`, `/uploads/**`

#### ErrorInterceptor
- Maneja errores HTTP globalmente
- Si recibe 401: logout automático y redirección a login
- Muestra mensajes de error amigables

---

## 🎯 Servicios Angular

### 1. AuthService

```typescript
class AuthService {
  // Autenticación
  login(credentials: LoginRequest): Observable<ApiResponse<LoginResponse>>
  logout(): void

  // Registro
  registrarPersonaNatural(data: RegistroPersonaNatural): Observable<ApiResponse<any>>
  registrarPersonaJuridica(data: RegistroPersonaJuridica): Observable<ApiResponse<any>>

  // Validación
  checkEmailAvailability(email: string): Observable<ApiResponse<boolean>>
  checkDniAvailability(dni: string): Observable<ApiResponse<boolean>>
  checkRucAvailability(ruc: string): Observable<ApiResponse<boolean>>

  // Estado
  isAuthenticated(): boolean
  getCurrentUser(): any
  getUserType(): string | null
  hasRole(role: string): boolean
  isAdmin(): boolean
  isCliente(): boolean
  getToken(): string | null
}
```

### 2. SolicitudService

```typescript
class SolicitudService {
  crearSolicitud(formData: FormData): Observable<ApiResponse<SolicitudProforma>>
  obtenerMisSolicitudes(): Observable<ApiResponse<SolicitudProforma[]>>
  obtenerSolicitudPorId(id: number): Observable<ApiResponse<SolicitudProforma>>
  obtenerTodasSolicitudes(estado?: EstadoSolicitud): Observable<ApiResponse<SolicitudProforma[]>>
  aprobarSolicitud(id: number): Observable<ApiResponse<SolicitudProforma>>
  rechazarSolicitud(id: number, motivo: string): Observable<ApiResponse<SolicitudProforma>>
  cambiarEstado(id: number, estado: EstadoSolicitud, motivoRechazo?: string): Observable<ApiResponse<SolicitudProforma>>
  contarSolicitudesPendientes(): Observable<ApiResponse<number>>
}
```

### 3. ProformaService

```typescript
class ProformaService {
  crearProforma(data: CrearProformaRequest): Observable<ApiResponse<Proforma>>
  enviarProforma(id: number): Observable<ApiResponse<void>>
  obtenerProformaPorId(id: number): Observable<ApiResponse<Proforma>>
  obtenerProformaPorCodigo(codigo: string): Observable<ApiResponse<Proforma>>
  obtenerMisProformas(): Observable<ApiResponse<Proforma[]>>
  obtenerTodasProformas(estado?: EstadoProforma): Observable<ApiResponse<Proforma[]>>
  marcarComoVista(id: number): Observable<ApiResponse<void>>
  obtenerEstadisticas(): Observable<ApiResponse<any>>
}
```

### 4. ComprobantePagoService

```typescript
class ComprobantePagoService {
  subirComprobante(
    proformaId: number,
    monto: number,
    archivo: File,
    numeroOperacion?: string,
    entidadBancaria?: string,
    observaciones?: string
  ): Observable<ApiResponse<ComprobantePago>>

  obtenerComprobantePorId(id: number): Observable<ApiResponse<ComprobantePago>>
  obtenerMisComprobantes(): Observable<ApiResponse<ComprobantePago[]>>
  obtenerComprobantesPorProforma(proformaId: number): Observable<ApiResponse<ComprobantePago[]>>
  verificarComprobante(id: number): Observable<ApiResponse<ComprobantePago>>
  rechazarComprobante(id: number, motivo: string): Observable<ApiResponse<ComprobantePago>>
}
```

### 5. ContenidoService

```typescript
class ContenidoService {
  // Imágenes
  subirImagen(formData: FormData): Observable<ApiResponse<Imagen>>
  obtenerImagenesPorTipo(tipo: TipoImagen): Observable<ApiResponse<Imagen[]>>
  obtenerImagenesActivasPorTipo(tipo: TipoImagen): Observable<ApiResponse<Imagen[]>>
  obtenerTodasImagenes(): Observable<ApiResponse<Imagen[]>>
  actualizarImagen(id: number, formData: FormData): Observable<ApiResponse<Imagen>>
  eliminarImagen(id: number): Observable<ApiResponse<void>>

  // Proyectos
  crearProyecto(formData: FormData): Observable<ApiResponse<ProyectoExitoso>>
  obtenerProyectosActivos(): Observable<ApiResponse<ProyectoExitoso[]>>
  obtenerProyectoPorId(id: number): Observable<ApiResponse<ProyectoExitoso>>
  actualizarProyecto(id: number, proyecto: ProyectoExitoso): Observable<ApiResponse<ProyectoExitoso>>
  eliminarProyecto(id: number): Observable<ApiResponse<void>>
}
```

---

## 📋 Ejemplos de Request/Response

### Ejemplo 1: Login Exitoso

**Request:**
```http
POST /api/auth/login HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "correoElectronico": "cliente@example.com",
  "contrasena": "Password123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "timestamp": "2024-11-19T10:30:00",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInRpcG9Vc3VhcmlvIjoiQ0xJRU5URV9OQVRVUkFMIiwiY29ycmVvIjoiY2xpZW50ZUBleGFtcGxlLmNvbSIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.abc123...",
    "tipoToken": "Bearer",
    "expiraEn": 86400000,
    "correoElectronico": "cliente@example.com",
    "tipoUsuario": "CLIENTE_NATURAL",
    "nombreCompleto": "Juan Pérez García"
  }
}
```

### Ejemplo 2: Crear Solicitud

**Request:**
```http
POST /api/solicitudes HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary

------WebKitFormBoundary
Content-Disposition: form-data; name="titulo"

Solicitud de Remodelación de Local
------WebKitFormBoundary
Content-Disposition: form-data; name="descripcion"

Necesito presupuesto para remodelar mi local comercial de 50m2
------WebKitFormBoundary
Content-Disposition: form-data; name="archivo"; filename="plano.pdf"
Content-Type: application/pdf

[Binary file data]
------WebKitFormBoundary--
```

**Response (201 CREATED):**
```json
{
  "success": true,
  "message": "Solicitud creada exitosamente",
  "timestamp": "2024-11-19T10:35:00",
  "data": {
    "id": 1,
    "titulo": "Solicitud de Remodelación de Local",
    "descripcion": "Necesito presupuesto para remodelar mi local comercial de 50m2",
    "archivoAdjunto": "solicitudes/plano_1700123456.pdf",
    "estado": "PENDIENTE",
    "motivoRechazo": null,
    "fechaSolicitud": "2024-11-19T10:35:00",
    "fechaRevision": null,
    "revisadoPor": null,
    "clienteNombre": "Juan Pérez García"
  }
}
```

### Ejemplo 3: Crear Proforma

**Request:**
```http
POST /api/proformas HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "solicitudId": 1,
  "vigenciaHasta": "2024-12-19",
  "observaciones": "Precios sujetos a variación según disponibilidad de materiales",
  "gastos": [
    {
      "concepto": "Materiales de Construcción",
      "descripcion": "Cemento Portland, arena gruesa, ladrillos King Kong",
      "cantidad": 100,
      "unidad": "UND",
      "precioUnitario": 15.50,
      "orden": 1
    },
    {
      "concepto": "Mano de Obra Especializada",
      "descripcion": "Albañiles certificados con experiencia",
      "cantidad": 20,
      "unidad": "JORNAL",
      "precioUnitario": 80.00,
      "orden": 2
    },
    {
      "concepto": "Herramientas y Equipos",
      "descripcion": "Alquiler de mezcladora, andamios, herramientas menores",
      "cantidad": 1,
      "unidad": "GLOBAL",
      "precioUnitario": 500.00,
      "orden": 3
    }
  ]
}
```

**Response (201 CREATED):**
```json
{
  "success": true,
  "message": "Proforma creada exitosamente",
  "timestamp": "2024-11-19T10:40:00",
  "data": {
    "id": 1,
    "codigo": "PRF-0001-2024",
    "clienteNombre": "Juan Pérez García",
    "clienteCorreo": "cliente@example.com",
    "subtotal": 3650.00,
    "igv": 657.00,
    "total": 4307.00,
    "vigenciaHasta": "2024-12-19",
    "observaciones": "Precios sujetos a variación según disponibilidad de materiales",
    "estado": "ENVIADA",
    "fechaCreacion": "2024-11-19T10:40:00",
    "fechaEnvio": null,
    "creadoPor": "admin@constructora.com",
    "gastos": [
      {
        "id": 1,
        "concepto": "Materiales de Construcción",
        "descripcion": "Cemento Portland, arena gruesa, ladrillos King Kong",
        "cantidad": 100,
        "unidad": "UND",
        "precioUnitario": 15.50,
        "subtotal": 1550.00,
        "orden": 1
      },
      {
        "id": 2,
        "concepto": "Mano de Obra Especializada",
        "descripcion": "Albañiles certificados con experiencia",
        "cantidad": 20,
        "unidad": "JORNAL",
        "precioUnitario": 80.00,
        "subtotal": 1600.00,
        "orden": 2
      },
      {
        "id": 3,
        "concepto": "Herramientas y Equipos",
        "descripcion": "Alquiler de mezcladora, andamios, herramientas menores",
        "cantidad": 1,
        "unidad": "GLOBAL",
        "precioUnitario": 500.00,
        "subtotal": 500.00,
        "orden": 3
      }
    ]
  }
}
```

---

## ⚠️ Manejo de Errores

### Códigos de Estado HTTP

| Código | Significado | Acción Frontend |
|--------|-------------|-----------------|
| **200** | OK | Operación exitosa |
| **201** | Created | Recurso creado exitosamente |
| **400** | Bad Request | Validación fallida, mostrar errores |
| **401** | Unauthorized | Logout automático, redirigir a login |
| **403** | Forbidden | Mostrar "No tiene permisos" |
| **404** | Not Found | Mostrar "Recurso no encontrado" |
| **500** | Internal Server Error | Mostrar "Error del servidor" |

### Formato de Respuesta de Error

```json
{
  "success": false,
  "message": "Error descriptivo del problema",
  "timestamp": "2024-11-19T10:50:00",
  "data": null
}
```

### Ejemplos de Errores

**Error 401 - No Autorizado:**
```json
{
  "success": false,
  "message": "Token JWT inválido o expirado",
  "timestamp": "2024-11-19T10:50:00"
}
```

**Error 400 - Validación:**
```json
{
  "success": false,
  "message": "Errores de validación en los datos enviados",
  "timestamp": "2024-11-19T10:52:00",
  "data": {
    "correoElectronico": "Debe ser un email válido",
    "contrasena": "Debe tener al menos 8 caracteres"
  }
}
```

**Error 403 - Prohibido:**
```json
{
  "success": false,
  "message": "No tiene permisos para realizar esta acción",
  "timestamp": "2024-11-19T10:53:00"
}
```

---

## 🔒 Roles y Permisos

### Matriz de Permisos

| Endpoint | PÚBLICO | CLIENTE | ADMIN |
|----------|---------|---------|-------|
| **Autenticación** |
| POST /auth/login | ✅ | ✅ | ✅ |
| POST /auth/registro/* | ✅ | - | - |
| GET /auth/validate | - | ✅ | ✅ |
| **Solicitudes** |
| POST /solicitudes | - | ✅ | ❌ |
| GET /solicitudes/mis-solicitudes | - | ✅ | ❌ |
| GET /solicitudes/admin/todas | - | ❌ | ✅ |
| PATCH /solicitudes/{id}/estado | - | ❌ | ✅ |
| **Proformas** |
| POST /proformas | - | ❌ | ✅ |
| GET /proformas/mis-proformas | - | ✅ | ❌ |
| GET /proformas/admin/todas | - | ❌ | ✅ |
| POST /proformas/{id}/enviar | - | ❌ | ✅ |
| **Comprobantes** |
| POST /comprobantes | - | ✅ | ❌ |
| GET /comprobantes/mis-comprobantes | - | ✅ | ❌ |
| POST /comprobantes/{id}/verificar | - | ❌ | ✅ |
| **Contenido** |
| GET /contenido/*/publico/* | ✅ | ✅ | ✅ |
| POST /contenido/imagenes | - | ❌ | ✅ |
| POST /contenido/proyectos | - | ❌ | ✅ |
| DELETE /contenido/* | - | ❌ | ✅ |
| **Archivos** |
| GET /uploads/* | ✅ | ✅ | ✅ |

### Descripción de Roles

#### PÚBLICO
- Sin autenticación requerida
- Puede: Ver contenido público, registrarse, iniciar sesión

#### CLIENTE (CLIENTE_NATURAL / CLIENTE_JURIDICO)
- Requiere autenticación con token JWT
- Puede: Crear solicitudes, ver sus solicitudes, ver sus proformas, subir comprobantes de pago

#### ADMINISTRADOR
- Requiere autenticación con token JWT y rol ADMINISTRADOR
- Puede: Todo lo del cliente + gestionar solicitudes, crear proformas, verificar comprobantes, gestionar contenido web

---

## 📝 Notas Adicionales

### Configuración CORS

El backend permite peticiones desde:
- `http://localhost:4200` (desarrollo)
- Métodos permitidos: GET, POST, PUT, DELETE, PATCH, OPTIONS
- Headers permitidos: Authorization, Content-Type
- Credenciales permitidas: Sí

### Almacenamiento de Archivos

Los archivos subidos se almacenan en:
- **Solicitudes:** `/uploads/solicitudes/`
- **Comprobantes:** `/uploads/comprobantes/`
- **Imágenes:** `/uploads/imagenes/`
- **Proyectos:** `/uploads/proyectos/`

### Cálculos de Proforma

```typescript
subtotal = Σ(cantidad * precioUnitario) // Suma de todos los gastos
igv = subtotal * 0.18                   // 18% de IGV
total = subtotal + igv                  // Total a pagar
```

### Formato de Fechas

- **Backend:** `yyyy-MM-dd'T'HH:mm:ss` (ISO 8601)
- **Frontend:** Date objects de JavaScript
- **API:** Strings en formato ISO 8601

---

## 🚀 Inicio Rápido

### Backend (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

### Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

La aplicación estará disponible en: `http://localhost:4200`

---

## 📞 Contacto y Soporte

Para dudas o soporte técnico:
- 📧 Email: soporte@constructora.com
- 📱 Teléfono: +51 987 654 321

---

**Última actualización:** 2024-11-19
**Versión del documento:** 1.0.0
**Autor:** Equipo de Desarrollo Constructora
