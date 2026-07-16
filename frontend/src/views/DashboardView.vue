<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useBuildingStore } from '../stores/buildingStore';
import { useRoomStore } from '../stores/roomStore';
import { useStudentStore } from '../stores/studentStore';

// Imports components con
import KpiCards from '../components/dashboard/KpiCards.vue';
import OccupancyProgress from '../components/dashboard/OccupancyProgress.vue';
import RecentStudents from '../components/dashboard/RecentStudents.vue';

const buildingStore = useBuildingStore();
const roomStore = useRoomStore();
const studentStore = useStudentStore();

onMounted(async () => {
  await Promise.all([
    buildingStore.fetchBuildings(),
    roomStore.fetchRooms(),
    studentStore.fetchStudents(),
  ]);
});

// Thống kê chung
const totalBuildings = computed(() => buildingStore.buildings.length);
const totalStudents = computed(() => studentStore.students.length);
const totalRooms = computed(() => roomStore.rooms.length);

const totalCapacity = computed(() => {
  return roomStore.rooms.reduce((sum, room) => sum + (room.maximumCapacity || 0), 0);
});

const totalOccupied = computed(() => {
  return roomStore.rooms.reduce((sum, room) => sum + (room.currentOccupancy || 0), 0);
});

const occupancyRate = computed(() => {
  if (totalCapacity.value === 0) return 0;
  return Math.round((totalOccupied.value / totalCapacity.value) * 100);
});

// Calculate the room occupancy by building
const buildingOccupancyData = computed(() => {
  return buildingStore.buildings.map((building) => {
    const buildingRooms = roomStore.rooms.filter((r) => r.buildingCode === building.code);
    const capacity = buildingRooms.reduce((sum, r) => sum + (r.maximumCapacity || 0), 0);
    const occupied = buildingRooms.reduce((sum, r) => sum + (r.currentOccupancy || 0), 0);
    const rate = capacity > 0 ? Math.round((occupied / capacity) * 100) : 0;

    return {
      name: building.name,
      rate,
    };
  });
});

const displayRecentStudents = computed(() => {
  // Simulate 3 first students
  return studentStore.students.slice(0, 3).map((s) => ({
    id: s.id,
    fullName: s.fullName,
    roomCode: s.buildingCode && s.roomNumber ? `${s.buildingCode}-${s.roomNumber}` : 'Unassigned',
  }));
});
</script>

<template>
  <div class="p-6 max-w-7xl mx-auto space-y-6">
    <!-- Header -->
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
      <p class="text-sm text-gray-500 mt-1">Overview of the dormitory system</p>
    </div>

    <!-- KPI Widgets -->
    <KpiCards
      :totalStudents="totalStudents"
      :totalRooms="totalRooms"
      :totalBuildings="totalBuildings"
      :occupancyRate="occupancyRate"
      :totalOccupied="totalOccupied"
      :totalCapacity="totalCapacity"
    />

    <!-- Bottom charts & list -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <OccupancyProgress :buildingProgress="buildingOccupancyData" />
      <RecentStudents :recentStudents="displayRecentStudents" />
    </div>
  </div>
</template>
