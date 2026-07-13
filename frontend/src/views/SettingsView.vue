<script setup lang="ts">
import { ref } from 'vue';
import { useToast } from 'vue-toastification';

// Mock State
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
  toast.success('System settings updated successfully!');
};
</script>

<template>
  <div class="p-6 max-w-4xl mx-auto space-y-6 relative">
    <!-- Header -->
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Settings</h1>
      <p class="text-sm text-gray-500 mt-1">
        Configure global dormitory rules, system parameters, and contact profiles
      </p>
    </div>

    <div class="space-y-6">
      <!-- 1. General Profile -->
      <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
        <h2
          class="text-sm font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-2"
        >
          Profile & Branding
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Organization Name</label>
            <input
              v-model="systemSettings.dormitoryName"
              type="text"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1"
              >System Contact Email</label
            >
            <input
              v-model="systemSettings.contactEmail"
              type="email"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Base Currency</label>
            <select
              v-model="systemSettings.currency"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
            >
              <option value="USD">USD ($)</option>
              <option value="VND">VND (đ)</option>
              <option value="EUR">EUR (€)</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 2. Room & Utility Rules -->
      <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
        <h2
          class="text-sm font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-2"
        >
          Utility & Tax Rules
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1"
              >Default Invoice VAT (%)</label
            >
            <input
              v-model.number="systemSettings.taxRate"
              type="number"
              min="0"
              max="100"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div class="flex items-center pt-5">
            <span class="text-xs text-gray-400 italic"
              >Applied automatically to all monthly room invoices.</span
            >
          </div>
        </div>
      </div>

      <!-- 3. System Controls (Toggles) -->
      <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
        <h2
          class="text-sm font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-2"
        >
          System Controls
        </h2>
        <div class="divide-y divide-gray-100">
          <!-- Toggle 1 -->
          <div class="flex items-center justify-between py-3">
            <div>
              <h3 class="text-sm font-medium text-gray-900">Allow Online Student Registration</h3>
              <p class="text-xs text-gray-500">
                Students can submit room contract requests from the public portal
              </p>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                v-model="systemSettings.allowStudentRegistration"
                class="sr-only peer"
              />
              <div
                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"
              ></div>
            </label>
          </div>
          <!-- Toggle 2 -->
          <div class="flex items-center justify-between py-3">
            <div>
              <h3 class="text-sm font-medium text-gray-900">Maintenance Mode</h3>
              <p class="text-xs text-rose-500 font-medium">
                Lock the system dashboard for urgent database upgrades
              </p>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                v-model="systemSettings.maintenanceMode"
                class="sr-only peer"
              />
              <div
                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-rose-600"
              ></div>
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Action Button -->
    <div class="flex items-center justify-end pt-2">
      <button
        @click="handleSaveSettings"
        class="px-6 py-2.5 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm cursor-pointer"
      >
        Save Configuration
      </button>
    </div>
  </div>
</template>
