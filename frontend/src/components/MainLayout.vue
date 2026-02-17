<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const router = useRouter();

const handleLogout = () => {
  authStore.logout();
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- Header -->
    <header class="bg-white shadow-sm sticky top-0 z-10" v-if="authStore.isAuthenticated">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex">
            <div class="flex-shrink-0 flex items-center">
              <span class="text-xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Linktic Inventory</span>
            </div>
            <nav class="hidden sm:ml-6 sm:flex sm:space-x-8">
              <RouterLink to="/" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium" active-class="border-indigo-500 text-gray-900">
                Productos
              </RouterLink>
              <RouterLink to="/inventory" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium" active-class="border-indigo-500 text-gray-900">
                Inventario
              </RouterLink>
            </nav>
          </div>
          <div class="flex items-center gap-4">
             <span class="text-sm text-gray-600 hidden sm:block">{{ authStore.user }}</span>
             <button 
                @click="handleLogout"
                class="bg-white text-gray-500 hover:text-red-600 px-3 py-2 rounded-md text-sm font-medium transition-colors border border-gray-200 hover:border-red-200"
              >
               Cerrar Sesión
             </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow">
      <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <slot />
      </div>
    </main>

    <!-- Footer -->
    <footer class="bg-white border-t border-gray-200">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        <p class="text-center text-sm text-gray-500">
          &copy; 2026 Prueba Linktic. Todos los derechos reservados.
        </p>
      </div>
    </footer>
  </div>
</template>
