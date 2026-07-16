<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/authStore';
import { useToast } from 'vue-toastification';
import LoginForm from '../components/auth/LoginForm.vue';

const authStore = useAuthStore();
const router = useRouter();
const toast = useToast();

const username = ref('');
const password = ref('');
const isLoading = ref(false);

const handleLogin = async () => {
  if (!username.value.trim() || !password.value.trim()) {
    toast.error('Username and password cannot be left empty!');
    return;
  }

  isLoading.value = true;
  try {
    const success = await authStore.login(username.value, password.value);
    if (success) {
      toast.success(`Welcome back, ${username.value}!`);
      router.push('/dashboard');
    } else {
      toast.error('Invalid credentials, please try again.');
    }
  } catch (error) {
    toast.error('Something went wrong during login.');
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <LoginForm
    v-model:username="username"
    v-model:password="password"
    :isLoading="isLoading"
    @submit="handleLogin"
  />
</template>
