<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoomStore } from '../stores/roomStore';
import { useStudentStore } from '../stores/studentStore';
import { useBuildingStore } from '../stores/buildingStore.ts';
import { useAuthStore } from '../stores/authStore';
import Swal from 'sweetalert2';
import { useToast } from 'vue-toastification';
import { Plus } from '@lucide/vue';

// Import Child Components
import RoomFilter from '../components/room/RoomFilter.vue';
import RoomTable from '../components/room/RoomTable.vue';
import RoomModal from '../components/room/RoomModal.vue';
import RoomDetailModal from '../components/room/RoomDetailModal.vue';

import type { Room, NewRoom } from '../types/room';
import { getErrorMessage } from '../services/api.ts';

const roomStore = useRoomStore();
const studentStore = useStudentStore();
const buildingStore = useBuildingStore();
const authStore = useAuthStore();
const toast = useToast();

const isLoading = computed(() => roomStore.loading);
const rooms = computed(() => roomStore.rooms);

// State for search and filter[cite: 9]
const searchQuery = ref('');
const selectedBuilding = ref('All');

// Modal visible flags[cite: 9]
const isAddEditModalOpen = ref(false);
const isDetailModalOpen = ref(false);

const editingRoomId = ref<number | null>(null);
const selectedRoom = ref<Room | null>(null);

const newRoom = ref<NewRoom>({
  roomNumber: '',
  buildingCode: '',
  monthlyRent: 50,
  maximumCapacity: 0,
  status: 'ACTIVE',
});

onMounted(async () => {
  await roomStore.fetchRooms();
  await studentStore.fetchStudents();
  await buildingStore.fetchBuildings();
});

const buildingsList = computed(() => buildingStore.buildings.map((b) => b.code));

// Retrieve list of students residing in the selected room[cite: 9]
const studentsInRoom = computed(() => {
  if (!selectedRoom.value) return [];
  return studentStore.students.filter(
    (student) =>
      student.roomNumber &&
      student.buildingCode &&
      selectedRoom.value?.roomNumber === student.roomNumber
  );
});

// Computed filtering[cite: 9]
const filteredRooms = computed((): Room[] => {
  return rooms.value.filter((room) => {
    const matchesSearch = room.roomNumber.includes(searchQuery.value);
    const matchesBuilding =
      selectedBuilding.value === 'All' || room.buildingCode === selectedBuilding.value;
    return matchesSearch && matchesBuilding;
  });
});

const openRoomDetail = (room: Room) => {
  selectedRoom.value = room;
  isDetailModalOpen.value = true;
};

const openAddModal = () => {
  editingRoomId.value = null;
  newRoom.value = {
    roomNumber: '',
    buildingCode: buildingsList.value[0] || '', // Pre-select first building if available
    monthlyRent: 50,
    maximumCapacity: 4,
    status: 'ACTIVE',
  };
  isAddEditModalOpen.value = true;
};

const openEditModal = (room: Room) => {
  editingRoomId.value = room.id;
  newRoom.value = {
    roomNumber: room.roomNumber,
    buildingCode: room.buildingCode,
    monthlyRent: room.monthlyRent,
    maximumCapacity: room.maximumCapacity,
    status: room.status,
  };
  isAddEditModalOpen.value = true;
};

const handleSaveRoom = async () => {
  if (!newRoom.value.roomNumber) {
    toast.error('Please enter room number.');
    return;
  }

  let success = false;
  if (editingRoomId.value) {
    try {
      const result = await roomStore.updateRoom(editingRoomId.value, { ...newRoom.value });
      if (result.success) {
        toast.success(`Updated room successfully!`);
        success = true;
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  } else {
    try {
      const result = await roomStore.addRoom(newRoom.value);
      if (result.success) {
        toast.success(`Added new room successfully!`);
        success = true;
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  }

  if (success) {
    isAddEditModalOpen.value = false;
  }
};

const handleDeleteRoom = async (room: any) => {
  if (room.currentOccupancy > 0) {
    toast.error(`Cannot delete room ${room.roomNumber} because it is currently occupied!`);
    return;
  }

  const result = await Swal.fire({
    title: `Delete room ${room.roomNumber}?`,
    text: 'Once deleted, this action cannot be undone!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Delete',
    cancelButtonText: 'Cancel',
  });

  if (result.isConfirmed) {
    await roomStore.deleteRoom(room.id);
    Swal.fire({
      title: 'Deleted!',
      text: `Deleted room ${room.roomNumber}.`,
      icon: 'success',
      timer: 2000,
    });
  }
};
</script>

<template>
  <div class="p-6 max-w-7xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Rooms</h1>
        <p class="text-sm text-gray-500 mt-1">Manage dormitory rooms, capacity, and monthly rent</p>
      </div>
      <button
        v-if="authStore.userRole === 'Manager'"
        :disabled="isLoading"
        @click="openAddModal"
        class="inline-flex items-center justify-center gap-2 px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors shadow-sm cursor-pointer disabled:cursor-not-allowed disabled:bg-blue-400"
      >
        <Plus :size="20" />
        <span>Add Room</span>
      </button>
    </div>

    <!-- 1. Search & Filter -->
    <RoomFilter
      v-model:searchQuery="searchQuery"
      v-model:selectedBuilding="selectedBuilding"
      :buildingsList="buildingsList"
    />

    <!-- 2. Rooms Table -->
    <RoomTable
      :rooms="filteredRooms"
      :isLoading="isLoading"
      @viewDetail="openRoomDetail"
      @edit="openEditModal"
      @delete="handleDeleteRoom"
    />

    <!-- 3. Add/Edit Room Modal -->
    <RoomModal
      v-model:isOpen="isAddEditModalOpen"
      v-model:roomData="newRoom"
      :editingRoomId="editingRoomId"
      :buildingsList="buildingsList"
      @save="handleSaveRoom"
    />

    <!-- 4. Detail & Occupants Modal -->
    <RoomDetailModal v-model="isDetailModalOpen" :room="selectedRoom" :students="studentsInRoom" />
  </div>
</template>
