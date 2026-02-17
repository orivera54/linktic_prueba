import { defineStore } from 'pinia';
import { ref } from 'vue';
import { inventoryClient } from '@/api/client';
import type { PurchaseRequest } from '@/types';

export const useInventoryStore = defineStore('inventory', () => {
    const loading = ref(false);
    const error = ref<string | null>(null);

    const purchaseProduct = async (request: PurchaseRequest) => {
        loading.value = true;
        error.value = null;
        try {
            await inventoryClient.post('/inventory/purchases', request, {
                headers: {
                    'Idempotency-Key': crypto.randomUUID()
                }
            });
            return true;
        } catch (err: any) {
            console.error('Purchase failed:', err);
            error.value = err.response?.data?.detail || err.response?.data?.title || err.message || 'Purchase failed';
            return false;
        } finally {
            loading.value = false;
        }
    };

    return {
        loading,
        error,
        purchaseProduct,
    };
});
