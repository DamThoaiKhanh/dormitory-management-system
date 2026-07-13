<script setup lang="ts">
import { useRoute } from 'vue-router';
import { ref } from 'vue';
import AppSidebar from './components/layout/AppSidebar.vue';
import AppHeader from './components/layout/AppHeader.vue';

const route = useRoute();
const isMobileMenuOpen = ref(false);

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
};

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false;
};
</script>

<template>
  <div class="min-h-screen flex bg-gray-50 font-sans antialiased">
    <!-- Sidebar Desktop -->
    <AppSidebar v-if="route.meta.requiresAuth !== false" class="hidden md:flex flex-shrink-0" />

    <!-- Sidebar Mobile (Drawer) -->
    <AppSidebar
      v-if="route.meta.requiresAuth !== false"
      :is-mobile-open="isMobileMenuOpen"
      @close="closeMobileMenu"
      class="md:hidden fixed inset-y-0 left-0 z-50 transform transition-transform duration-300"
      :class="{ 'translate-x-0': isMobileMenuOpen, '-translate-x-full': !isMobileMenuOpen }"
    />

    <!-- Overlay Mobile -->
    <div
      v-if="isMobileMenuOpen"
      class="md:hidden fixed inset-0 bg-black/50 z-40"
      @click="closeMobileMenu"
    />

    <div class="flex-1 flex flex-col min-w-0">
      <AppHeader v-if="route.meta.requiresAuth !== false" @toggle-mobile-menu="toggleMobileMenu" />

      <main class="flex-1 overflow-y-auto">
        <router-view />
      </main>
    </div>
  </div>
</template>
