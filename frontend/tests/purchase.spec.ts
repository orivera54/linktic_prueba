import { test, expect } from '@playwright/test';

test.describe('Flujo de Compra de Productos', () => {

    test.beforeEach(async ({ page }) => {
        // Intercept all API calls
        await page.route('**/api/v1/products*', async route => {
            await route.fulfill({
                json: {
                    content: [{
                        id: 'prod-123',
                        name: 'Producto E2E Test',
                        price: 15.5,
                        sku: 'E2E-SKU-001',
                        status: 'ACTIVE'
                    }],
                    totalPages: 1,
                    totalElements: 1,
                    number: 0
                }
            });
        });

        await page.route('**/api/v1/products/prod-123', async route => {
            await route.fulfill({
                json: {
                    id: 'prod-123',
                    name: 'Producto E2E Test',
                    price: 15.5,
                    sku: 'E2E-SKU-001',
                    status: 'ACTIVE'
                }
            });
        });

        // Login
        await page.goto('/login');
        await page.fill('#email', 'test@example.com');
        await page.fill('#password', 'password123');
        await page.click('button[type="submit"]');
        await page.waitForURL('**/');
    });

    test('Debería navegar de listar a detalle y realizar compra exitosa', async ({ page }) => {
        await page.click('text=Producto E2E Test');
        await page.waitForURL(/\/product\/prod-123/);

        await page.route('**/api/v1/inventory/purchases', async route => {
            await route.fulfill({ status: 200 });
        });

        await page.click('text=Comprar Ahora');
        await page.fill('#quantity', '2');

        // Setup dialog handler before clicking
        const dialogPromise = page.waitForEvent('dialog');
        await page.click('button:has-text("Confirmar Compra")');
        const dialog = await dialogPromise;
        await dialog.accept();

        await page.waitForURL('**/');
        await expect(page).toHaveURL(/localhost:5173\/?$/);
    });

    test('Debería mostrar error cuando no hay stock suficiente (409)', async ({ page }) => {
        await page.goto('/product/prod-123');

        await page.route('**/api/v1/inventory/purchases', async route => {
            await route.fulfill({
                status: 409,
                json: { detail: 'Stock insuficiente para el producto' }
            });
        });

        await page.click('text=Comprar Ahora');
        await page.click('button:has-text("Confirmar Compra")');

        await expect(page.locator('text=Stock insuficiente para el producto')).toBeVisible();
    });

    test('Debería mostrar error de comunicación cuando el servicio falla', async ({ page }) => {
        await page.route('**/api/v1/products*', async route => {
            await route.abort('connectionfailed');
        });

        await page.goto('/');
        await expect(page.locator('text=Error de comunicación')).toBeVisible();
        await expect(page.locator('button:has-text("Reintentar")')).toBeVisible();
    });
});
