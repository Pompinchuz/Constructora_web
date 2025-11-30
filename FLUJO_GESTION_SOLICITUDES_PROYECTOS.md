# Flujo de Gestión de Solicitudes y Aprobación de Proyectos

## 📋 Resumen

Este documento describe el flujo completo de gestión de solicitudes de proforma y aprobación de proyectos en el sistema de la constructora.

## 🔄 Flujo Completo

### 1. Cliente Envía Solicitud

**URL**: `http://localhost:4200/cliente/nueva-solicitud`

**Proceso**:
- El cliente llena el formulario con:
  - **Título** (obligatorio): Nombre del proyecto
  - **Descripción** (obligatorio): Descripción detallada del proyecto
  - **Archivo Adjunto** (opcional): Imagen o PDF (máximo 10MB)
- Al enviar, la solicitud se crea con estado `PENDIENTE`
- Se almacena en la base de datos
- Se envía notificación por email a los administradores

**Endpoint Backend**: `POST /api/solicitudes`

---

### 2. Administrador Revisa Solicitudes

**URL**: `http://localhost:4200/admin/solicitudes`

**Proceso**:
- El administrador puede ver todas las solicitudes
- Puede filtrar por estado (Pendiente, En Revisión, Aprobada, Rechazada)
- Para cada solicitud puede:
  - Ver detalles completos
  - Aprobar la solicitud
  - Rechazar la solicitud (con motivo)
  - Cambiar el estado manualmente

**Endpoint Backend**: `GET /api/solicitudes/admin/todas`

---

### 3. Aprobación de Solicitud (AUTOMÁTICO)

**URL Admin**: `http://localhost:4200/admin/solicitudes/{id}`

**Proceso Automático**:
Cuando el administrador **aprueba** una solicitud:

1. ✅ Se cambia el estado de la solicitud a `APROBADA`
2. ✅ **Se crea automáticamente un ProyectoExitoso** con los siguientes datos:
   - **Nombre**: Título de la solicitud
   - **Descripción**: Descripción de la solicitud
   - **Cliente**: El cliente que hizo la solicitud
   - **Estado de Aprobación**: `PENDIENTE_APROBACION`
   - **Imagen Principal**: Si el archivo adjunto de la solicitud es una imagen (.jpg, .jpeg, .png)
   - **Activo**: `false` (no visible públicamente hasta que el cliente apruebe)
   - **Fecha Solicitud Aprobación**: Fecha actual

3. ✅ Se asocia el proyecto a la solicitud
4. ✅ **Se envía email al cliente** notificándole que:
   - Su solicitud fue aprobada
   - Se ha creado un proyecto
   - Debe ingresar a su panel para aprobar la publicación del proyecto

**Endpoint Backend**: `PATCH /api/solicitudes/{id}/estado?estado=APROBADA`

**Código Backend**: `SolicitudProformaService.java:132-160`

---

### 4. Cliente Aprueba/Rechaza Publicación del Proyecto

**URL**: `http://localhost:4200/cliente/aprobacion-proyectos`

**Dashboard del Cliente**:
- En el dashboard principal (`/cliente/dashboard`) el cliente ve:
  - Contador de "Proyectos por Aprobar" (en naranja)
  - Botón destacado "Aprobar Proyectos" con badge si hay pendientes

**Vista de Aprobación de Proyectos**:
- **Pestaña "Pendientes de Aprobación"**: Muestra proyectos en estado `PENDIENTE_APROBACION`
- **Pestaña "Todos Mis Proyectos"**: Muestra todos los proyectos asociados al cliente

**Opciones del Cliente**:

#### Opción A: Aprobar el Proyecto
- Presiona botón "Aprobar Publicación"
- **Resultado**:
  - Estado del proyecto → `APROBADO`
  - **Activo** → `true` (✅ **SE PUBLICA AUTOMÁTICAMENTE**)
  - Fecha Respuesta Cliente → Fecha actual
  - El proyecto ahora es visible en la página pública

**Endpoint Backend**: `POST /api/contenido/proyectos/{id}/aprobar`

#### Opción B: Rechazar el Proyecto
- Presiona botón "Rechazar"
- Completa un motivo de rechazo
- **Resultado**:
  - Estado del proyecto → `RECHAZADO`
  - **Activo** → `false` (permanece oculto)
  - Motivo de Rechazo → Se guarda el motivo
  - Fecha Respuesta Cliente → Fecha actual

