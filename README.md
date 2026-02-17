# Linktic - Prueba Técnica Full Stack

Este repositorio contiene una aplicación Full Stack para la gestión de Productos e Inventario, construida con Java Spring Boot (Microservicios) y Vue.js.

## Arquitectura

El sistema consta de tres componentes principales:

1.  **Servicio de Productos** (Puerto `8081`): Gestiona el catálogo de productos.
2.  **Servicio de Inventario** (Puerto `8082`): Gestiona el stock y las compras.
3.  **Frontend** (Puerto `5173`): Aplicación Vue.js para la interacción del usuario.

E infraestructura:
-   **PostgreSQL**: Base de datos para ambos servicios.
-   **Docker Compose**: Orquesta la base de datos.

## Stack Tecnológico

-   **Backend**: Java 17, Spring Boot 3, Spring Data JPA, Hibernate, Resilience4j, Flyway.
-   **Frontend**: Vue 3, Vite, Pinia, TailwindCSS, TypeScript.
-   **Base de Datos**: PostgreSQL.

## Requisitos Previos

-   Java 17+
-   Node.js 20+
-   Docker y Docker Compose
-   Maven

## Primeros Pasos

### 1. Iniciar Infraestructura

Inicia la base de datos PostgreSQL:

```bash
docker-compose up -d
```

### 2. Iniciar Servicios Backend

**Servicio de Productos**:

```bash
cd products-service
mvn spring-boot:run
```

**Servicio de Inventario**:

```bash
cd inventory-service
mvn spring-boot:run
```

### 3. Iniciar Frontend

```bash
cd frontend
npm install
npm run dev
```

Accede a la aplicación en [http://localhost:5173](http://localhost:5173).

## Características Implementadas

-   [x] Arquitectura de Microservicios
-   [x] Comunicación Resiliente (Circuit Breaker)
-   [x] Base de Datos PostgreSQL con Migraciones Flyway
-   [x] APIs RESTful con Paginación y Ordenamiento
-   [x] Frontend con Vue 3 y TailwindCSS
-   [x] Lista de Productos con Búsqueda y Filtros
-   [x] Flujo de Compra de Inventario
-   [x] Bloqueo Optimista para Control de Concurrencia
