<script setup lang="ts">
import { defineProps, defineEmits, computed } from 'vue';

const props = defineProps<{
  currentPage: number;
  totalPages: number;
  totalElements: number;
}>();

const emit = defineEmits<{
  (e: 'page-change', page: number): void;
}>();

const pages = computed(() => {
  const range = [];
  // Simple range logic: show current, prev, next, first, last
  // For basic implementation, just show all if small, or limited range around current
  const start = Math.max(0, props.currentPage - 2);
  const end = Math.min(props.totalPages - 1, props.currentPage + 2);
  
  for (let i = start; i <= end; i++) {
    range.push(i);
  }
  return range;
});
</script>

<template>
  <div class="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6">
    <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
      <div>
        <p class="text-sm text-gray-700">
          Mostrando página
          <span class="font-medium">{{ currentPage + 1 }}</span>
          de
          <span class="font-medium">{{ totalPages }}</span>
          (<span class="font-medium">{{ totalElements }}</span> resultados)
        </p>
      </div>
      <div>
        <nav class="isolate inline-flex -space-x-px rounded-md shadow-sm" aria-label="Pagination">
          <button 
            @click="emit('page-change', currentPage - 1)"
            :disabled="currentPage === 0"
            class="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span class="sr-only">Anterior</span>
            <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
              <path fill-rule="evenodd" d="M12.79 5.23a.75.75 0 01-.02 1.06L8.832 10l3.938 3.71a.75.75 0 11-1.04 1.08l-4.5-4.25a.75.75 0 010-1.08l4.5-4.25a.75.75 0 011.06.02z" clip-rule="evenodd" />
            </svg>
          </button>
          
          <button 
            v-for="page in pages" 
            :key="page"
            @click="emit('page-change', page)"
            :class="[
              page === currentPage ? 'z-10 bg-indigo-600 text-white focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600' : 'text-gray-900 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:outline-offset-0',
              'relative inline-flex items-center px-4 py-2 text-sm font-semibold focus:z-20'
            ]"
          >
            {{ page + 1 }}
          </button>
          
          <button 
            @click="emit('page-change', currentPage + 1)"
            :disabled="currentPage >= totalPages - 1"
            class="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span class="sr-only">Siguiente</span>
            <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
              <path fill-rule="evenodd" d="M7.21 14.77a.75.75 0 01.02-1.06L11.168 10 7.23 6.29a.75.75 0 111.04-1.08l4.5 4.25a.75.75 0 010 1.08l-4.5 4.25a.75.75 0 01-1.06-.02z" clip-rule="evenodd" />
            </svg>
          </button>
        </nav>
      </div>
    </div>
  </div>
</template>
