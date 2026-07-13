<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useBuildingStore } from '../stores/buildingStore';
import { useRoomStore } from '../stores/roomStore';
import { useStudentStore } from '../stores/studentStore';
import { Users, Hotel, Warehouse, ChartLine } from '@lucide/vue';

const buildingStore = useBuildingStore();
const roomStore = useRoomStore();
const studentStore = useStudentStore();

onMounted(async () => {
  await buildingStore.fetchBuildings();
  await roomStore.fetchRooms();
  await studentStore.fetchStudents();
});

const totalBuildings = computed(() => buildingStore.buildings.length);
const totalStudents = computed(() => studentStore.students.length);
const totalRooms = computed(() => roomStore.rooms.length);

const totalCapacity = computed(() => {
  return roomStore.rooms.reduce((sum, room) => sum + (room.capacity || 0), 0);
});

const totalOccupied = computed(() => {
  return roomStore.rooms.reduce((sum, room) => sum + (room.occupied || 0), 0);
});

const occupancyRate = computed(() => {
  if (totalCapacity.value === 0) return 0;
  return Math.round((totalOccupied.value / totalCapacity.value) * 100);
});
</script>

<template>
  <div class="p-6 max-w-7xl mx-auto space-y-6">
    <!-- Header -->
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
      <p class="text-sm text-gray-500 mt-1">Overview of the dormitory system</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-5">
      <!-- Total students card -->
      <div
        class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
      >
        <div>
          <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Total Students</p>
          <h3 class="text-2xl font-bold text-gray-900 mt-1">
            {{ totalStudents }}
          </h3>
        </div>
        <div
          class="w-10 h-10 rounded-lg bg-blue-50 flex items-center justify-center text-blue-600 font-semibold"
        >
          <Users :size="20" />
        </div>
      </div>

      <!-- Total room card -->
      <div
        class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
      >
        <div>
          <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Total Rooms</p>
          <h3 class="text-2xl font-bold text-gray-900 mt-1">
            {{ totalRooms }}
          </h3>
        </div>
        <div
          class="w-10 h-10 rounded-lg bg-purple-50 flex items-center justify-center text-purple-600 font-semibold"
        >
          <Warehouse :size="20" />
        </div>
      </div>

      <!-- Total building card -->
      <div
        class="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex items-center justify-between"
      >
        <div>
          <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">
            Total Buildings
          </p>
          <h3 class="text-2xl font-bold text-gray-900 mt-1">
            {{ totalBuildings }}
          </h3>
        </div>
        <div
          class="w-10 h-10 rounded-lg bg-amber-50 flex items-center justify-center text-amber-600 font-semibold"
        >
          <Hotel :size="20" />
        </div>
      </div>

      <!-- Occupacy rate card-->
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
          class="w-10 h-10 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600 font-semibold"
        >
          <ChartLine :size="20" />
        </div>
      </div>
    </div>

    <!-- Progress Bar -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
        <h2 class="text-base font-bold text-gray-800">Room occupancy</h2>
        <div class="space-y-4">
          <div>
            <div class="flex justify-between text-sm font-medium mb-1">
              <span class="text-gray-600">Building A</span>
              <span class="text-gray-900">85%</span>
            </div>
            <div class="w-full bg-gray-100 h-2 rounded-full">
              <div class="bg-blue-600 h-2 rounded-full" style="width: 85%"></div>
            </div>
          </div>
          <div>
            <div class="flex justify-between text-sm font-medium mb-1">
              <span class="text-gray-600">Building B</span>
              <span class="text-gray-900">73%</span>
            </div>
            <div class="w-full bg-gray-100 h-2 rounded-full">
              <div class="bg-blue-600 h-2 rounded-full" style="width: 73%"></div>
            </div>
          </div>
          <div>
            <div class="flex justify-between text-sm font-medium mb-1">
              <span class="text-gray-600">Building C</span>
              <span class="text-gray-900">92%</span>
            </div>
            <div class="w-full bg-gray-100 h-2 rounded-full">
              <div class="bg-blue-600 h-2 rounded-full" style="width: 92%"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- Recent students -->
      <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
        <h2 class="text-base font-bold text-gray-800">Recent students</h2>
        <div class="divide-y divide-gray-100">
          <div class="flex justify-between items-center py-3">
            <span class="text-sm font-medium text-gray-700">Nguyễn Văn A</span>
            <span class="text-xs font-semibold px-2.5 py-1 bg-gray-100 text-gray-600 rounded-md"
              >A-101</span
            >
          </div>
          <div class="flex justify-between items-center py-3">
            <span class="text-sm font-medium text-gray-700">Trần Thị B</span>
            <span class="text-xs font-semibold px-2.5 py-1 bg-gray-100 text-gray-600 rounded-md"
              >B-204</span
            >
          </div>
          <div class="flex justify-between items-center py-3">
            <span class="text-sm font-medium text-gray-700">Lê Văn C</span>
            <span class="text-xs font-semibold px-2.5 py-1 bg-gray-100 text-gray-600 rounded-md"
              >A-305</span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
