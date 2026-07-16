<script setup lang="ts">
import { X } from '@lucide/vue';
import type { NewStaff } from '../../types/staff';

const isOpen = defineModel<boolean>({ required: true });
const staffData = defineModel<NewStaff>('staffData', { required: true });

defineProps<{
  isEditMode: boolean;
  buildingsList: string[];
}>();

const emit = defineEmits<{
  (e: 'save'): void;
}>();
</script>

<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm"
  >
    <div
      class="bg-white w-full max-w-md max-h-[calc(100vh-2rem)] rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col animate-in fade-in zoom-in-95 duration-150"
    >
      <!-- Header -->
      <div
        class="shrink-0 px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/50"
      >
        <h3 class="text-base font-bold text-gray-900">
          {{ isEditMode ? 'Edit Staff Member' : 'Add Staff Member' }}
        </h3>
        <button @click="isOpen = false" class="text-gray-400 hover:text-gray-600 cursor-pointer">
          <X :size="18" stroke-width="2.5" />
        </button>
      </div>

      <!-- Form Content -->
      <div class="p-6 space-y-4 flex-1 min-h-0 overflow-y-auto text-sm">
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Employee Code *</label
          >
          <input
            v-model="staffData.employeeCode"
            type="text"
            :disabled="isEditMode"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Full Name *</label
          >
          <input
            v-model="staffData.fullName"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Gender</label>
          <select
            v-model="staffData.gender"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option value="MALE">MALE</option>
            <option value="FEMALE">FEMALE</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Date of Birth *</label
          >
          <input
            v-model="staffData.dateOfBirth"
            type="text"
            placeholder="YYYY-MM-DD"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Citizen ID *</label
          >
          <input
            v-model="staffData.citizenId"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Phone Number *</label
          >
          <input
            v-model="staffData.phoneNumber"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Address</label>
          <input
            v-model="staffData.address"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Assign to Building</label
          >
          <select
            v-model="staffData.assignedBuildingCode"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option value="None">-- Leave Unassigned --</option>
            <option v-for="b in buildingsList" :key="b" :value="b">
              {{ b }}
            </option>
          </select>
        </div>
      </div>

      <!-- Footer Buttons -->
      <div
        class="shrink-0 px-6 py-4 bg-gray-50 border-t border-gray-100 flex items-center justify-end space-x-3"
      >
        <button
          @click="isOpen = false"
          class="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-800 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer"
        >
          Cancel
        </button>
        <button
          @click="$emit('save')"
          class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm cursor-pointer"
        >
          Save Details
        </button>
      </div>
    </div>
  </div>
</template>
