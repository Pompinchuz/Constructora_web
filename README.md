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
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

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
