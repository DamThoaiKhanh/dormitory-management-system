<script setup lang="ts">
import type { NewRoom } from '../../types/room';

const isOpen = defineModel<boolean>('isOpen', { required: true });
const roomData = defineModel<NewRoom>('roomData', { required: true });

defineProps<{
  editingRoomId: number | null;
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
      class="bg-white w-full max-w-md rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col"
    >
      <!-- Header -->
      <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
        <h3 class="text-base font-bold text-gray-900">
          {{ editingRoomId ? 'Edit Room' : 'New Room' }}
        </h3>
        <button @click="isOpen = false" class="text-gray-400 hover:text-gray-600 cursor-pointer">
          ✕
        </button>
      </div>

      <!-- Form Inputs -->
      <div class="p-6 space-y-4">
        <div>
          <div class="flex items-center gap-1 mb-1">
            <label class="text-xs font-semibold text-gray-600 uppercase">Room Number</label>
            <span class="text-red-500">*</span>
          </div>
          <input
            v-model="roomData.roomNumber"
            type="text"
            placeholder="e.g. 101, 202"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <div class="flex items-center gap-1 mb-1">
            <label class="text-xs font-semibold text-gray-600 uppercase">Building Code</label>
            <span class="text-red-500">*</span>
          </div>
          <select
            v-model="roomData.buildingCode"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option v-for="building in buildingsList" :key="building" :value="building">
              {{ building }}
            </option>
          </select>
        </div>
        <div>
          <div class="flex items-center gap-1 mb-1">
            <label class="text-xs font-semibold text-gray-600 uppercase">Monthly Rent ($)</label>
            <span class="text-red-500">*</span>
          </div>
          <input
            v-model.number="roomData.monthlyRent"
            type="number"
            min="0"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <div class="flex items-center gap-1 mb-1">
            <label class="text-xs font-semibold text-gray-600 uppercase"
              >Maximum Capacity (Beds)</label
            >
            <span class="text-red-500">*</span>
          </div>
          <input
            v-model.number="roomData.maximumCapacity"
            type="number"
            min="1"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Status</label>
          <select
            v-model="roomData.status"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>
      </div>

      <!-- Actions -->
      <div
        class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex items-center justify-end space-x-3"
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
          Save
        </button>
      </div>
    </div>
  </div>
</template>
