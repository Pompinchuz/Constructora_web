# ✅ Checklist de Despliegue

## Lista de Verificación Completa para Deployment

---

## 📋 Fase 1: Preparación (ANTES de Desplegar)

### Cuentas y Accesos
- [ ] Cuenta de GitHub creada y verificada
- [ ] Repositorio pusheado a GitHub
- [ ] Cuenta de Railway creada (https://railway.app/)
- [ ] Cuenta de Netlify creada (https://www.netlify.com/)
- [ ] Cuenta de Gmail con 2FA activada

### Código y Builds
- [ ] Backend compila sin errores: `mvn clean package`
- [ ] Frontend compila sin errores: `npm run build`
- [ ] Todos los tests pasan (si los hay)
- [ ] No hay errores en consola del navegador
- [ ] Código commiteado y pusheado a GitHub

### Archivos de Configuración Creados
- [ ] `backend/railway.json`
- [ ] `backend/nixpacks.toml`
- [ ] `backend/.railwayignore`
- [ ] `backend/src/main/resources/application-prod.properties`
- [ ] `frontend/netlify.toml`
- [ ] `frontend/_redirects`
- [ ] `frontend/.nvmrc`
- [ ] `frontend/src/environments/environment.prod.ts` actualizado

---

## 🔐 Fase 2: Información Sensible (PREPARAR)

### JWT Secret
- [ ] Generar JWT Secret aleatorio de 64+ caracteres
  ```bash
  # Opción 1 (Linux/Mac):
  openssl rand -base64 64

  # Opción 2 (Node.js):
  node -e "console.log(require('crypto').randomBytes(64).toString('base64'))"

  # Opción 3 (Online):
  # https://randomkeygen.com/
  ```
- [ ] Guardar en lugar seguro (no en el código)

### Email SMTP
- [ ] Email configurado (Gmail recomendado)
- [ ] 2FA activado en Gmail
- [ ] App Password generado desde Gmail
  - Ir a: https://myaccount.google.com/
  - Seguridad → Verificación en dos pasos → Contraseñas de aplicaciones
- [ ] Email del administrador definido

### Variables de Entorno Preparadas
Copiar este template y llenar:

```env
# Railway Backend
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=[TU_JWT_SECRET_AQUI]
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=[tu-email@gmail.com]
MAIL_PASSWORD=[app-password-de-16-caracteres]
ADMIN_EMAIL=[admin@tuempresa.com]
APP_URL=[se-actualiza-despues]
CORS_ORIGINS=[se-actualiza-despues]
UPLOAD_DIR=/app/uploads
APP_NAME=Constructora
```

---

## 🚂 Fase 3: Despliegue Backend (Railway)

### Crear Proyecto
- [ ] Login en Railway con GitHub
- [ ] New Project → Deploy from GitHub repo
- [ ] Seleccionar repositorio `Constructora_web`
- [ ] Root directory configurado: `backend`

### Agregar MySQL
- [ ] Click en "+ New" en el proyecto
- [ ] Seleccionar "Database" → "Add MySQL"
- [ ] Verificar que se crearon variables automáticas:
  - `MYSQL_URL`
  - `MYSQL_USER`
  - `MYSQL_PASSWORD`
  - `MYSQL_DATABASE`

### Configurar Variables
- [ ] Agregar `SPRING_PROFILES_ACTIVE=prod`
- [ ] Agregar `JWT_SECRET`
- [ ] Agregar `MAIL_HOST`
- [ ] Agregar `MAIL_PORT`
- [ ] Agregar `MAIL_USERNAME`
- [ ] Agregar `MAIL_PASSWORD`
- [ ] Agregar `ADMIN_EMAIL`
- [ ] Agregar `APP_URL` (temporal, actualizar después)
- [ ] Agregar `CORS_ORIGINS` (temporal, actualizar después)
- [ ] Agregar `UPLOAD_DIR=/app/uploads`
- [ ] Agregar `APP_NAME=Constructora`

### Deploy y Verificación
- [ ] Click en "Deploy"
- [ ] Esperar build (5-10 minutos)
- [ ] Verificar logs sin errores críticos
- [ ] Copiar URL pública de Railway
- [ ] Probar health check: `https://tu-backend.railway.app/actuator/health`
- [ ] Debe responder: `{"status":"UP"}`

---

## 🌐 Fase 4: Despliegue Frontend (Netlify)

### Actualizar Environment
- [ ] Abrir `frontend/src/environments/environment.prod.ts`
- [ ] Actualizar `apiUrl` con URL de Railway
- [ ] Actualizar `uploadUrl` con URL de Railway + `/uploads`
- [ ] Guardar cambios
- [ ] Commit y push:
  ```bash
  git add frontend/src/environments/environment.prod.ts
  git commit -m "Actualizar URLs de producción"
  git push
  ```

### Crear Sitio
- [ ] Login en Netlify con GitHub
- [ ] "Add new site" → "Import an existing project"
- [ ] Conectar con GitHub
- [ ] Seleccionar repositorio `Constructora_web`

### Configurar Build
- [ ] Base directory: `frontend`
- [ ] Build command: `npm run build`
- [ ] Publish directory: `dist/frontend/browser`
- [ ] Node version: `20` (opcional)

### Deploy y Verificación
- [ ] Click en "Deploy site"
- [ ] Esperar build (5-10 minutos)
- [ ] Verificar logs sin errores
- [ ] Copiar URL de Netlify
- [ ] Abrir sitio en navegador
- [ ] Verificar que carga correctamente

---

## 🔄 Fase 5: Actualizar URLs Cruzadas

### Actualizar Backend (Railway)
- [ ] Ir a Variables en Railway
- [ ] Actualizar `APP_URL` con URL de Netlify
- [ ] Actualizar `CORS_ORIGINS` con URL de Netlify
  ```
  CORS_ORIGINS=https://tu-app.netlify.app
  ```
- [ ] Railway redesplegará automáticamente
- [ ] Esperar redespliegue (2-3 minutos)

### Verificar Conexión
- [ ] Abrir sitio de Netlify
- [ ] Abrir DevTools (F12) → Console
- [ ] Intentar login
- [ ] No debe haber errores de CORS
- [ ] Backend debe responder correctamente

---

## 👤 Fase 6: Configuración Inicial

### Crear Usuario Administrador
Opción A - API directa:
```bash
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

Opción B - Desde la interfaz:
- [ ] Ir a tu sitio de Netlify
- [ ] Click en "Registrarse"
- [ ] Crear cuenta de administrador
- [ ] Verificar que funciona

### Probar Funcionalidades
- [ ] Login de administrador
- [ ] Login de cliente
- [ ] Registro de nuevo cliente
- [ ] Creación de solicitud
- [ ] Subida de archivo en solicitud
- [ ] Email de notificación recibido
- [ ] Aprobación de solicitud por admin
- [ ] Creación automática de proyecto
- [ ] Email de aprobación recibido por cliente
- [ ] Aprobación de proyecto por cliente
- [ ] Proyecto visible en página pública

---

## 🔒 Fase 7: Seguridad Post-Despliegue

### Verificación de Seguridad
- [ ] HTTPS habilitado en ambos servicios (automático)
- [ ] JWT Secret es único y NO está en el código
- [ ] App Password usado (no contraseña real de Gmail)
- [ ] CORS solo permite tu dominio de Netlify
- [ ] Variables sensibles solo en variables de entorno
- [ ] Logs no muestran contraseñas o tokens

### Configuración SSL
- [ ] Railway: SSL automático ✅
- [ ] Netlify: SSL automático ✅
- [ ] Verificar candado verde en navegador

---

## 📊 Fase 8: Monitoreo

### Configurar Monitoreo
- [ ] Verificar logs de Railway funcionando
- [ ] Verificar logs de Netlify funcionando
- [ ] Agregar a favoritos:
  - Dashboard de Railway
  - Dashboard de Netlify
  - Email admin configurado

### Opcional (Recomendado)
- [ ] Configurar UptimeRobot para monitoreo 24/7
- [ ] Configurar alertas de email si sitio cae
- [ ] Configurar Google Analytics
- [ ] Configurar Sentry para tracking de errores

---

## 🎨 Fase 9: Personalización (Opcional)

### Dominio Personalizado
- [ ] Comprar dominio (GoDaddy, Namecheap, etc.)
- [ ] Configurar DNS en Netlify
- [ ] Actualizar `APP_URL` en Railway
- [ ] Actualizar `CORS_ORIGINS` en Railway
- [ ] Verificar SSL del nuevo dominio

### Branding
- [ ] Actualizar favicon
- [ ] Actualizar meta tags
- [ ] Actualizar título de página
- [ ] Actualizar logo si aplica

---

## 📝 Fase 10: Documentación

### Documentar Despliegue
- [ ] Guardar URLs de producción:
  - Backend: `___________________________`
  - Frontend: `___________________________`
- [ ] Guardar credenciales de admin:
  - Email: `___________________________`
  - Password: `___________________________` (guardar seguro)
- [ ] Documentar variables de entorno usadas
- [ ] Crear documento con procedimientos de mantenimiento

---

## ✅ Verificación Final

### Checklist de Funcionalidad
- [ ] ✅ Sitio carga en navegador
- [ ] ✅ No hay errores en consola
- [ ] ✅ Login funciona
- [ ] ✅ Registro funciona
- [ ] ✅ API responde correctamente
- [ ] ✅ Base de datos conectada
- [ ] ✅ Emails se envían
- [ ] ✅ Archivos se suben
- [ ] ✅ Todas las páginas funcionan
- [ ] ✅ Responsive funciona en móvil

### Checklist de Rendimiento
- [ ] ✅ Sitio carga en menos de 3 segundos
- [ ] ✅ API responde en menos de 2 segundos
- [ ] ✅ Imágenes optimizadas
- [ ] ✅ Sin errores 404
- [ ] ✅ Sin errores 500

### Checklist de Seguridad
- [ ] ✅ HTTPS habilitado
- [ ] ✅ CORS configurado correctamente
- [ ] ✅ JWT funcionando
- [ ] ✅ Contraseñas hasheadas
- [ ] ✅ Variables sensibles protegidas

---

## 🚨 En Caso de Problemas

### Recursos de Ayuda
1. **Logs de Railway**: Ver errores del backend
2. **Logs de Netlify**: Ver errores del frontend
3. **Consola del navegador**: Ver errores de JavaScript
4. **Network tab**: Ver requests fallidos

### Contactos de Soporte
- Railway Discord: https://discord.gg/railway
- Netlify Support: https://www.netlify.com/support/
- Documentación de Railway: https://docs.railway.app/
- Documentación de Netlify: https://docs.netlify.com/

---

## 🎉 ¡Despliegue Completado!

Si marcaste todas las casillas, tu aplicación está **funcionando en producción** 🚀

**URLs Finales**:
- Frontend: `https://_________________________.netlify.app`
- Backend: `https://_________________________.railway.app`

**Próximos pasos**:
1. Monitorear logs primeros días
2. Probar con usuarios reales
3. Configurar backups regulares
4. Considerar CDN para archivos estáticos
5. Configurar analytics

---

**Fecha de Despliegue**: _______________
**Versión Desplegada**: 1.0.0
**Desplegado por**: _______________

✅ **CHECKLIST COMPLETADO**
