<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useProductsStore } from '@/stores/products';
import ProductCard from '@/components/ProductCard.vue';
import Pagination from '@/components/Pagination.vue';
import PurchaseModal from '@/components/PurchaseModal.vue';
import RetryButton from '@/components/RetryButton.vue';
import type { Product, ProductStatus } from '@/types';
import { storeToRefs } from 'pinia';

const productsStore = useProductsStore();
const { products, loading, error, totalPages, totalElements, currentPage } = storeToRefs(productsStore);

const searchQuery = ref('');
const statusFilter = ref<ProductStatus | ''>('');

// Modal State
const isPurchaseModalOpen = ref(false);
const selectedProduct = ref<Product | null>(null);

const fetchProducts = (force = false) => {
  productsStore.fetchProducts(
    currentPage.value,
    10, // Page size
    searchQuery.value,
    statusFilter.value || undefined,
    force
  );
};

onMounted(() => {
  fetchProducts();
});

let searchTimeout: ReturnType<typeof setTimeout>;
watch(searchQuery, () => {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    currentPage.value = 0; 
    fetchProducts();
  }, 300);
});

watch(statusFilter, () => {
  currentPage.value = 0;
  fetchProducts();
});

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchProducts();
};

const handleAddToCart = (product: Product) => {
  selectedProduct.value = product;
  isPurchaseModalOpen.value = true;
};

const handlePurchaseComplete = () => {
  alert('Compra exitosa!');
  fetchProducts(true); // Force refresh after purchase
};
</script>

<template>
  <div class="px-4 sm:px-6 lg:px-8 py-8">
    <div class="sm:flex sm:items-center">
      <div class="sm:flex-auto">
        <h1 class="text-2xl font-semibold text-gray-900">Productos</h1>
        <p class="mt-2 text-sm text-gray-700">Lista de todos los productos disponibles en el catálogo.</p>
      </div>
      <div class="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
        <button 
          @click="fetchProducts(true)"
          class="inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
          :disabled="loading"
        >
          <svg class="h-4 w-4 mr-2" :class="{'animate-spin': loading}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          Sincronizar
        </button>
      </div>
    </div>
    
    <!-- Filters -->
    <div class="mt-6 flex flex-col sm:flex-row gap-4" v-if="!error">
      <div class="relative rounded-md shadow-sm sm:min-w-xs">
        <input 
          v-model="searchQuery"
          type="text" 
          class="block w-full rounded-md border-0 py-1.5 pl-3 pr-10 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" 
          placeholder="Buscar productos..."
        >
      </div>
      
      <div class="sm:min-w-[150px]">
        <select 
          v-model="statusFilter"
          class="block w-full rounded-md border-0 py-1.5 pl-3 pr-10 text-gray-900 ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
        >
          <option value="">Todos los Estados</option>
          <option value="ACTIVE">Activo</option>
          <option value="INACTIVE">Inactivo</option>
        </select>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading && !products.length" class="mt-12 flex justify-center">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="mt-12 flex justify-center">
      <RetryButton :message="error" @retry="fetchProducts(true)" />
    </div>

    <!-- Product Grid -->
    <div v-else class="mt-8">
      <div v-if="products.length > 0" class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
        <ProductCard 
          v-for="product in products" 
          :key="product.id" 
          :product="product"
          @add-to-cart="handleAddToCart"
        />
      </div>

      <!-- Empty State -->
      <div v-else class="text-center py-12 bg-white rounded-lg border border-dashed border-gray-300">
        <p class="text-gray-500 font-medium">No se encontraron productos que coincidan con sus criterios.</p>
        <button @click="searchQuery = ''; statusFilter = ''" class="mt-4 text-indigo-600 hover:text-indigo-500 text-sm font-semibold">
          Limpiar filtros
        </button>
      </div>
    </div>

    <!-- Pagination -->
    <div v-if="!error && totalPages > 1" class="mt-8">
      <Pagination 
        :current-page="currentPage" 
        :total-pages="totalPages" 
        :total-elements="totalElements"
        @page-change="handlePageChange"
      />
    </div>
    
    <!-- Purchase Modal -->
    <PurchaseModal
      :is-open="isPurchaseModalOpen"
      :product="selectedProduct"
      @close="isPurchaseModalOpen = false"
      @purchase-complete="handlePurchaseComplete"
    />
  </div>
</template>
