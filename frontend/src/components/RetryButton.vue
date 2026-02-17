<script setup lang="ts">
defineProps<{
  message?: string;
}>();

defineEmits<{
  (e: 'retry'): void;
}>();
</script>

<template>
  <div class="flex flex-col items-center justify-center p-8 bg-white rounded-lg border border-red-100 shadow-sm">
    <div class="p-3 bg-red-100 rounded-full mb-4">
      <svg class="w-8 h-8 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
      </svg>
    </div>
    <h3 class="text-lg font-semibold text-gray-900 mb-2">Error de comunicación</h3>
    <p class="text-gray-500 text-center mb-6 max-w-xs">
      {{ message || 'No se pudo conectar con el servidor. Por favor, intente de nuevo.' }}
    </p>
    <button 
      @click="$emit('retry')"
      class="inline-flex items-center px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium rounded-md transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
    >
      <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
      </svg>
      Reintentar
    </button>
  </div>
</template>
