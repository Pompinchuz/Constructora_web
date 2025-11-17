# Documentación de la Rama `modelyisus`

## Información General

**Rama**: `modelyisus`
**Última actualización**: 83 minutos atrás
**Commits adelante de main**: 4 commits
**Autor principal**: Pompinchuz

---

## Resumen de Commits

### 1. **Version 05-dubi** (8bacfd0) - 83 minutos atrás
Refinamiento y ajustes finales al sistema de autenticación y gestión de contenido web.

**Archivos modificados**: 20 archivos
- **Backend** (6 archivos): Ajustes en seguridad, servicios de autenticación y controladores
- **Frontend** (14 archivos): Mejoras en componentes de administración y gestión de contenido
- **Assets**: Imágenes y archivos de proyectos cargados

**Cambios principales**:
- Refinamiento de `SecurityConfig.java` (75 líneas modificadas)
- Optimización de `ContenidoController.java` (97 líneas modificadas)
- Mejoras en `FileController.java` (110 líneas modificadas)
- Actualización de servicios de autenticación y usuarios
- Expansión masiva del componente `contenido-web` (646 líneas CSS, 433 líneas HTML, 412 líneas TS)
- Mejoras en servicios frontend (auth, contenido, proyecto)

---

### 2. **Jwt + Authentication + Dising Home principal** (b53db78) - 29 horas atrás
Implementación completa del sistema de autenticación JWT y diseño de la página principal.

**Archivos modificados**: 94 archivos
**Líneas agregadas**: +8,771 | **Líneas eliminadas**: -292

#### Backend - Nuevas Características

**Dependencias (pom.xml)**:
- JWT (JSON Web Tokens)
- Spring Security
- Email/SMTP
- File Upload

**Configuración**:
- `AsyncConfig.java`: Configuración para operaciones asíncronas
- `CorsConfig.java`: Configuración CORS para comunicación frontend-backend
- `JwtAuthenticationFilter.java`: Filtro de autenticación JWT (105 líneas)
- `SecurityConfig.java`: Configuración de seguridad Spring (115 líneas)
- `WebConfig.java`: Configuración web general

**Controladores Nuevos**:
- `AuthController.java` (175 líneas): Login, registro, gestión de usuarios
- `ComprobanteController.java` (286 líneas): Gestión de comprobantes de pago
- `ContenidoController.java` (367 líneas): Gestión del contenido web
- `FileController.java` (92 líneas): Carga y gestión de archivos
- `ProformaController.java` (276 líneas): Gestión de proformas
- `SolicitudController.java` (269 líneas): Gestión de solicitudes

**DTOs (Data Transfer Objects)**:
- `ProformaEstadisticasDTO.java`: Estadísticas de proformas

**Servicios Nuevos**:
- `AuthService.java`: Lógica de autenticación
- `ComprobanteService.java`: Lógica de comprobantes (121 líneas)
- `ContenidoService.java`: Gestión de contenido web (336 líneas)
- `CustomUserDetailsService.java`: Integración con Spring Security
- `EmailService.java`: Envío de correos electrónicos (282 líneas)
- `FileStorageService.java`: Almacenamiento de archivos (186 líneas)
- `JwtService.java`: Generación y validación de tokens JWT (163 líneas)
- `ProformaService.java`: Lógica de proformas (82 líneas)
- `SolicitudProformaService.java`: Lógica de solicitudes (84 líneas)

**Templates**:
- `templates/email/bienvenida.html`: Email de bienvenida (180 líneas)

#### Frontend - Nuevas Características

**Dependencias (package.json)**:
- Nuevas librerías para autenticación y UI

**Rutas (app.routes.ts)**:
- Rutas de autenticación (login, registro)
- Rutas de administración
- Rutas de cliente
- Rutas públicas

**Componentes de Administración**:
- `admin-dashboard`: Dashboard principal (125 líneas)
- `solicitudes-list`: Lista de solicitudes (125 líneas)

