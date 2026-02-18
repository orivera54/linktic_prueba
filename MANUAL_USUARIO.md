# Manual de Usuario - Sistema de Gestión de Inventario

Bienvenido. Este manual le guiará a través de las funcionalidades del Sistema de Gestión de Inventario Linktic.

## 1. Introducción

La aplicación permite a los usuarios consultar el catálogo de productos y simular compras de inventario. Está diseñada para ser intuitiva y fácil de usar.

## 2. Acceso al Sistema (Autenticación)

Para acceder al sistema, diríjase a: **http://localhost:5173**. 

Se le solicitará iniciar sesión. Dado que es una versión de prueba técnica, puede ingresar con cualquier correo y contraseña ficticios. El sistema le otorgará un token de acceso simulado.

Una vez dentro, podrá ver su usuario en la barra superior y tendrá la opción de **Cerrar Sesión**.

## 3. Navegación y Funcionalidades Principales

### 3.1. Catálogo de Productos y Caché
En la vista principal, verá la cuadrícula de productos. Note el botón **"Sincronizar"** en la parte superior derecha:
- La aplicación guarda los productos en una memoria temporal (clase `SimpleCache`) por 5 minutos para que la navegación sea instantánea.
- Si desea forzar la recarga de los datos desde el servidor, presione **"Sincronizar"**.

### 3.2. Ver Detalle de un Producto
Ahora puede ver información más detallada de cada ítem antes de proceder:
1. Haga clic en la **imagen (icono de caja)** o en el **nombre** de cualquier producto.
2. Será llevado a una vista dedicada con el precio resaltado, SKU y descripción del producto.
3. Desde esta vista también puede iniciar el proceso de compra.

### 3.3. Comprar un Producto
Para realizar una compra:
1. Haga clic en el botón **"Agregar al Carrito"** (desde la lista) o **"Comprar Ahora"** (desde el detalle).
2. Se abrirá la ventana de confirmación en español.
3. Ingrese la cantidad deseada.
4. Presione **"Confirmar Compra"**.
   - **Nota de Robustez:** Si el servidor está caído, la aplicación le mostrará un botón de **"Reintentar"** para facilitar la recuperación sin recargar la página.
   - Si no hay suficiente stock, recibirá una notificación inmediata detallando el error (409 Conflict).

### 3.4. Búsqueda y Filtros
- Utilice la barra de búsqueda para filtrar por nombre en tiempo real.
- Utilice el selector de estado para ver solo productos **Activos** o **Inactivos**.

- Utilice el selector de estado para ver solo productos **Activos** o **Inactivos**.

## 5. Pruebas de API (Avanzado)

Si desea probar los servicios directamente sin usar la interfaz gráfica (por ejemplo, para automatización o integración), puede utilizar comandos cURL.

En el **Manual de Instalación (Sección 8)** encontrará la lista completa de comandos cURL para:
- Crear, actualizar y eliminar productos (Puerto 8081).
- Consultar inventario y realizar compras manuales (Puerto 8082).

**Ejemplo rápido (Consultar inventario):**
```bash
curl -X GET http://localhost:8082/inventory/{productId}
```

## 6. Soporte
Si encuentra problemas persistentes, verifique el **Manual de Instalación** o use la función de "Sincronizar" para refrescar el estado de la aplicación.
