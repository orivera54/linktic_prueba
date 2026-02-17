<script setup lang="ts">
import { ref, defineProps, defineEmits, watch } from 'vue';
import type { Product } from '@/types';
import { useInventoryStore } from '@/stores/inventory';

const props = defineProps<{
  isOpen: boolean;
  product: Product | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'purchase-complete'): void;
}>();

const quantity = ref(1);
const inventoryStore = useInventoryStore();
const isPurchasing = ref(false);
const error = ref<string | null>(null);

// Reset state when modal opens
watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    quantity.value = 1;
    error.value = null;
  }
});

const handlePurchase = async () => {
  if (!props.product) return;
  
  isPurchasing.value = true;
  error.value = null;
  
  try {
    const success = await inventoryStore.purchaseProduct({
      productId: props.product.id,
      quantity: quantity.value
    });
    
    if (success) {
      emit('purchase-complete');
      emit('close');
    } else {
      error.value = inventoryStore.error || 'Purchase failed';
    }
  } catch (err) {
    error.value = 'An unexpected error occurred';
  } finally {
    isPurchasing.value = false;
  }
};

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(price);
};
</script>

<template>
  <div v-if="isOpen" class="relative z-10" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <!-- Background backdrop -->
    <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"></div>

    <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
      <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
        <div class="relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg">
          <div class="bg-white px-4 pb-4 pt-5 sm:p-6 sm:pb-4">
            <div class="sm:flex sm:items-start">
              <div class="mx-auto flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-full bg-blue-100 sm:mx-0 sm:h-10 sm:w-10">
                <svg class="h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z" />
                </svg>
              </div>
              <div class="mt-3 text-center sm:ml-4 sm:mt-0 sm:text-left w-full">
                <h3 class="text-base font-semibold leading-6 text-gray-900" id="modal-title">Comprar Producto</h3>
                <div class="mt-2" v-if="product">
                  <p class="text-sm text-gray-500">
                    Está a punto de comprar: <span class="font-bold text-gray-900">{{ product.name }}</span>
                  </p>
                  <p class="text-sm text-gray-500 mt-1">
                    Precio por unidad: {{ formatPrice(product.price) }}
                  </p>
                  
                  <div class="mt-4">
                    <label for="quantity" class="block text-sm font-medium leading-6 text-gray-900">Cantidad</label>
                    <div class="mt-2">
                       <input 
                        type="number" 
                        name="quantity" 
                        id="quantity" 
                        v-model="quantity" 
                        min="1"
                        class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 px-3"
                      >
                    </div>
                  </div>
                  
                  <div class="mt-4 bg-gray-50 p-3 rounded-md">
                    <p class="text-sm font-medium text-gray-900 flex justify-between">
                      <span>Total:</span>
                      <span>{{ formatPrice(product.price * quantity) }}</span>
                    </p>
                  </div>
                  
                  <div v-if="error" class="mt-2 text-sm text-red-600">
                    {{ error }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
            <button 
              type="button" 
              class="inline-flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 sm:ml-3 sm:w-auto disabled:opacity-50 disabled:cursor-not-allowed"
              :disabled="isPurchasing || quantity < 1"
              @click="handlePurchase"
            >
              <span v-if="isPurchasing">Procesando...</span>
              <span v-else>Confirmar Compra</span>
            </button>
            <button 
              type="button" 
              class="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto"
              @click="$emit('close')"
            >
              Cancelar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
