<script setup lang="ts">
import { ref } from 'vue';
import { useToast } from 'vue-toastification';
import SettingsForm from '../components/settings/SettingsForm.vue';

// Khởi tạo state (Có thể thay thế bằng dữ liệu từ store của bạn nếu cần)
const systemSettings = ref({
  dormitoryName: 'Đại học ABC - Khu Ký túc xá Xanh',
  contactEmail: 'admin.dorm@abc.edu.vn',
  currency: 'USD',
  taxRate: 10,
  maintenanceMode: false,
  allowStudentRegistration: true,
});

const toast = useToast();

const handleSaveSettings = () => {
  // Logic gọi API lưu trữ cấu hình sẽ được viết ở đây
  toast.success('System settings updated successfully!');
};
</script>

<template>
  <div class="p-6 max-w-4xl mx-auto space-y-6">
    <!-- Header -->
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Settings</h1>
      <p class="text-sm text-gray-500 mt-1">
        Configure global dormitory rules, system parameters, and contact profiles
      </p>
    </div>

    <!-- Dumb Form -->
    <SettingsForm v-model="systemSettings" @save="handleSaveSettings" />
  </div>
</template>