**Componentes de Autenticación**:
- `login`: Formulario de login (94 líneas HTML, 101 líneas TS)
- `register`: Formulario de registro (204 líneas HTML, 154 líneas TS)
- `registro-tipo`: Selector de tipo de registro (118 líneas)

**Componentes de Cliente**:
- `cliente-dashboard`: Dashboard del cliente (97 líneas HTML, 74 líneas TS)
- `mis-solicitudes`: Solicitudes del cliente (100 líneas)
- `nueva-solicitud`: Creación de solicitudes (142 líneas)

**Componentes Públicos**:
- `contacto`: Página de contacto (217 líneas HTML, 75 líneas TS)
- `footer`: Pie de página (147 líneas)
- `hero-section`: Sección hero (190 líneas CSS, 72 líneas HTML, 33 líneas TS)
- `home`: Página principal
- `navbar`: Barra de navegación (279 líneas CSS, 75 líneas HTML, 67 líneas TS)
- `proyectos`: Galería de proyectos (205 líneas CSS, 141 líneas HTML, 48 líneas TS)
- `servicios`: Página de servicios (79 líneas HTML, 70 líneas TS)
- `sobre-nosotros`: Página acerca de (120 líneas HTML, 45 líneas TS)
- `public-layout`: Layout público

**Constantes y Modelos**:
- `api-endpoints.ts`: Endpoints del API (73 líneas)
- `app-constants.ts`: Constantes de la aplicación (57 líneas)
- `constants.ts`: Constantes generales (45 líneas)
- `estados.constants.ts`: Estados del sistema (89 líneas)
- Modelos: auth, cliente, comprobante, contenido, proforma, solicitud

**Guards (Protección de Rutas)**:
- `admin.guard.ts`: Protección de rutas de administrador
- `auth.guard.ts`: Protección de rutas autenticadas
- `cliente.guard.ts`: Protección de rutas de cliente

**Interceptors**:
- `auth.interceptor.ts`: Interceptor de autenticación (31 líneas)
- `error.interceptor.ts`: Manejo de errores (62 líneas)
- `loading.interceptor.ts`: Indicador de carga (22 líneas)

**Servicios**:
- `auth.service.ts`: Servicio de autenticación (167 líneas)
- `contenido.service.ts`: Servicio de contenido (69 líneas)
- `loading.service.ts`: Servicio de loading (37 líneas)
- `notification.service.ts`: Servicio de notificaciones (36 líneas)
- `proforma.service.ts`: Servicio de proformas (75 líneas)
- `proyecto.service.ts`: Servicio de proyectos (69 líneas)
- `solicitud.service.ts`: Servicio de solicitudes (70 líneas)

**Configuración**:
- `environment.ts` y `environment.prod.ts`: Variables de entorno (7 líneas cada uno)
- `tailwind.config.js.txt`: Configuración de Tailwind CSS (25 líneas)

**Estilos**:
- `styles.css`: Estilos globales (+325 líneas)

---

### 3. **sub v004** (64d0866) - 7 días atrás
Ajustes menores en servicios del backend.

**Archivos modificados**: 4 archivos
- `AuthService.java`
- `ComprobanteService.java`
- `ProformaService.java`
- `SolicitudProformaService.java`

**Cambios**: +8 líneas | -7 líneas

---

### 4. **Av03-R4** (e8570a2) - 7 días atrás
Refactorización masiva de la arquitectura del proyecto.

**Archivos modificados**: 279 archivos
**Cambios**: +3,699 líneas | -1,496 líneas

#### Cambios Estructurales Importantes

**Backend - Refactorización de Modelos a Entidades**:
- **Eliminados** (carpeta `model/`):
  - `Cliente.java`, `ClienteEmpresa.java`, `ClienteNatural.java`
  - `Comprobante.java`, `DocumentoAdjunto.java`
  - `FormularioContacto.java`, `ImagenProyecto.java`
  - `Pago.java`, `PagoCredito.java`, `PagoDebito.java`, `PagoPlin.java`, `PagoYape.java`
  - `Proyecto.java`, `Usuario.java`
  - Enums: `EstadoPago`, `EstadoProforma`, `Rol`, `TipoCliente`, `TipoComprobante`, `TipoDocumento`

