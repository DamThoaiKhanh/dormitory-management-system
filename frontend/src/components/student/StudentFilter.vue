<script setup lang="ts">
import { Search } from '@lucide/vue';

// Đồng bộ hóa trực tiếp với cha bằng defineModel
const searchQuery = defineModel<string>('searchQuery', { default: '' });
const selectedBuilding = defineModel<string>('selectedBuilding', { default: 'All' });

defineProps<{
  buildingCodeList: string[];
}>();
</script>

<template>
  <div
    class="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex flex-col sm:flex-row gap-4"
  >
    <!-- Search Name or ID -->
    <div class="flex-1 relative">
      <span class="absolute inset-y-0 left-3 flex items-center text-gray-400">
        <Search :size="20" />
      </span>
      <input
        v-model="searchQuery"
        type="text"
        placeholder="Search by student name or ID..."
        class="w-full pl-9 pr-4 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
      />
    </div>

    <!-- Filter by Building -->
    <div class="w-full sm:w-56 flex items-center space-x-2">
      <span class="text-sm text-gray-500 whitespace-nowrap">Building:</span>
      <select
        v-model="selectedBuilding"
        class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
      >
        <option value="All">All</option>
        <option v-for="building in buildingCodeList" :key="building" :value="building">
          {{ building }}
        </option>
      </select>
    </div>
  </div>
</template>
