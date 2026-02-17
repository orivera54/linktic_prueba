# Manual de Instalación e Implementación

Este documento detalla los pasos necesarios para instalar, configurar y ejecutar la aplicación Full Stack de Gestión de Inventario.

## 1. Requisitos del Sistema

Antes de comenzar, asegúrese de tener instalado el siguiente software:

*   **Java Development Kit (JDK) 17 o superior**: Necesario para ejecutar los servicios backend (Spring Boot).
    *   Verificar instalación: `java -version`
*   **Apache Maven 3.8+**: Herramienta de construcción para Java.
    *   Verificar instalación: `mvn -version`
*   **Node.js 20+ y npm**: Necesario para el frontend (Vue.js).
    *   Verificar instalación: `node -v` y `npm -v`
*   **Docker y Docker Compose**: Necesario para ejecutar la base de datos PostgreSQL.
    *   Verificar instalación: `docker -v` y `docker-compose -v`
*   **Git**: Para clonar el repositorio (opcional si se descarga el código).

## 2. Configuración de la Base de Datos

El proyecto utiliza PostgreSQL. La forma más sencilla de iniciarlo es utilizando Docker Compose, que levantará una instancia de la base de datos con la configuración necesaria.

1.  Abra una terminal en la raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`).
2.  Ejecute el siguiente comando:

    ```bash
    docker-compose up -d
    ```

    *   `-d`: Ejecuta el contenedor en segundo plano (detached mode).

3.  Verifique que el contenedor esté corriendo:

    ```bash
    docker ps
    ```
    Debería ver un contenedor llamado `linktic-postgres`.

    *Nota: Las tablas de la base de datos se crearán automáticamente al iniciar los servicios backend gracias a **Flyway**.*

## 3. Instalación y Ejecución del Backend

El backend consta de dos microservicios: **Products Service** e **Inventory Service**. Deben ejecutarse ambos.

### 3.1. Servicio de Productos (Products Service)

1.  Abra una nueva terminal.
2.  Navegue al directorio del servicio:
    ```bash
    cd products-service
    ```
3.  Compile y empaquete el proyecto:
    ```bash
    mvn clean package -DskipTests
    ```
4.  Ejecute el servicio:
    ```bash
    mvn spring-boot:run
    ```
    El servicio iniciará en el puerto **8081**.

### 3.2. Servicio de Inventario (Inventory Service)

1.  Abra *otra* terminal nueva.
2.  Navegue al directorio del servicio:
    ```bash
    cd inventory-service
    ```
3.  Compile y empaquete el proyecto:
    ```bash
    mvn clean package -DskipTests
    ```
4.  Ejecute el servicio:
    ```bash
    mvn spring-boot:run
    ```
    El servicio iniciará en el puerto **8082**.

## 4. Instalación y Ejecución del Frontend

1.  Abra una nueva terminal.
2.  Navegue al directorio del frontend:
    ```bash
    cd frontend
    ```
3.  Instale las dependencias del proyecto (solo la primera vez):
    ```bash
    npm install
    ```
4.  Inicie el servidor de desarrollo:
    ```bash
    npm run dev
    ```
5.  La aplicación estará disponible en su navegador en: **http://localhost:5173**

## 5. Implementación de Pruebas

El frontend incluye una suite de pruebas robusta (Unitarias y E2E).

### 5.1. Pruebas Unitarias (Vitest)
Se utilizan para validar la lógica de los componentes, tiendas (stores) y utilidades.
```bash
cd frontend
npm run test
```

### 5.2. Pruebas de Extremo a Extremo (Playwright)
Automatizan el flujo de usuario completo (Login -> Listar -> Seleccionar -> Comprar).
```bash
cd frontend
npx playwright test
```

## 6. Verificación de la Instalación

1.  Acceda a `http://localhost:5173`.
2.  Deberá ser redirigido automáticamente a la página de **Inicio de Sesión**.
3.  Utilice cualquier correo y contraseña para ingresar (Mock Auth).
4.  Debería ver el catálogo de productos.
5.  Pruebe el botón de "Sincronizar" para verificar que el caché funciona correctamente.

## 7. Solución de Problemas Comunes

*   **Error "defineProps is a compiler macro"**: Es una advertencia informativa de Vue 3 que no afecta la ejecución.
*   **Fallo en pruebas E2E**: Asegúrese de que no haya otros procesos ocupando el puerto 5173, ya que Playwright inicia su propio servidor de desarrollo para las pruebas.
*   **Error de conexión a Base de Datos**: Asegúrese de que el contenedor de Docker esté corriendo (`docker ps`).
*   **Error CORS**: Verifique que los servicios backend estén en los puertos 8081 y 8082.
*   **Puertos Ocupados**:
    *   Products Service necesita el puerto `8081`.
    *   Inventory Service necesita el puerto `8082`.
    *   Frontend necesita el puerto `5173`.
    Asegúrese de que estos puertos estén libres.
