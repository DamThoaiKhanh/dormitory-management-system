<script setup lang="ts">
import { Search } from '@lucide/vue';

// Define Props for two-way binding
defineProps<{
  searchQuery: string;
  statusFilter: string;
}>();

// Define Emits to update parent's state
defineEmits<{
  (e: 'update:searchQuery', value: string): void;
  (e: 'update:statusFilter', value: string): void;
}>();
</script>

<template>
  <div
    class="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex flex-col sm:flex-row gap-4"
  >
    <!-- Search Input -->
    <div class="flex-1 relative">
      <span class="absolute inset-y-0 left-3 flex items-center text-gray-400">
        <Search :size="20" />
      </span>
      <input
        :value="searchQuery"
        @input="$emit('update:searchQuery', ($event.target as HTMLInputElement).value)"
        type="text"
        placeholder="Search buildings by code or name..."
        class="w-full pl-9 pr-4 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
      />
    </div>

    <!-- Status Filter Dropdown -->
    <div class="w-full sm:w-48 flex items-center space-x-2">
      <span class="text-sm text-gray-500 whitespace-nowrap">Status:</span>
      <select
        :value="statusFilter"
        @change="$emit('update:statusFilter', ($event.target as HTMLSelectElement).value)"
        class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
      >
        <option value="All">All</option>
        <option value="ACTIVE">Active</option>
        <option value="INACTIVE">Inactive</option>
      </select>
    </div>
  </div>
</template>
