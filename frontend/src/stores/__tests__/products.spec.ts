import { describe, it, expect, beforeEach, vi } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useProductsStore } from '../products';
import { productsClient } from '@/api/client';
import { globalCache } from '@/utils/cache';

// Mock axios client
vi.mock('@/api/client', () => ({
    productsClient: {
        get: vi.fn()
    }
}));

describe('ProductsStore', () => {
    beforeEach(() => {
        setActivePinia(createPinia());
        vi.clearAllMocks();
        globalCache.clear();
    });

    it('should initialize with empty state', () => {
        const store = useProductsStore();
        expect(store.products).toEqual([]);
        expect(store.loading).toBe(false);
        expect(store.error).toBeNull();
    });

    it('should fetch products successfully', async () => {
        const store = useProductsStore();
        const mockData = {
            content: [{ id: '1', name: 'Product 1', price: 10, sku: 'SKU1', status: 'ACTIVE' }],
            totalPages: 1,
            totalElements: 1,
            number: 0
        };

        (productsClient.get as any).mockResolvedValueOnce({ data: mockData });

        await store.fetchProducts(0, 10);

        expect(store.products).toHaveLength(1);
        expect(store.products[0].name).toBe('Product 1');
        expect(store.loading).toBe(false);
        expect(store.totalElements).toBe(1);
    });

    it('should handle fetch error', async () => {
        const store = useProductsStore();
        (productsClient.get as any).mockRejectedValueOnce(new Error('Network Error'));

        await store.fetchProducts();

        expect(store.error).toBe('Network Error');
        expect(store.loading).toBe(false);
        expect(store.products).toEqual([]);
    });

    it('should use cache if available', async () => {
        const store = useProductsStore();
        const mockData = { content: [], totalPages: 0, totalElements: 0, number: 0 };

        (productsClient.get as any).mockResolvedValueOnce({ data: mockData });

        // First call - should hit network
        await store.fetchProducts();
        expect(productsClient.get).toHaveBeenCalledTimes(1);

        // Second call - should hit cache
        await store.fetchProducts();
        expect(productsClient.get).toHaveBeenCalledTimes(1);
    });
});
