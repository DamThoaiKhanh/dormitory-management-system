<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuthStore } from '../../stores/authStore';
import { Bell, Menu } from '@lucide/vue';

const emit = defineEmits<{
  (e: 'toggle-mobile-menu'): void;
}>();

const authStore = useAuthStore();
const router = useRouter();

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};
</script>

<template>
  <header
    class="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 sticky top-0 z-10"
  >
    <div class="flex items-center">
      <button
        @click="emit('toggle-mobile-menu')"
        class="p-2 -ml-2 mr-2 text-gray-500 hover:text-gray-700 rounded-lg md:hidden hover:bg-gray-100 cursor-pointer"
      >
        <Menu :size="24" />
      </button>

      <span class="text-base lg:text-lg text-gray-700 font-medium hidden md:block">
        Dormitory Management System
      </span>
    </div>

    <div class="flex items-center space-x-4">
      <button
        class="p-2 text-gray-400 hover:text-gray-600 rounded-full hover:bg-gray-50 relative transition-colors cursor-pointer"
      >
        <Bell :size="20" />
        <span class="absolute top-1.5 right-1.5 w-1.5 h-1.5 bg-red-500 rounded-full"></span>
      </button>

      <div class="h-6 w-px bg-gray-200"></div>

      <div class="flex items-center space-x-3 group">
        <div
          class="w-8 h-8 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold text-base uppercase shadow-sm"
        >
          {{ authStore.user?.fullName ? authStore.user.fullName.charAt(0) : 'A' }}
        </div>
        <div class="text-left hidden sm:block">
          <p class="text-sm font-semibold text-gray-700 transition-colors">
            {{ authStore.user?.fullName || 'Chưa đăng nhập' }}
          </p>
          <p class="text-xs text-gray-400">
            {{ authStore.user?.role || 'Guest' }}
          </p>
        </div>
      </div>

      <div class="h-6 w-px bg-gray-200"></div>

      <!-- Logout button -->
      <button
        @click="handleLogout"
        class="inline-flex items-center space-x-1 px-3 py-1.5 bg-red-50 hover:bg-red-100 text-red-600 rounded-lg text-sm font-medium transition-colors border border-red-100 cursor-pointer"
      >
        <!-- Icon Logout dạng SVG -->
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          class="w-4 h-4"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75"
          />
        </svg>
        <span class="hidden lg:inline">Logout</span>
      </button>
    </div>
  </header>
</template>