**Endpoint Backend**: `POST /api/contenido/proyectos/{id}/rechazar`

---

### 5. Publicación Automática (CUANDO CLIENTE APRUEBA)

**Proceso Automático en Backend**:

Cuando el cliente aprueba un proyecto (`ContenidoService.java:363-394`):
```java
// Aprobar el proyecto
proyecto.setEstadoAprobacion(EstadoAprobacionProyecto.APROBADO);
proyecto.setFechaRespuestaCliente(LocalDateTime.now());
proyecto.setActivo(true);  // ✅ PUBLICACIÓN AUTOMÁTICA
proyecto.setMotivoRechazo(null);
```

**Resultado**:
- El proyecto ahora aparece en:
  - Página pública de proyectos (`http://localhost:4200/proyectos`)
  - Endpoint público: `GET /api/contenido/proyectos/publico`

---

## 📊 Estados del Sistema

### Estados de Solicitud (`EstadoSolicitud`)
- `PENDIENTE`: Solicitud nueva sin revisar
- `EN_REVISION`: Solicitud siendo revisada
- `APROBADA`: Solicitud aprobada (✅ crea proyecto automáticamente)
- `RECHAZADA`: Solicitud rechazada

### Estados de Aprobación de Proyecto (`EstadoAprobacionProyecto`)
- `PENDIENTE_APROBACION`: Esperando que el cliente autorice la publicación
- `APROBADO`: Cliente autorizó (✅ se publica automáticamente)
- `RECHAZADO`: Cliente rechazó la publicación

---

## 🗄️ Relaciones en Base de Datos

### Tabla `solicitudes_proforma`
```sql
- id
- clienteId (FK → clientes)
- titulo
- descripcion
- archivoAdjunto
- estado (ENUM)
- motivoRechazo
- fechaSolicitud
- fechaRevision
- revisadoPor (FK → administradores)
- proyectoId (FK → proyectos_exitosos)  -- ✅ NUEVA RELACIÓN
```

### Tabla `proyectos_exitosos`
```sql
- id
- nombre
- descripcion
- ubicacion
- fechaInicio
- fechaFinalizacion
- imagenPrincipal
- activo (BOOLEAN) -- ✅ true = visible públicamente
- clienteId (FK → clientes)
- estadoAprobacion (ENUM)
- motivoRechazo
- fechaSolicitudAprobacion
- fechaRespuestaCliente
```

---

## 📧 Notificaciones por Email

### Email 1: Nueva Solicitud → Admin
- **Trigger**: Cliente crea una solicitud
- **Destinatario**: Administrador(es)
- **Contenido**: Notificación de nueva solicitud pendiente de revisión

### Email 2: Solicitud Rechazada → Cliente
- **Trigger**: Admin rechaza una solicitud
- **Destinatario**: Cliente
- **Contenido**: Notificación de rechazo con motivo

### Email 3: Proyecto Pendiente de Aprobación → Cliente
- **Trigger**: Admin aprueba una solicitud (✅ se crea proyecto automáticamente)
- **Destinatario**: Cliente
- **Contenido**:
  - Notificación de aprobación de solicitud
  - Información del proyecto creado
  - Link a la sección de aprobación de proyectos
  - Solicitud de autorización para publicar

**Método**: `EmailService.notificarProyectoPendienteAprobacion()`

---

## 🎨 Interfaz de Usuario

### Para el Cliente:

1. **Dashboard** (`/cliente/dashboard`)
   - Contador de "Proyectos por Aprobar"
   - Botón destacado con badge si hay pendientes

2. **Nueva Solicitud** (`/cliente/nueva-solicitud`)
   - Formulario con título, descripción y archivo adjunto
   - Validación de campos obligatorios
   - Carga de archivos hasta 10MB

3. **Aprobación de Proyectos** (`/cliente/aprobacion-proyectos`)
   - Vista de proyectos pendientes en cards
   - Botones "Aprobar" y "Rechazar"
   - Modal para ingresar motivo de rechazo
   - Historial de todos los proyectos

### Para el Administrador:

1. **Lista de Solicitudes** (`/admin/solicitudes`)
   - Filtro por estado
   - Cards con información resumida
   - Botones de acción rápida

2. **Detalle de Solicitud** (`/admin/solicitudes/{id}`)
   - Información completa de la solicitud
   - Selector de estado
   - Descarga de archivo adjunto
   - Modal de confirmación para cambios

