<script setup lang="ts">
import { ref, watch } from 'vue';
import { X } from '@lucide/vue';
import type { Building } from '../../types/building.ts';

// Define Modal Configuration
const props = defineProps<{
  isOpen: boolean;
  editingCode: string | null;
  initialData: Building;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'save', data: Building): void;
}>();

// Clone initialData to prevent direct mutatation of parent's state
const formData = ref<Building>({ ...props.initialData });

// Sync formData state whenever initialData changes
watch(
  () => props.initialData,
  (newData) => {
    formData.value = { ...newData };
  },
  { deep: true }
);

// Form submit handler
const handleSubmit = () => {
  emit('save', formData.value);
};
</script>

<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm animate-fade-in"
  >
    <div
      class="bg-white w-full max-w-lg rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col"
    >
      <!-- Modal Header -->
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
        <h3 class="text-base font-bold text-gray-900">
          {{ editingCode ? 'Edit Building' : 'Add Building' }}
        </h3>
        <button
          @click="$emit('close')"
          class="text-gray-400 p-2 text-lg cursor-pointer hover:text-red-600 hover:bg-gray-100 rounded-full"
        >
          <X :size="20" stroke-width="3" />
        </button>
      </div>

      <!-- Modal Body (Form Fields) -->
      <div class="p-6 space-y-4 overflow-y-auto max-h-[70vh]">
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Building Code *</label
          >
          <input
            v-model="formData.code"
            :disabled="!!editingCode"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 disabled:bg-gray-100"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Building Name *</label
          >
          <input
            v-model="formData.name"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Address</label>
          <input
            v-model="formData.address"
            type="text"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Number of floors</label
          >
          <input
            v-model.number="formData.numberOfFloors"
            type="number"
            min="1"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
            >Description</label
          >
          <textarea
            v-model="formData.description"
            rows="3"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          ></textarea>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Status</label>
          <select
            v-model="formData.status"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>
      </div>

      <!-- Modal Footer -->
      <div
        class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex items-center justify-end space-x-3"
      >
        <button
          @click="$emit('close')"
          class="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-800 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer"
        >
          Cancel
        </button>
        <button
          @click="handleSubmit"
          class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm cursor-pointer"
        >
          Save
        </button>
      </div>
    </div>
  </div>
</template>
