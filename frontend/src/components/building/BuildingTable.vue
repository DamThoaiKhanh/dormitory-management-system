<script setup lang="ts">
import TableSkeleton, { type SkeletonColumn } from '../common/TableSkeleton.vue';
import type { Building } from '../../types/building.ts';

// Define input properties
defineProps<{
  buildings: Building[];
  isLoading: boolean;
}>();

// Define action events to delegate interactions to the Smart View[cite: 8]
defineEmits<{
  (e: 'edit', building: Building): void;
  (e: 'delete', building: Building): void;
}>();

// Define columns definition for the TableSkeleton[cite: 8]
const skeletonColumns: SkeletonColumn[] = [
  { key: 'code', label: 'Code', width: '10%', skeletonWidth: '150px' },
  { key: 'building_name', label: 'Building Name', width: '30%', skeletonWidth: '70px' },
  { key: 'floors', label: 'Floors', width: '20%', skeletonWidth: '90px' },
  { key: 'status', label: 'Status', width: '20%', type: 'badge' },
  { key: 'actions', label: 'Actions', width: '20%', align: 'right', type: 'actions' },
];
</script>

<template>
  <!-- Loading State -->
  <TableSkeleton v-if="isLoading" :columns="skeletonColumns" :rows="5" />

  <!-- Data Table -->
  <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr
            class="bg-gray-50 border-b border-gray-100 text-xs font-semibold text-gray-500 uppercase tracking-wider"
          >
            <th class="px-6 py-4">Code</th>
            <th class="px-6 py-4">Building Name</th>
            <th class="px-6 py-4">Floors</th>
            <th class="px-6 py-4">Status</th>
            <th class="px-6 py-4 text-right">Actions</th>
            <th class="px-6 py-4">Address</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 text-sm text-gray-700">
          <tr v-for="b in buildings" :key="b.code" class="hover:bg-gray-50/70 transition-colors">
            <td class="px-6 py-4 font-semibold text-gray-900">{{ b.code }}</td>
            <td class="px-6 py-4">{{ b.name }}</td>
            <td class="px-6 py-4">{{ b.numberOfFloors }}</td>
            <td class="px-6 py-4">
              <span
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                :class="
                  b.status === 'ACTIVE'
                    ? 'bg-emerald-50 text-emerald-700'
                    : 'bg-rose-50 text-rose-700'
                "
              >
                {{ b.status }}
              </span>
            </td>
            <td class="px-6 py-4 text-right space-x-3 text-sm font-medium">
              <button
                @click="$emit('edit', b)"
                class="text-blue-600 hover:text-blue-900 cursor-pointer"
              >
                Edit
              </button>
              <button
                @click="$emit('delete', b)"
                class="text-rose-600 hover:text-rose-900 cursor-pointer"
              >
                Delete
              </button>
            </td>
            <td class="px-6 py-4">{{ b.address || '-' }}</td>
          </tr>

          <!-- Empty State -->
          <tr v-if="buildings.length === 0">
            <td colspan="6" class="px-6 py-12 text-center text-gray-400">
              No buildings found matching your criteria.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
