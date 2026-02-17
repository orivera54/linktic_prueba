import { defineStore } from 'pinia';
import { ref } from 'vue';
import { productsClient } from '@/api/client';
import type { Product, ProductStatus, Page } from '@/types';
import { globalCache } from '@/utils/cache';

export const useProductsStore = defineStore('products', () => {
    const products = ref<Product[]>([]);
    const loading = ref(false);
    const error = ref<string | null>(null);
    const totalPages = ref(0);
    const totalElements = ref(0);
    const currentPage = ref(0);

    const CACHE_TTL = 5 * 60 * 1000; // 5 minutes

    const fetchProducts = async (page = 0, size = 10, search = '', status?: ProductStatus, forceRefresh = false) => {
        const cacheKey = `products-${page}-${size}-${search}-${status || 'all'}`;

        if (!forceRefresh) {
            const cachedData = globalCache.get<Page<Product>>(cacheKey);
            if (cachedData) {
                products.value = cachedData.content;
                totalPages.value = cachedData.totalPages;
                totalElements.value = cachedData.totalElements;
                currentPage.value = cachedData.number;
                return;
            }
        }

        loading.value = true;
        error.value = null;
        try {
            const response = await productsClient.get<Page<Product>>('/products', {
                params: { page, size, search, status }
            });
            const data = response.data;
            products.value = data.content;
            totalPages.value = data.totalPages;
            totalElements.value = data.totalElements;
            currentPage.value = data.number;

            // Cache the result
            globalCache.set(cacheKey, data, CACHE_TTL);
        } catch (err: any) {
            console.error('Failed to fetch products:', err);
            error.value = err.message || 'Failed to load products';
        } finally {
            loading.value = false;
        }
    };

    return {
        products,
        loading,
        error,
        totalPages,
        totalElements,
        currentPage,
        fetchProducts
    };
});