---

## ✅ Validaciones y Seguridad

### Backend:
- ✅ Validación de pertenencia: El cliente solo puede aprobar/rechazar sus propios proyectos
- ✅ Validación de estado: Solo se pueden aprobar/rechazar proyectos en estado `PENDIENTE_APROBACION`
- ✅ Validación de archivos: Whitelist de extensiones permitidas
- ✅ Tamaño máximo de archivo: 10MB
- ✅ Validación de roles: Endpoints protegidos por rol (Admin/Cliente)

### Frontend:
- ✅ Validación de formularios reactivos
- ✅ Mensajes de error claros
- ✅ Confirmaciones antes de acciones críticas
- ✅ Manejo de estados de carga

---

## 📝 Ejemplo de Flujo Completo

1. Juan (cliente) crea solicitud "Construcción de Casa de 2 Pisos"
   - Estado: `PENDIENTE`
   - Admin recibe email

2. María (admin) revisa y aprueba la solicitud
   - Estado solicitud: `APROBADA`
   - ✅ **Se crea automáticamente ProyectoExitoso "Construcción de Casa de 2 Pisos"**
   - Estado proyecto: `PENDIENTE_APROBACION`, Activo: `false`
   - Juan recibe email pidiendo autorización

3. Juan ingresa a su panel → "Aprobación de Proyectos"
   - Ve el proyecto pendiente con imagen y descripción
   - Presiona "Aprobar Publicación"

4. **Sistema publica automáticamente**:
   - Estado proyecto: `APROBADO`
   - Activo: `true`
   - ✅ **El proyecto ahora es visible en la página pública**

---

## 🚀 Endpoints de API

### Solicitudes:
- `POST /api/solicitudes` - Crear solicitud (Cliente)
- `GET /api/solicitudes/mis-solicitudes` - Ver mis solicitudes (Cliente)
- `GET /api/solicitudes/admin/todas` - Ver todas (Admin)
- `PATCH /api/solicitudes/{id}/estado` - Cambiar estado (Admin)

### Proyectos:
- `GET /api/contenido/proyectos/cliente/pendientes` - Proyectos pendientes (Cliente)
- `GET /api/contenido/proyectos/cliente/mis-proyectos` - Todos mis proyectos (Cliente)
- `POST /api/contenido/proyectos/{id}/aprobar` - Aprobar proyecto (Cliente)
- `POST /api/contenido/proyectos/{id}/rechazar` - Rechazar proyecto (Cliente)
- `GET /api/contenido/proyectos/publico` - Proyectos públicos (Todos)

---

## 🔧 Archivos Modificados

### Backend:
- `SolicitudProforma.java` - Agregada relación con ProyectoExitoso
- `SolicitudProformaService.java` - Creación automática de proyecto al aprobar
- `EmailService.java` - Nuevo método de notificación

### Frontend:
- `cliente-dashboard.component.*` - Ya implementado con contador de proyectos
- `aprobacion-proyectos.component.*` - Ya implementado
- `proyecto.service.ts` - Ya implementado con métodos de aprobación

---

## 📌 Notas Importantes

1. **Imagen del proyecto**: Si la solicitud tiene un archivo adjunto de tipo imagen, se usa automáticamente como imagen principal del proyecto.

2. **Sin intervención manual**: Una vez que el cliente aprueba, el proyecto se publica automáticamente sin necesidad de intervención del administrador.

3. **Trazabilidad completa**: Todas las acciones quedan registradas con fechas y usuarios.

4. **Notificaciones asíncronas**: Los emails se envían de forma asíncrona para no bloquear las operaciones.

---

## 📖 Resumen del Problema Resuelto

**Problema Inicial**:
- Faltaba conexión entre solicitud aprobada y creación de proyecto
- No había flujo de autorización del cliente para publicar proyectos
- No se publicaban automáticamente los proyectos aprobados por el cliente

**Solución Implementada**:
✅ Creación automática de proyecto al aprobar solicitud
✅ Flujo de autorización con notificaciones al cliente
✅ Publicación automática cuando el cliente autoriza
✅ Dashboard con indicadores visuales de proyectos pendientes
✅ Trazabilidad completa del proceso

---

**Fecha de Implementación**: Noviembre 2025
**Rama**: `claude/request-admin-workflow-01E6UqxfEU1vdDVXRcPPuvZH`
**Estado**: ✅ Implementado y Documentado
