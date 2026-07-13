<script setup lang="ts">
import { useRoute } from 'vue-router';
import { LayoutDashboard, Building2, DoorOpen, Users, UserCog, Settings, X } from '@lucide/vue';
import logo from '../../assets/logo.png';

const route = useRoute();

defineProps<{
  isMobileOpen?: boolean;
}>();

defineEmits<{
  (e: 'close'): void;
}>();

interface MenuItem {
  name: string;
  path: string;
  icon: any;
}

const menuItems: MenuItem[] = [
  { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
  { name: 'Buildings', path: '/buildings', icon: Building2 },
  { name: 'Rooms', path: '/rooms', icon: DoorOpen },
  { name: 'Students', path: '/students', icon: Users },
  { name: 'Staff', path: '/staff', icon: UserCog },
];
</script>

<template>
  <aside
    class="w-72 bg-white border-r border-gray-200 flex flex-col h-full overflow-y-auto"
    :class="{ 'shadow-2xl': isMobileOpen }"
  >
    <button
      @click="$emit('close')"
      class="md:hidden absolute right-4 top-4 text-gray-500 z-10 hover:text-red-600 cursor-pointer rounded-full hover:bg-gray-100 p-2"
    >
      <X :size="20" stroke-width="3" />
    </button>

    <!-- Logo -->
    <div class="h-16 flex items-center px-6 border-b border-gray-200">
      <img :src="logo" alt="Dorm Manager Logo" class="w-10 h-10 mr-3 object-cover rounded-full" />
      <span class="hidden md:block text-xl font-bold text-blue-600 tracking-wide"
        >Dorm Manager</span
      >
      <span class="md:hidden text-lg font-bold text-blue-600 tracking-wide">DMS</span>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 p-4 space-y-1">
      <router-link
        v-for="item in menuItems"
        :key="item.path"
        :to="item.path"
        @click="$emit('close')"
        class="flex items-center space-x-3 px-4 py-3 rounded-lg text-base font-medium transition-colors"
        :class="
          route.path.startsWith(item.path)
            ? 'bg-blue-50 text-blue-600'
            : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
        "
      >
        <component :is="item.icon" :size="20" />
        <span>{{ item.name }}</span>
      </router-link>
    </nav>

    <!-- Settings -->
    <div class="p-4 border-t border-gray-200">
      <router-link
        to="/settings"
        @click="$emit('close')"
        class="flex items-center space-x-3 px-4 py-3 rounded-lg text-base font-medium transition-colors"
        :class="
          route.path.startsWith('/settings')
            ? 'bg-blue-50 text-blue-600'
            : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
        "
      >
        <Settings :size="20" />
        <span>Settings</span>
      </router-link>
    </div>
  </aside>
</template>
