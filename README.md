# AAGG CONSTRUCTORA - Sistema de Gestión Web

## 📋 Descripción del Proyecto

Aplicación web full-stack para la gestión integral de una empresa constructora. Permite administrar proyectos, clientes, proformas, solicitudes y contenido web de manera eficiente.

## 🏗️ Arquitectura y Tecnologías

### Backend
- **Framework**: Spring Boot 3.5.7 (Java 21)
- **Base de Datos**: MySQL
- **Seguridad**: JWT Authentication
- **ORM**: JPA/Hibernate
- **Email**: JavaMail
- **File Upload**: Gestión de archivos

### Frontend
- **Framework**: Angular 19 con SSR
- **UI**: Angular Material + Tailwind CSS
- **Estado**: RxJS
- **HTTP**: Interceptors para auth y errores

## 🚀 Funcionalidades Principales

### 👥 Sistema de Usuarios
- Autenticación con JWT
- Roles: Admin, Cliente, Público
- Registro y login seguro

### 🏢 Gestión de Proyectos
- CRUD completo de proyectos
- Subida de imágenes
- Estados de proyecto

### 💰 Sistema de Proformas
- Generación de presupuestos
- Gestión de cotizaciones
- Estados de aprobación

### 📋 Solicitudes de Servicio
- Creación de solicitudes
- Seguimiento de estado
- Comunicación cliente-admin

### 💳 Comprobantes de Pago
- Subida de comprobantes
- Validación de pagos
- Historial de transacciones

### 🌐 Contenido Web
- Administración del sitio público
- Gestión de secciones
- Contenido dinámico

## 📁 Estructura del Proyecto

```
AAGG-Constructora/
├── backend/                    # Spring Boot Application
│   ├── src/main/java/com/constructora/backend/
│   │   ├── config/            # Configuraciones
│   │   ├── controller/        # REST Controllers
│   │   ├── entity/           # JPA Entities
│   │   ├── repository/       # Data Repositories
│   │   ├── service/          # Business Logic
│   │   └── util/             # Utilities
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── templates/email/
│   └── uploads/               # File uploads
├── frontend/                   # Angular Application
│   ├── src/app/
│   │   ├── components/        # UI Components
│   │   ├── services/          # API Services
│   │   ├── core/             # Guards & Interceptors
│   │   └── models/           # TypeScript Interfaces
│   ├── src/assets/           # Static Assets
│   └── src/environments/     # Environment Configs
├── docs/                      # Documentation
└── docker/                    # Docker Configuration
```

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Java 21
- Node.js 18+
- MySQL 8.0+
- Maven 3.9+

### Backend Setup
```bash
cd backend
mvn clean install
# Configurar application.properties con tu DB
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
ng serve
```

## 🔧 Configuración

### Base de Datos
Crear base de datos MySQL y configurar en `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aagg_constructora
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### Variables de Entorno
Configurar `frontend/src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## 🚀 Despliegue

### Desarrollo
- Backend: `http://localhost:8080`
- Frontend: `http://localhost:4200`

### Producción
- Configurar variables de entorno
- Build frontend: `ng build --configuration production`
- Build backend: `mvn clean package`

## 📚 API Endpoints

### Autenticación
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Registro

### Proyectos
- `GET /api/proyectos` - Listar proyectos
- `POST /api/proyectos` - Crear proyecto
- `PUT /api/proyectos/{id}` - Actualizar proyecto

### Proformas
- `GET /api/proformas` - Listar proformas
- `POST /api/proformas` - Crear proforma

### Más endpoints en la documentación completa...

## 🤝 Contribución

1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Equipo de Desarrollo

- **Desarrollador Principal**: Jhonatan Sánchez
- **Proyecto**: AAGG CONSTRUCTORA
- **Tecnologías**: Full-Stack Web Development

## 📞 Contacto

Para preguntas o soporte, contactar al equipo de desarrollo.

---

⭐ Si este proyecto te resulta útil, ¡dale una estrella en GitHub!
