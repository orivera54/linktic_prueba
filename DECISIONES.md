# Registro de Decisiones Técnicas (ADR)

Este documento detalla las decisiones técnicas clave tomadas durante el desarrollo de la prueba técnica para Linktic.

## ADR 1: Gestión de Estado con Pinia
**Estatus:** Aceptado
**Contexto:** Se necesitaba una forma centralizada de manejar el estado de los productos, el inventario y la autenticación.
**Decisión:** Utilizar Pinia sobre Vuex.
**Justificación:** Pinia es el estándar oficial para Vue 3. Ofrece una API más simple (Composition API), mejor soporte para TypeScript y es más ligero.

## ADR 2: Estrategia de Caching Cliente
**Estatus:** Aceptado
**Contexto:** El usuario solicitó características de nivel "Senior", específicamente control de expiración de datos.
**Decisión:** Implementar una clase `SimpleCache` con TTL (Time-To-Live).
**Justificación:** Evita llamadas redundantes a la API de productos en navegaciones frecuentes, mejorando la experiencia del usuario y reduciendo la carga en el servidor. El TTL de 5 minutos asegura que los datos no queden obsoletos indefinidamente.

## ADR 3: Mecanismo de Reintento Manual
**Estatus:** Aceptado
**Contexto:** En aplicaciones de misión crítica, los fallos de red son comunes.
**Decisión:** Crear un componente `RetryButton.vue` reutilizable.
**Justificación:** Permite al usuario recuperar la funcionalidad sin recargar toda la página, mejorando la resiliencia percibida de la aplicación.

## ADR 4: Autenticación Mock con Interceptores Axios
**Estatus:** Aceptado
**Contexto:** El backend no tiene una implementación completa de JWT, pero el frontend requería demostrar el manejo de seguridad.
**Decisión:** Implementar una tienda de `auth` con un token simulado e inyectarlo mediante interceptores de Axios.
**Justificación:** Este patrón es idéntico al que se usaría con un backend real (Spring Security + JWT), permitiendo una transición transparente en el futuro.

## ADR 5: Pruebas E2E con Playwright y Mocks
**Estatus:** Aceptado
**Contexto:** Se requería verificar el flujo completo de compra asegurando repetibilidad.
**Decisión:** Utilizar Playwright con `page.route` para mockear las API.
**Justificación:** Playwright es más rápido que Cypress y el mockeo de peticiones a nivel de red asegura que las pruebas de la UI sean independientes del estado real de la base de datos o de microservicios externos que podrían estar abajo durante el CI.

## ADR 6: Traducción y Localización
**Estatus:** Aceptado
**Contexto:** La prueba técnica se entregará a un equipo en español.
**Decisión:** Traducir toda la UI y la documentación al español.
**Justificación:** Mejora la claridad en la presentación de la prueba y demuestra atención al detalle y profesionalismo.
```