- **Creados** (carpeta `entity/`):
  - `Administrador.java` (32 líneas)
  - `Cliente.java` (52 líneas)
  - `ClientePersonaJuridica.java` (31 líneas)
  - `ClientePersonaNatural.java` (33 líneas)
  - `ComprobantePago.java` (59 líneas)
  - `GastoProforma.java` (40 líneas)
  - `Imagen.java` (44 líneas)
  - `ImagenProyecto.java` (24 líneas)
  - `Proforma.java` (71 líneas)
  - `ProyectoExitoso.java` (47 líneas)
  - `SolicitudProforma.java` (57 líneas)
  - `Usuario.java` (49 líneas)
  - Enums: `EstadoComprobante`, `EstadoProforma`, `EstadoSolicitud`, `TipoImagen`, `TipoUsuario`

**DTOs Nuevos**:
- `ApiResponseDTO.java` (31 líneas)
- `ClientePersonaJuridicaResponseDTO.java` (19 líneas)
- `ClientePersonaNaturalResponseDTO.java` (21 líneas)
- `CrearProformaDTO.java` (24 líneas)
- `GastoProformaDTO.java` (29 líneas)
- `GastoProformaResponseDTO.java` (18 líneas)
- `ImagenDTO.java` (23 líneas)
- `ImagenResponseDTO.java` (20 líneas)
- `ProformaResponseDTO.java` (28 líneas)
- `ProyectoExitosoDTO.java` (29 líneas)
- `ProyectoExitosoResponseDTO.java` (20 líneas)
- `RegistroClienteDTO.java` (27 líneas)
- `SolicitudProformaDTO.java` (17 líneas)
- `SolicitudProformaResponseDTO.java` (21 líneas)
- Auth: `LoginRequestDTO`, `LoginResponseDTO`, `RegistroPersonaJuridicaDTO`, `RegistroPersonaNaturalDTO`
- Cliente: `ClienteResponseDTO`
- Comprobante: `ComprobanteResponseDTO`, `SubirComprobanteDTO`

**Manejo de Excepciones**:
- `BadRequestException.java` (8 líneas)
- `ConflictException.java` (8 líneas)
- `EmailSendingException.java` (12 líneas)
- `ErrorDetails.java` (20 líneas)
- `FileStorageException.java` (12 líneas)
- `ForbiddenException.java` (8 líneas)
- `GlobalExceptionHandler.java` (178 líneas) - Manejo centralizado de errores
- `NotFoundException.java` (12 líneas)
- `ResourceNotFoundException.java` (12 líneas)
- `UnauthorizedException.java` (8 líneas)

**Repositorios Nuevos**:
- `AdministradorRepository.java` (18 líneas)
- `ClientePersonaJuridicaRepository.java` (20 líneas)
- `ClientePersonaNaturalRepository.java` (20 líneas)
- `ClienteRepository.java` (20 líneas)
- `ComprobantePagoRepository.java` (26 líneas)
- `GastoProformaRepository.java` (19 líneas)
- `ImagenProyectoRepository.java` (15 líneas)
- `ImagenRepository.java` (20 líneas)
- `ProformaRepository.java` (35 líneas)
- `ProyectoExitosoRepository.java` (21 líneas)
- `SolicitudProformaRepository.java` (34 líneas)
- `UsuarioRepository.java` (actualizado)

**Seguridad - Eliminado**:
- `JwtAuthFilter.java` (48 líneas)
- `JwtUtil.java` (38 líneas)
- `SecurityConfig.java` (48 líneas del anterior)

**Servicios Nuevos**:
- `AuthService.java` (157 líneas)
- `ComprobanteService.java` (81 líneas)
- `ProformaService.java` (172 líneas)
- `SolicitudProformaService.java` (122 líneas)

