# 🚀 Guía Completa de Despliegue

## Sistema de Gestión de Constructora
**Backend**: Railway + MySQL
**Frontend**: Netlify

---

## 📋 Tabla de Contenidos

1. [Requisitos Previos](#requisitos-previos)
2. [Despliegue del Backend en Railway](#despliegue-del-backend-en-railway)
3. [Despliegue del Frontend en Netlify](#despliegue-del-frontend-en-netlify)
4. [Configuración Post-Despliegue](#configuración-post-despliegue)
5. [Solución de Problemas](#solución-de-problemas)

---

## 📦 Requisitos Previos

### ✅ Cuentas Necesarias

- [ ] Cuenta de GitHub (para ambos servicios)
- [ ] Cuenta de Railway: https://railway.app/
- [ ] Cuenta de Netlify: https://www.netlify.com/
- [ ] Cuenta de Gmail (para email SMTP)

### ✅ Configuraciones Locales

- [ ] Código pusheado a GitHub en la rama principal
- [ ] Backend compilando correctamente (`mvn clean package`)
- [ ] Frontend compilando correctamente (`npm run build`)
- [ ] Variables de entorno identificadas

### ✅ Información que Necesitarás

- [ ] **JWT Secret**: Cadena aleatoria de al menos 64 caracteres
- [ ] **Email SMTP**: Gmail u otro proveedor
  - Email: `tu-email@gmail.com`
  - App Password: Generada desde configuración de Gmail
- [ ] **Admin Email**: Email del administrador del sistema

---

## 🚂 Despliegue del Backend en Railway

### Paso 1: Crear Proyecto en Railway

1. **Ir a Railway**: https://railway.app/
2. **Login con GitHub**
3. **New Project** → **Deploy from GitHub repo**
4. **Seleccionar tu repositorio**: `Constructora_web`
5. **Configure el servicio**:
   - Root Directory: `backend`
   - Start Command: (se auto-detecta con `railway.json`)

### Paso 2: Agregar Base de Datos MySQL

1. En tu proyecto de Railway, click en **+ New**
2. Seleccionar **Database** → **Add MySQL**
3. Railway creará automáticamente la base de datos y las variables:
   - `MYSQL_URL`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`
   - `MYSQL_DATABASE`

### Paso 3: Configurar Variables de Entorno

En Railway, ir a tu servicio backend → **Variables**:

```bash
# ============================================
# VARIABLES OBLIGATORIAS
# ============================================

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# JWT Configuration (GENERAR UNO NUEVO)
JWT_SECRET=TU-SECRETO-SUPER-LARGO-Y-ALEATORIO-DE-AL-MENOS-64-CARACTERES

# Email Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=tu-email@gmail.com
MAIL_PASSWORD=tu-app-password-de-gmail
ADMIN_EMAIL=admin@tuconstructora.com

# Application URLs (cambiar después del deploy)
APP_URL=https://tu-app.netlify.app
CORS_ORIGINS=https://tu-app.netlify.app,http://localhost:4200

# Uploads Directory
UPLOAD_DIR=/app/uploads

# ============================================
# VARIABLES OPCIONALES
# ============================================
APP_NAME=Constructora

# ============================================
# VARIABLES AUTO-GENERADAS POR RAILWAY (NO TOCAR)
# ============================================
# MYSQL_URL
# MYSQL_USER
# MYSQL_PASSWORD
# MYSQL_DATABASE
# PORT
```

### Paso 4: Generar JWT Secret Seguro

**Opción 1 - Online**:
```
https://randomkeygen.com/ (usar "CodeIgniter Encryption Keys")
```

**Opción 2 - Linux/Mac**:
```bash
openssl rand -base64 64
```

**Opción 3 - Node.js**:
```bash
node -e "console.log(require('crypto').randomBytes(64).toString('base64'))"
```

### Paso 5: Configurar Gmail App Password

1. Ir a: https://myaccount.google.com/
2. **Seguridad** → **Verificación en dos pasos** (activar si no está)
3. **Contraseñas de aplicaciones**
4. Crear nueva para "Mail"
5. Copiar la contraseña de 16 caracteres
6. Usar en `MAIL_PASSWORD`

### Paso 6: Deploy

1. Railway detectará los cambios automáticamente
2. Click en **Deploy**
3. Esperar a que termine el build (5-10 minutos)
4. **Copiar la URL pública** (ej: `https://tu-backend.railway.app`)

### Paso 7: Verificar Deploy del Backend

```bash
# Verificar health check
curl https://tu-backend.railway.app/actuator/health

# Debe responder:
{"status":"UP"}
```

---

## 🌐 Despliegue del Frontend en Netlify

### Paso 1: Preparar el Frontend

1. **Actualizar** `frontend/src/environments/environment.prod.ts`:

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://TU-BACKEND.railway.app',  // ← URL de Railway
  uploadUrl: 'https://TU-BACKEND.railway.app/uploads',
  appName: 'Constructora Sistema de Proformas',
  version: '1.0.0',
  enableDebugLogs: false,
  httpTimeout: 30000
};
```

2. **Commit y push** los cambios:

```bash
git add frontend/src/environments/environment.prod.ts
git commit -m "Actualizar URLs de producción"
git push
```

### Paso 2: Crear Sitio en Netlify

1. **Ir a Netlify**: https://app.netlify.com/
2. **Login con GitHub**
3. **Add new site** → **Import an existing project**
4. **Connect to Git provider** → **GitHub**
5. **Seleccionar** `Constructora_web`

### Paso 3: Configurar Build Settings

En la configuración de despliegue:

```
Base directory: frontend
Build command: npm run build
Publish directory: dist/frontend/browser
```

**Variables de entorno** (opcional):
```
NODE_VERSION=20
```

### Paso 4: Deploy

1. Click en **Deploy site**
2. Netlify comenzará el build (5-10 minutos)
3. **Copiar la URL** generada (ej: `https://random-name-123.netlify.app`)

### Paso 5: Configurar Dominio Personalizado (Opcional)

1. En Netlify → **Domain settings**
2. **Add custom domain**
3. Seguir instrucciones para configurar DNS

### Paso 6: Actualizar CORS en Backend

1. Ir a Railway → Variables
2. Actualizar `CORS_ORIGINS`:

```
CORS_ORIGINS=https://tu-app.netlify.app
```

3. Railway redesplegará automáticamente

---

## ⚙️ Configuración Post-Despliegue

### 1. Actualizar URLs Cruzadas

**En Railway** (Backend):
```bash
APP_URL=https://tu-app.netlify.app
CORS_ORIGINS=https://tu-app.netlify.app
```

**En Netlify** (Frontend):
- Ya actualizado en `environment.prod.ts`

### 2. Crear Usuario Administrador Inicial

**Opción A - Endpoint de registro**:
```bash
# POST a tu backend
curl -X POST https://tu-backend.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombreCompleto": "Admin Principal",
    "correoElectronico": "admin@tuempresa.com",
    "password": "Admin123!",
    "telefono": "987654321",
    "tipoCliente": "ADMINISTRADOR"
  }'
```

**Opción B - Directo en MySQL** (desde Railway):
1. Railway → MySQL service → **Data**
2. Ejecutar script SQL para crear admin

### 3. Probar Funcionalidades Críticas

- [ ] Login de usuario
- [ ] Registro de cliente
- [ ] Creación de solicitud
- [ ] Subida de archivos
- [ ] Envío de emails
- [ ] Aprobación de solicitudes
- [ ] Creación de proyectos

### 4. Configurar Backups (Recomendado)

Railway automáticamente hace backups, pero considera:
- Exportar datos periódicamente
- Usar servicio externo de storage para archivos (S3, Cloudinary)

---

## 🔧 Solución de Problemas

### Backend no Inicia

**Error**: `Application failed to start`

**Soluciones**:
1. Verificar logs en Railway
2. Verificar que `SPRING_PROFILES_ACTIVE=prod`
3. Verificar conexión a MySQL:
   ```bash
   # En Railway logs, buscar:
   "Failed to configure a DataSource"
   ```
4. Verificar variables `MYSQL_*` están presentes

### Frontend no Conecta con Backend

**Error**: `CORS error` o `Failed to fetch`

**Soluciones**:
1. Verificar `CORS_ORIGINS` en Railway incluye tu URL de Netlify
2. Verificar `environment.prod.ts` tiene la URL correcta
3. Verificar backend está corriendo (health check)

### Emails no se Envían

**Error**: `Authentication failed`

**Soluciones**:
1. Verificar App Password de Gmail (no la contraseña normal)
2. Verificar 2FA está activado en Gmail
3. Verificar `MAIL_USERNAME` y `MAIL_PASSWORD` correctos
4. Probar con otro email si Gmail no funciona

### Archivos no se Suben

**Error**: `413 Payload Too Large` o `File upload failed`

**Soluciones**:
1. Railway tiene límite de tamaño de archivos
2. Considerar usar servicio externo:
   - AWS S3
   - Cloudinary
   - UploadCare

### Error de JWT

**Error**: `Invalid token` o `Unauthorized`

**Soluciones**:
1. Verificar `JWT_SECRET` es el mismo en todas las instancias
2. Limpiar LocalStorage en frontend
3. Regenerar token desde login

---

## 📊 Monitoreo y Logs

### Ver Logs en Railway

```
Railway Dashboard → Tu servicio → Logs
```

Filtrar por:
- `ERROR` - Errores críticos
- `WARN` - Advertencias
- `INFO` - Información general

### Ver Logs en Netlify

```
Netlify Dashboard → Tu sitio → Deploys → [Deploy específico]
```

### Métricas de Railway

- CPU usage
- Memory usage
- Network traffic
- Database connections

---

## 🔐 Seguridad Post-Despliegue

### Checklist de Seguridad

- [ ] JWT Secret único y largo (>64 caracteres)
- [ ] HTTPS habilitado (automático en Railway y Netlify)
- [ ] CORS configurado solo con dominios permitidos
- [ ] Variables sensibles en variables de entorno (no en código)
- [ ] App Password de email (no contraseña real)
- [ ] Backups configurados
- [ ] Logs monitoreados
- [ ] Rate limiting considerado (futuro)

---

## 📝 Comandos Útiles

### Redeploy Forzado

**Railway**:
```bash
# En dashboard: Settings → Redeploy
```

**Netlify**:
```bash
# En dashboard: Deploys → Trigger deploy → Deploy site
```

### Rollback a Versión Anterior

**Railway**:
```
Deployments → [versión anterior] → Redeploy
```

**Netlify**:
```
Deploys → [deploy anterior] → Publish deploy
```

---

## 🎯 Próximos Pasos

Después del despliegue exitoso:

1. **Configurar dominio personalizado** (opcional)
2. **Configurar SSL certificate** (automático)
3. **Configurar analytics** (Google Analytics, etc.)
4. **Configurar monitoreo** (UptimeRobot, etc.)
5. **Configurar CI/CD** (auto-deploy en push)
6. **Migrar archivos a S3/Cloudinary** (recomendado)

---

## 📞 Soporte

### Railway
- Documentación: https://docs.railway.app/
- Discord: https://discord.gg/railway

### Netlify
- Documentación: https://docs.netlify.com/
- Support: https://www.netlify.com/support/

---

**Fecha de Creación**: Noviembre 2025
**Versión**: 1.0.0
**Autor**: Sistema de Constructora

---

✅ **¡Despliegue Completado!**

Una vez seguidos todos los pasos, tu aplicación estará funcionando en:
- Backend: `https://tu-backend.railway.app`
- Frontend: `https://tu-app.netlify.app`
