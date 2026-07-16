<script setup lang="ts">
import { LogIn, Loader2 } from '@lucide/vue';

const username = defineModel<string>('username', { default: '' });
const password = defineModel<string>('password', { default: '' });

defineProps<{
  isLoading: boolean;
}>();

const emit = defineEmits<{
  (e: 'submit'): void;
}>();
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-md border border-gray-100">
      <div class="text-center">
        <h2 class="text-3xl font-extrabold text-gray-900">Dormitory Admin</h2>
        <p class="mt-2 text-sm text-gray-500">Sign in to manage your system</p>
      </div>

      <form class="mt-8 space-y-4" @submit.prevent="emit('submit')">
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Username</label>
          <input
            v-model="username"
            type="text"
            placeholder="e.g. admin"
            class="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 transition-colors"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Password</label>
          <input
            v-model="password"
            type="password"
            placeholder="••••••"
            class="w-full px-3 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 transition-colors"
          />
        </div>

        <button
          :disabled="isLoading"
          type="submit"
          class="w-full flex justify-center items-center py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-medium rounded-lg transition-all gap-2 cursor-pointer"
        >
          <Loader2 v-slot="loader" v-if="isLoading" :size="20" class="animate-spin" />
          <LogIn v-else :size="20" />
          {{ isLoading ? 'Signing in...' : 'Sign In' }}
        </button>
      </form>
    </div>
  </div>
</template>