**Frontend - Reestructuración**:

**Eliminados**:
- `components/home/` (completo: 176 líneas CSS, 150 HTML, 95 TS)
- `components/login/` (completo: 78 líneas CSS, 27 HTML, 35 TS)
- `guards/auth.guard.ts`
- `interceptors/auth.interceptor.ts` (movido a `core/`)

**Creados - Estructura Modular**:
- `components/admin.module.ts` (12 líneas)
- `components/auth.module.ts` (12 líneas)
- `components/cliente.module.ts` (12 líneas)
- `components/shared.module.ts` (12 líneas)

**Componentes Admin** (todos nuevos con archivos básicos):
- `admin-dashboard/`: CSS, HTML, spec, TS (11 líneas)
- `admin-layout/`: CSS, HTML, spec, TS (11 líneas)
- `comprobantes-list/`: CSS, HTML, spec, TS (11 líneas)
- `contenido-web/`: CSS, HTML, spec, TS (11 líneas)
- `crear-proforma/`: CSS, HTML, spec, TS (11 líneas)
- `crear-proyecto/`: CSS, HTML, spec, TS (11 líneas)
- `proformas-list/`: CSS, HTML, spec, TS (11 líneas)
- `proyectos-list/`: CSS, HTML, spec, TS (11 líneas)
- `solicitud-detail/`: CSS, HTML, spec, TS (11 líneas)
- `solicitudes-list/`: CSS, HTML, spec, TS (11 líneas)

**Componentes Auth**:
- `login/`: CSS, HTML, spec, TS (11 líneas base)
- `register/`: CSS, HTML, spec, TS (11 líneas)
- `registro-tipo/`: CSS, HTML, spec, TS (11 líneas)

**Componentes Cliente**:
- `cliente-dashboard/`: CSS, HTML, spec, TS (11 líneas)
- `cliente-layout/`: CSS, HTML, spec, TS (11 líneas)
- `mis-proformas/`: CSS, HTML, spec, TS (11 líneas)
- `mis-solicitudes/`: CSS, HTML, spec, TS (11 líneas)
- `nueva-solicitud/`: CSS, HTML, spec, TS (11 líneas)
- `proforma-detail/`: CSS, HTML, spec, TS (11 líneas)
- `subir-comprobante/`: CSS, HTML, spec, TS (11 líneas)

**Componentes Públicos** (todos con archivos básicos):
- `contacto/`: CSS, HTML, spec, TS (11 líneas)
- `footer/`: CSS, HTML, spec, TS (11 líneas)
- `hero-section/`: CSS, HTML, spec, TS (11 líneas)
- `home/`: HTML (vacío), TS (48 líneas)
- `navbar/`: CSS, HTML, spec, TS (11 líneas)
- `proyectos/`: CSS, HTML, spec, TS (11 líneas)
- `public-layout/`: HTML (vacío), TS (25 líneas)
- `servicios/`: CSS, HTML, spec, TS (11 líneas)
- `sobre-nosotros/`: CSS, HTML, spec, TS (11 líneas)

**Componentes Compartidos**:
- `alert/`: CSS, HTML, spec, TS (11 líneas)
- `confirmation-dialog/`: CSS, HTML, spec, TS (11 líneas)
- `estado-badge/`: CSS, HTML, spec, TS (11 líneas)
- `file-upload/`: CSS, HTML, spec, TS (11 líneas)
- `loading-spinner/`: CSS, HTML, spec, TS (11 líneas)

**Core - Guards** (con specs):
- `admin.guard.ts` (5 líneas + 17 spec)
- `auth.guard.ts` (5 líneas + 17 spec)
- `cliente.guard.ts` (5 líneas + 17 spec)

**Core - Interceptors** (con specs):
- `auth.interceptor.ts` (movido, sin command + 17 spec)
- `error.interceptor.ts` (5 líneas + 17 spec)
- `loading.interceptor.ts` (5 líneas + 17 spec)

**Core - Utils** (archivos vacíos):
- `date-utils.ts`
- `file-utils.ts`
- `validators.ts`

