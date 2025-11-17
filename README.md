<<<<<<< HEAD
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

=======
# Sistema de Gestión para Constructora

Sistema web completo para la gestión de una empresa constructora, que incluye gestión de proyectos, proformas, solicitudes de clientes y administración de contenido web.

## Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos relacional
- **JWT (JSON Web Tokens)** - Autenticación basada en tokens
- **Thymeleaf** - Motor de plantillas
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

### Frontend
- **Angular 19.2**
- **TypeScript 5.7**
- **Angular Material** - Componentes UI
- **RxJS** - Programación reactiva
- **Express** - Servidor SSR
- **Angular SSR** - Server-Side Rendering

## Estructura del Proyecto

```
Constructora_web/
├── backend/                    # Aplicación Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/constructora/backend/
│   │   │   │   ├── config/           # Configuración (Security, JWT, etc.)
│   │   │   │   ├── controller/       # Controladores REST
│   │   │   │   ├── model/            # Modelos de datos
│   │   │   │   ├── repository/       # Repositorios JPA
│   │   │   │   ├── security/         # Seguridad y JWT
│   │   │   │   └── service/          # Lógica de negocio
│   │   │   └── resources/
│   │   │       ├── application.properties  # Configuración
│   │   │       └── templates/        # Plantillas Thymeleaf
│   │   └── test/
│   └── pom.xml
│
└── frontend/                   # Aplicación Angular
    ├── src/
    │   ├── app/
    │   │   ├── components/           # Componentes Angular
    │   │   ├── guards/               # Protección de rutas
    │   │   ├── interceptors/         # Interceptores HTTP
    │   │   ├── models/               # Modelos TypeScript
    │   │   └── services/             # Servicios Angular
    │   ├── assets/                   # Recursos estáticos
    │   └── environments/             # Configuración de entornos
    └── package.json
```

## Requisitos Previos

- **Java JDK 17** o superior
- **Node.js 18** o superior
- **npm** o **yarn**
- **MySQL 8.0** o superior
- **Maven 3.6** o superior

## Configuración

### Base de Datos

1. Crear una base de datos MySQL:
```sql
CREATE DATABASE constructora_db;
```

2. Configurar credenciales en `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/constructora_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

### Backend

1. Navegar al directorio del backend:
```bash
cd backend
```

2. Instalar dependencias y compilar:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

### Frontend

1. Navegar al directorio del frontend:
```bash
cd frontend
```

2. Instalar dependencias:
```bash
npm install
```

3. Ejecutar en modo desarrollo:
```bash
npm start
```

El frontend estará disponible en `http://localhost:4200`

## Características Principales

### Autenticación y Seguridad
- Sistema de autenticación basado en JWT
- Roles de usuario (Administrador, Cliente)
- Protección de rutas por roles
- Encriptación de contraseñas con BCrypt

### Gestión de Clientes
- Registro de clientes (Persona Natural / Persona Jurídica)
- Perfil de usuario
- Historial de solicitudes

### Gestión de Proyectos
- Galería de proyectos exitosos
- Administración de imágenes
- Descripción y detalles de proyectos

### Sistema de Proformas
- Creación y gestión de proformas
- Aprobación/rechazo de proformas
- Seguimiento de estados

### Gestión de Solicitudes
- Solicitudes de presupuesto
- Workflow de estados
- Comunicación cliente-administrador

### Panel de Administración
- Dashboard con estadísticas
- Gestión de contenido web
- Gestión de usuarios
- Gestión de solicitudes y proformas

## Endpoints Principales del API

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/logout` - Cerrar sesión

### Usuarios
- `GET /api/users` - Listar usuarios
- `GET /api/users/{id}` - Obtener usuario
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario

## Scripts Disponibles

### Backend
```bash
mvn clean           # Limpiar build
mvn test            # Ejecutar tests
mvn package         # Empaquetar aplicación
mvn spring-boot:run # Ejecutar aplicación
```

### Frontend
```bash
npm start           # Servidor de desarrollo
npm run build       # Build de producción
npm test            # Ejecutar tests
npm run watch       # Build en modo watch
```

## Variables de Entorno

### Backend
Crear archivo `.env` o configurar en `application.properties`:
```properties
# Database
DB_URL=jdbc:mysql://localhost:3306/constructora_db
DB_USERNAME=root
DB_PASSWORD=password

# JWT
JWT_SECRET=tu_clave_secreta_aqui
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

### Frontend
Configurar en `src/environments/environment.ts`:
>>>>>>> e1369d82a9456dbd6e7c4ff7c06bb0d855800b1a
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

<<<<<<< HEAD
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
=======
## Despliegue

### Backend
```bash
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm run build
# Los archivos estarán en dist/frontend/browser/
```

## Ramas del Proyecto

- **main** - Rama principal estable
- **modelyisus** - Rama de desarrollo con características avanzadas
- **feature/jhonatan** - Rama de características
- **feature/jwt-auth** - Implementación de autenticación JWT

Para más información sobre la rama `modelyisus`, consulta [DOCUMENTACION_RAMA_MODELYISUS.md](./DOCUMENTACION_RAMA_MODELYISUS.md)

## Solución de Problemas

### El backend no inicia
- Verificar que MySQL esté corriendo
- Verificar credenciales en `application.properties`
- Verificar que el puerto 8080 esté disponible

### El frontend no inicia
- Ejecutar `npm install` nuevamente
- Limpiar cache: `npm cache clean --force`
- Eliminar `node_modules` y reinstalar

### Errores de CORS
- Verificar configuración de CORS en el backend
- Verificar que las URLs coincidan en frontend/backend

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto es privado y confidencial.

## Contacto

Desarrollado por el equipo de Constructora

---

**Última actualización**: Noviembre 2025
>>>>>>> e1369d82a9456dbd6e7c4ff7c06bb0d855800b1a
