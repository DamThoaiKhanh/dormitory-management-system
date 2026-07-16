<script setup lang="ts">
import TableSkeleton, { type SkeletonColumn } from '../common/TableSkeleton.vue';
import type { Room } from '../../types/room';

const props = defineProps<{
  rooms: Room[];
  isLoading: boolean;
}>();

defineEmits<{
  (e: 'viewDetail', room: Room): void;
  (e: 'edit', room: Room): void;
  (e: 'delete', room: Room): void;
}>();

// Calculate badge styles based on room capacity
const getOccupancyLabel = (occupied: number, capacity: number) => {
  if (occupied === 0) return { text: 'Empty', class: 'bg-gray-100 text-gray-600' };
  if (occupied === capacity) {
    return { text: 'Full', class: 'bg-rose-50 text-rose-700 border border-rose-100' };
  }
  return { text: 'Available', class: 'bg-emerald-50 text-emerald-700 border border-emerald-100' };
};

const skeletonColumns: SkeletonColumn[] = [
  { key: 'room', label: 'Room', width: '10%', skeletonWidth: '50px' },
  { key: 'building_name', label: 'Building Name', width: '20%', skeletonWidth: '70px' },
  { key: 'occupancy', label: 'Occupancy', width: '20%', skeletonWidth: '90px' },
  { key: 'status', label: 'Status', width: '20%', type: 'badge' },
  { key: 'rent', label: 'Monthly Rent', width: '10%', type: 'badge' },
  { key: 'actions', label: 'Actions', width: '20%', align: 'right', type: 'actions' },
];
</script>

<template>
  <TableSkeleton v-if="isLoading" :columns="skeletonColumns" />

  <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr
            class="bg-gray-50 border-b border-gray-100 text-xs font-semibold text-gray-500 uppercase tracking-wider"
          >
            <th class="px-6 py-4">Room</th>
            <th class="px-6 py-4">Building Name</th>
            <th class="px-6 py-4">Occupancy</th>
            <th class="px-6 py-4">Status</th>
            <th class="px-6 py-4">Monthly Rent</th>
            <th class="px-6 py-4 text-right">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 text-sm text-gray-700">
          <tr v-for="r in rooms" :key="r.id" class="hover:bg-gray-50/70 transition-colors">
            <td class="px-6 py-4 font-semibold text-gray-900">{{ r.roomNumber }}</td>
            <td class="px-6 py-4 text-gray-500">{{ r.buildingName }}</td>
            <td class="px-6 py-4">
              <div class="flex items-center space-x-2">
                <span class="font-medium text-gray-900"
                  >{{ r.currentOccupancy }} / {{ r.maximumCapacity }}</span
                >
                <span class="text-gray-400 text-xs">beds</span>
              </div>
            </td>
            <td class="px-6 py-4">
              <span
                class="inline-flex items-center px-2.5 py-1 rounded-md text-xs font-semibold tracking-wide uppercase"
                :class="getOccupancyLabel(r.currentOccupancy, r.maximumCapacity).class"
              >
                {{ getOccupancyLabel(r.currentOccupancy, r.maximumCapacity).text }}
              </span>
            </td>
            <td class="px-6 py-4 font-medium text-gray-900">
              ${{ r.monthlyRent }}<span class="text-gray-400 font-normal text-xs">/mo</span>
            </td>
            <td class="px-6 py-4 text-right space-x-3 text-xs font-medium">
              <button
                @click="$emit('viewDetail', r)"
                class="text-emerald-600 hover:text-emerald-800 text-sm font-medium mr-2 cursor-pointer"
              >
                View
              </button>
              <button
                @click="$emit('edit', r)"
                class="text-blue-600 hover:text-blue-900 mr-3 text-sm font-medium cursor-pointer"
              >
                Edit
              </button>
              <button
                @click="$emit('delete', r)"
                class="text-rose-600 hover:text-rose-900 text-sm font-medium cursor-pointer"
              >
                Delete
              </button>
            </td>
          </tr>
          <tr v-if="rooms.length === 0">
            <td colspan="6" class="px-6 py-12 text-center text-gray-400">
              No rooms found matching your filter criteria.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