**Servicios** (todos con specs de 16 líneas):
- `auth.service.ts` (55 líneas reducidas)
- `cliente.service.ts` (9 líneas)
- `comprobante.service.ts` (9 líneas)
- `contenido.service.ts` (9 líneas)
- `file.service.ts` (9 líneas)
- `notification.service.ts` (9 líneas)
- `proforma.service.ts` (9 líneas)
- `proyecto.service.ts` (9 líneas)
- `solicitud.service.ts` (9 líneas)

**Modelos** (archivos creados):
- `api.models.ts` (14 líneas)
- `auth.models.ts` (vacío)
- `cliente.models.ts` (vacío)
- `comprobante.models.ts` (28 líneas)
- `contenido.models.ts` (39 líneas)
- `proforma.models.ts` (vacío)
- `solicitud.models.ts` (vacío)

**Constantes** (archivos vacíos en este commit):
- `api-endpoints.ts`
- `app-constants.ts`
- `constants.ts`
- `estados.constants.ts`

**Assets**:
- Logo movido: `assets/Logo_em.png` → `assets/images/logos/Logo_em.png`
- Archivo placeholder: `assets/sd`

**Environments**:
- `environment.prod.ts` (vacío)
- `environment.ts` (4 líneas eliminadas)

---

## Características Principales Implementadas

### 🔐 Autenticación y Seguridad
- **JWT (JSON Web Tokens)**: Sistema completo de autenticación basado en tokens
- **Spring Security**: Configuración robusta de seguridad
- **Filtros de Autenticación**: Interceptores para validar tokens
- **Guards de Angular**: Protección de rutas por roles (admin, cliente, auth)
- **Interceptores HTTP**: Manejo automático de autenticación y errores
- **Tipos de Usuario**: Administrador, Cliente Persona Natural, Cliente Persona Jurídica

### 📧 Sistema de Emails
- **EmailService**: Servicio de envío de correos (282 líneas)
- **Templates HTML**: Email de bienvenida personalizado
- **Notificaciones**: Sistema de notificaciones al usuario

### 📁 Gestión de Archivos
- **FileStorageService**: Almacenamiento local de archivos (186 líneas)
- **FileController**: Endpoints para subida y descarga
- **Tipos de Archivos**: Imágenes, documentos, comprobantes
- **Upload de Imágenes**: Para proyectos y contenido web

### 💼 Gestión de Proformas
- **CRUD Completo**: Crear, leer, actualizar, eliminar proformas
- **Gastos**: Gestión de gastos asociados a proformas
- **Estados**: Pendiente, aprobada, rechazada
- **Estadísticas**: Dashboard con métricas de proformas

### 📋 Gestión de Solicitudes
- **Solicitudes de Proforma**: Clientes pueden solicitar presupuestos
- **Workflow**: Estados de solicitud (nueva, en proceso, completada)
- **Seguimiento**: Historial de solicitudes por cliente

### 💳 Gestión de Comprobantes
- **Subida de Comprobantes**: Clientes pueden subir comprobantes de pago
- **Validación**: Administradores validan los comprobantes
- **Estados**: Pendiente, aprobado, rechazado

### 🌐 Gestión de Contenido Web
- **CMS Básico**: Gestión de contenido dinámico
- **Secciones**: Hero, Servicios, Proyectos, Sobre Nosotros, Contacto
- **Imágenes**: Gestión de imágenes para cada sección
- **Proyectos Exitosos**: Galería de proyectos completados

### 🎨 Frontend - Diseño y UX
- **Tailwind CSS**: Framework de utilidades CSS
- **Componentes Reutilizables**: Alert, Loading Spinner, File Upload, etc.
- **Diseño Responsive**: Adaptado a móviles y desktop
- **Navegación**: Navbar y Footer con enlaces dinámicos
- **Layouts**: Separación de layouts público, cliente y admin

