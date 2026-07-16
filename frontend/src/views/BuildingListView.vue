<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useBuildingStore } from '../stores/buildingStore';
import { useRoomStore } from '../stores/roomStore';
import { useToast } from 'vue-toastification';
import { Plus } from '@lucide/vue';

// Import Child Components
import BuildingFilter from '../components/building/BuildingFilter.vue';
import BuildingTable from '../components/building/BuildingTable.vue';
import BuildingModal from '../components/building/BuildingModal.vue';

import type { Building } from '../types/building.ts';
import { getErrorMessage } from '../services/api.ts';

// Initialize stores and services
const buildingStore = useBuildingStore();
const roomStore = useRoomStore();
const toast = useToast();

// Reactive State & Filtering
const searchQuery = ref('');
const statusFilter = ref('All');
const isOpenDialog = ref(false);
const editingBuildingCode = ref<string | null>(null);

const buildings = computed(() => buildingStore.buildings);
const isLoading = computed(() => buildingStore.loading);

// Temporary model to bind with Form Modal
const newBuilding = ref<Building>({
  code: '',
  name: '',
  numberOfFloors: 1,
  status: 'ACTIVE',
});

// Load initial data on mount
onMounted(async () => {
  await buildingStore.fetchBuildings();
  await roomStore.fetchRooms();
});

// Computed property to filter data
const filteredBuildings = computed((): Building[] => {
  return buildings.value.filter((b) => {
    const matchesSearch =
      b.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      b.code.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesStatus = statusFilter.value === 'All' || b.status === statusFilter.value;
    return matchesSearch && matchesStatus;
  });
});

// Open Modal for Creating new building
const openAddModal = () => {
  editingBuildingCode.value = null;
  newBuilding.value = {
    code: '',
    name: '',
    numberOfFloors: 1,
    address: '',
    description: '',
    status: 'ACTIVE',
  };
  isOpenDialog.value = true;
};

// Open Modal for Editing
const openEditModal = (building: Building) => {
  editingBuildingCode.value = building.code;
  newBuilding.value = { ...building };
  isOpenDialog.value = true;
};

// Validate and Submit Save Data
const handleSaveBuilding = async (submittedData: Building) => {
  if (submittedData.code.trim() === '' || submittedData.name.trim() === '') {
    toast.error('Building code and building name can not be left empty!');
    return;
  }

  if (submittedData.numberOfFloors <= 0) {
    toast.error('Number of floors must be a positive number!');
    return;
  }

  let success = false;
  if (editingBuildingCode.value) {
    try {
      const result = await buildingStore.updateBuilding(editingBuildingCode.value, submittedData);
      if (result.success) {
        toast.success('Building updated successfully!');
        success = true;
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  } else {
    try {
      const result = await buildingStore.addBuilding(submittedData);
      if (result.success) {
        toast.success('Building created successfully!');
        success = true;
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  }

  if (success) {
    isOpenDialog.value = false;
  }
};

// Handle delete action with validation
const handleDeleteBuilding = async (building: any) => {
  const hasRooms = roomStore.rooms.some(
    (r) => r.buildingCode === building.name || r.buildingCode === `Building ${building.code}`
  );
  if (hasRooms) {
    alert(`Cannot delete ${building.name} because it contains active rooms!`);
    return;
  }

  if (confirm(`Are you sure you want to delete building ${building.name}?`)) {
    await buildingStore.deleteBuilding(building.code);
  }
};
</script>

<template>
  <div class="p-6 max-w-7xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Buildings</h1>
        <p class="text-sm text-gray-500 mt-1">Manage dormitory buildings and structural details</p>
      </div>
      <button
        :disabled="isLoading"
        @click="openAddModal"
        class="inline-flex items-center justify-center px-4 py-2.5 gap-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors shadow-sm cursor-pointer disabled:cursor-not-allowed disabled:bg-blue-400"
      >
        <Plus :size="20" />
        <span>Add Building</span>
      </button>
    </div>

    <!-- Search and Filter Component -->
    <BuildingFilter v-model:searchQuery="searchQuery" v-model:statusFilter="statusFilter" />

    <!-- Display Building Table Component -->
    <BuildingTable
      :buildings="filteredBuildings"
      :isLoading="isLoading"
      @edit="openEditModal"
      @delete="handleDeleteBuilding"
    />

    <!-- Create/Edit Building Dialog Component -->
    <BuildingModal
      :isOpen="isOpenDialog"
      :editingCode="editingBuildingCode"
      :initialData="newBuilding"
      @close="isOpenDialog = false"
      @save="handleSaveBuilding"
    />
  </div>
</template>
