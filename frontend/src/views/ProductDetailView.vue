<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useProductsStore } from '@/stores/products';
import { productsClient } from '@/api/client';
import PurchaseModal from '@/components/PurchaseModal.vue';
import type { Product } from '@/types';

const route = useRoute();
const router = useRouter();
const productsStore = useProductsStore();

const productId = route.params.id as string;
const product = ref<Product | null>(null);
const loading = ref(true);
const error = ref<string | null>(null);
const isPurchaseModalOpen = ref(false);

const fetchProduct = async () => {
    loading.value = true;
    error.value = null;
    try {
        const response = await productsClient.get<Product>(`/products/${productId}`);
        product.value = response.data;
    } catch (err: any) {
        console.error('Error fetching product:', err);
        error.value = 'No se pudo cargar el producto. Es posible que no exista.';
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchProduct();
});

const formatPrice = (price?: number) => {
    if (price === undefined) return '$0.00';
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    }).format(price);
};

const handlePurchaseComplete = () => {
    alert('Compra realizada con éxito');
    router.push('/');
};
</script>

<template>
    <div class="max-w-4xl mx-auto px-4 py-8">
        <div v-if="loading" class="flex justify-center py-12">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
        </div>

        <div v-else-if="error" class="bg-red-50 p-6 rounded-lg text-center">
            <p class="text-red-700 mb-4">{{ error }}</p>
            <button @click="router.push('/')" class="text-indigo-600 font-medium hover:underline">Volver a la lista</button>
        </div>

        <div v-else-if="product" class="bg-white shadow-lg rounded-xl overflow-hidden">
            <div class="md:flex">
                <div class="md:w-1/3 bg-gray-100 flex items-center justify-center p-12">
                    <span class="text-8xl">📦</span>
                </div>
                <div class="md:w-2/3 p-8">
                    <div class="mb-4">
                        <span 
                            class="px-2 py-1 text-xs font-semibold rounded-full"
                            :class="product.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                        >
                            {{ product.status === 'ACTIVE' ? 'Activo' : 'Inactivo' }}
                        </span>
                    </div>
                    <h1 class="text-3xl font-bold text-gray-900 mb-2">{{ product.name }}</h1>
                    <p class="text-sm text-gray-500 mb-6">SKU: {{ product.sku }}</p>
                    
                    <div class="border-t border-b border-gray-100 py-6 mb-6">
                        <span class="text-3xl font-bold text-indigo-600">{{ formatPrice(product.price) }}</span>
                    </div>
                    
                    <p class="text-gray-600 mb-8">
                        Este producto forma parte de nuestro catálogo exclusivo. 
                        Usted puede realizar compras directas para actualizar el inventario.
                    </p>
                    
                    <div class="flex gap-4">
                        <button 
                            @click="isPurchaseModalOpen = true"
                            class="flex-1 bg-indigo-600 text-white font-bold py-3 px-6 rounded-lg hover:bg-indigo-700 transition-colors shadow-md"
                        >
                            Comprar Ahora
                        </button>
                        <button 
                            @click="router.push('/')"
                            class="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition-colors"
                        >
                            Volver
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Purchase Modal -->
        <PurchaseModal
            v-if="product"
            :is-open="isPurchaseModalOpen"
            :product="product"
            @close="isPurchaseModalOpen = false"
            @purchase-complete="handlePurchaseComplete"
        />
    </div>
</template>