### 🔄 Arquitectura Modular
- **Módulos**: Admin, Auth, Cliente, Shared, Public
- **Separación de Responsabilidades**: Componentes, servicios, guards, interceptors
- **DTOs**: Transferencia de datos tipada
- **Modelos**: Interfaces TypeScript para datos

### 🗄️ Base de Datos
- **JPA/Hibernate**: ORM para persistencia
- **Entidades**: 12 entidades principales
- **Relaciones**: OneToMany, ManyToOne, OneToOne
- **Repositorios**: 11 repositorios con queries personalizadas

### ⚠️ Manejo de Errores
- **GlobalExceptionHandler**: Manejo centralizado de excepciones (178 líneas)
- **Excepciones Personalizadas**: 10 tipos de excepciones específicas
- **ErrorInterceptor**: Manejo de errores HTTP en Angular
- **Mensajes Amigables**: Respuestas JSON con detalles del error

---

## Estadísticas Generales

**Total de cambios (todos los commits)**:
- **Archivos creados**: ~150 archivos
- **Archivos modificados**: ~120 archivos
- **Archivos eliminados**: ~30 archivos
- **Líneas agregadas**: +12,470 líneas
- **Líneas eliminadas**: -1,788 líneas
- **Cambio neto**: +10,682 líneas

**Distribución Backend/Frontend**:
- **Backend**: ~40% de los cambios
- **Frontend**: ~60% de los cambios

---

## Tecnologías Utilizadas

### Backend
- **Java** con Spring Boot
- **Spring Security** + JWT
- **Spring Data JPA** + Hibernate
- **MySQL** (asumido por contexto)
- **JavaMail** para emails
- **Lombok** (probablemente)
- **Maven** como build tool

### Frontend
- **Angular** (versión reciente)
- **TypeScript**
- **Tailwind CSS**
- **RxJS** para programación reactiva
- **Angular Router** para navegación
- **HttpClient** para comunicación con API

---

## Estructura de Directorios

### Backend
```
backend/src/main/java/com/constructora/backend/
├── config/          # Configuración (Security, CORS, JWT, etc.)
├── controller/      # Controladores REST
│   └── dto/         # Data Transfer Objects
├── entity/          # Entidades JPA
│   └── enums/       # Enumeraciones
├── exception/       # Excepciones personalizadas
├── repository/      # Repositorios JPA
└── service/         # Lógica de negocio
```

### Frontend
```
frontend/src/app/
├── components/
│   ├── admin/       # Componentes de administración
│   ├── auth/        # Componentes de autenticación
│   ├── cliente/     # Componentes de cliente
│   ├── constants/   # Constantes y endpoints
│   ├── models/      # Modelos TypeScript
│   ├── public/      # Componentes públicos
│   └── shared/      # Componentes compartidos
├── core/
│   ├── guards/      # Protección de rutas
│   ├── interceptors/# Interceptores HTTP
│   └── utils/       # Utilidades
├── services/        # Servicios Angular
└── environments/    # Configuración de entornos
```

---

## Próximos Pasos Sugeridos

1. **Testing**: Implementar tests unitarios e integración
2. **Documentación API**: Swagger/OpenAPI
3. **Optimización**: Lazy loading de módulos
4. **Seguridad**: Auditoría de seguridad
5. **Performance**: Optimización de queries
6. **Deployment**: Configuración de CI/CD
7. **Monitoreo**: Logging y métricas

---

## Notas Importantes

- La rama contiene **archivos binarios** (imágenes JPG/PNG) en `backend/src/main/resources/static/uploads/`
- Hay archivos vacíos marcadores (`sd`, `adsa`) que probablemente deben eliminarse
- Algunos archivos de constantes y modelos fueron creados vacíos en commits anteriores y poblados después
- La refactorización de `model/` a `entity/` indica una mejora en la arquitectura
- El sistema está diseñado para manejar dos tipos de clientes: Persona Natural y Persona Jurídica

---

**Fecha de documentación**: 2025-11-17
**Generado por**: Claude Code
**Rama base de comparación**: `main`
