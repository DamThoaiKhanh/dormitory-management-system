<script setup lang="ts">
import { Users, Hotel, Warehouse, ChartLine } from '@lucide/vue';

defineProps<{
  totalStudents: number;
  totalRooms: number;
  totalBuildings: number;
  occupancyRate: number;
  totalOccupied: number;
  totalCapacity: number;
}>();
</script>

<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
    <!-- Total students card -->
    <div
      class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
    >
      <div>
        <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Total Students</p>
        <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ totalStudents }}</h3>
      </div>
      <div class="w-10 h-10 rounded-lg bg-blue-50 flex items-center justify-center text-blue-600">
        <Users :size="20" />
      </div>
    </div>

    <!-- Total rooms card -->
    <div
      class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
    >
      <div>
        <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Total Rooms</p>
        <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ totalRooms }}</h3>
      </div>
      <div
        class="w-10 h-10 rounded-lg bg-purple-50 flex items-center justify-center text-purple-600"
      >
        <Warehouse :size="20" />
      </div>
    </div>

    <!-- Total buildings card -->
    <div
      class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
    >
      <div>
        <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Total Buildings</p>
        <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ totalBuildings }}</h3>
      </div>
      <div class="w-10 h-10 rounded-lg bg-amber-50 flex items-center justify-center text-amber-600">
        <Hotel :size="20" />
      </div>
    </div>

    <!-- Occupancy Rate card -->
    <div
      class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
    >
      <div>
        <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Occupancy Rate</p>
        <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ occupancyRate }}%</h3>
        <p class="text-[10px] text-gray-400 mt-0.5">
          {{ totalOccupied }}/{{ totalCapacity }} beds filled
        </p>
      </div>
      <div
        class="w-10 h-10 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600"
      >
        <ChartLine :size="20" />
      </div>
    </div>
  </div>
</template>
