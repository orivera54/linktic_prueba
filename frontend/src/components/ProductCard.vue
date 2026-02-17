<script setup lang="ts">
import { defineProps } from 'vue';
import { RouterLink } from 'vue-router';
import type { Product } from '@/types';

defineProps<{
  product: Product;
}>();

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(price);
};
</script>

<template>
  <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
    <!-- Placeholder Image -->
    <RouterLink :to="`/product/${product.id}`" class="block">
      <div class="h-48 bg-gray-200 flex items-center justify-center cursor-pointer hover:bg-gray-300 transition-colors">
        <span class="text-gray-400 text-4xl">📦</span>
      </div>
    </RouterLink>
    
    <div class="p-4">
      <div class="flex justify-between items-start mb-2">
        <RouterLink :to="`/product/${product.id}`" class="block hover:text-indigo-600 transition-colors truncate flex-1 mr-2">
           <h3 class="text-lg font-semibold truncate" :title="product.name">{{ product.name }}</h3>
        </RouterLink>
        <span 
          class="px-2 py-1 text-xs font-semibold rounded-full"
          :class="product.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
        >
          {{ product.status === 'ACTIVE' ? 'Activo' : 'Inactivo' }}
        </span>
      </div>
      
      <p class="text-sm text-gray-500 mb-2">SKU: {{ product.sku }}</p>
      
      <div class="flex justify-between items-center mt-4">
        <span class="text-xl font-bold text-gray-900">{{ formatPrice(product.price) }}</span>
        <button 
          class="bg-blue-600 hover:bg-blue-700 text-white font-medium py-1 px-3 rounded text-sm transition-colors"
          @click="$emit('addToCart', product)"
        >
          Agregar al Carrito
        </button>
      </div>
    </div>
  </div>
</template>
