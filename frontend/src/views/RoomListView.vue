<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoomStore } from '../stores/roomStore';
import { useStudentStore } from '../stores/studentStore';
import { useBuildingStore } from '../stores/buildingStore.ts';
import { useAuthStore } from '../stores/authStore';
import Swal from 'sweetalert2';
import { useToast } from 'vue-toastification';
import { Plus, Search } from '@lucide/vue';
import type { Room } from '../types/room';
import TableSkeleton, { type SkeletonColumn } from '../components/common/TableSkeleton.vue';

const roomStore = useRoomStore();
const studentStore = useStudentStore();
const buildingStore = useBuildingStore();
const authStore = useAuthStore();
const toast = useToast();

const isLoading = computed(() => roomStore.loading);

const rooms = computed(() => roomStore.rooms);

onMounted(async () => {
  await roomStore.fetchRooms();
  await studentStore.fetchStudents();
  await buildingStore.fetchBuildings();
});

// const buildingsList = ['Building A', 'Building B', 'Building C'];
const buildingsList = computed(() => buildingStore.buildings.map((building) => building.name));
const searchQuery = ref('');
const selectedBuilding = ref('All');

const isAddEditModalOpen = ref(false);
const editingRoomId = ref<number | null>(null);

const newRoom = ref<Room>({
  id: 1,
  roomNumber: '101',
  buildingCode: 'A',
  buildingName: 'Building A',
  monthlyRent: 0,
  maximumCapacity: 0,
  currentOccupancy: 0,
  availableBeds: 0,
  occupancyStatus: 'EMPTY',
  status: 'ACTIVE',
});

const isDetailModalOpen = ref(false);
const selectedRoom = ref<Room | null>(null);

const studentsInRoom = computed(() => {
  if (!selectedRoom.value) return [];

  return studentStore.students.filter((student) => {
    if (!student.roomNumber || !student.buildingCode) return false;

    if (selectedRoom.value?.roomNumber === student.roomNumber) {
      return true;
    }

    return false;
  });
});

const openRoomDetail = (room: Room) => {
  selectedRoom.value = room;
  isDetailModalOpen.value = true;
};

const openAddModal = () => {
  editingRoomId.value = null; // reset editingRoomId
  isAddEditModalOpen.value = true;
};

const openEditModal = (room: Room) => {
  editingRoomId.value = room.id;
  newRoom.value = room;
  isAddEditModalOpen.value = true;
};

const handleSaveRoom = async () => {
  if (!newRoom.value.roomNumber) {
    alert('Vui lòng nhập số phòng!');
    return;
  }

  // Editing room
  if (editingRoomId.value) {
    await roomStore.updateRoom(editingRoomId.value, { ...newRoom.value });
  } else {
    // Add new room
    await roomStore.addRoom({ ...newRoom.value });
  }

  isAddEditModalOpen.value = false;
};

const handleDeleteRoom = async (room: any) => {
  if (room.occupied > 0) {
    toast.error(`Can not delete room ${room.number} because ${room.occupied} occupied!`);
    return;
  }

  const result = await Swal.fire({
    title: `Delete room ${room.number}?`,
    text: 'Once deleted, it can not redo!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Delete',
    cancelButtonText: 'Cancel',
  });

  if (result.isConfirmed) {
    await roomStore.deleteRoom(room.id);

    // Optional: show success toast
    Swal.fire({
      title: 'Deleted!',
      text: `Deleted room ${room.number}.`,
      icon: 'success',
      timer: 2000,
    });
  }
};

const getOccupancyLabel = (occupied: number, capacity: number) => {
  if (occupied === 0) return { text: 'Empty', class: 'bg-gray-100 text-gray-600' };
  if (occupied === capacity)
    return {
      text: 'Full',
      class: 'bg-rose-50 text-rose-700 border border-rose-100',
    };
  return {
    text: 'Available',
    class: 'bg-emerald-50 text-emerald-700 border border-emerald-100',
  };
};

const filteredRooms = computed(() => {
  return rooms.value.filter((room) => {
    const matchesSearch = room.roomNumber.includes(searchQuery.value);
    const matchesBuilding =
      selectedBuilding.value === 'All' || room.buildingName === selectedBuilding.value;
    return matchesSearch && matchesBuilding;
  });
});

const skeletonColumns: SkeletonColumn[] = [
  {
    key: 'room',
    label: 'Room',
    width: '10%',
    skeletonWidth: '50px',
  },
  {
    key: 'building_name',
    label: 'Building Name',
    width: '20%',
    skeletonWidth: '70px',
  },
  {
    key: 'occupacy',
    label: 'Occurpancy',
    width: '20%',
    skeletonWidth: '90px',
  },
  {
    key: 'status',
    label: 'Status',
    width: '20%',
    type: 'badge',
  },
  {
    key: 'rent',
    label: 'Monthly Rent',
    width: '10%',
    type: 'badge',
  },
  {
    key: 'actions',
    label: 'Actions',
    width: '20%',
    align: 'right',
    type: 'actions',
  },
];
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

    <!-- Search & Filter -->
    <div
      class="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex flex-col sm:flex-row gap-4"
    >
      <div class="flex-1 relative">
        <span class="absolute inset-y-0 left-3 flex items-center text-gray-400">
          <Search :size="20" />
        </span>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search room by number..."
          class="w-full pl-9 pr-4 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
        />
      </div>
      <div class="w-full sm:w-56 flex items-center space-x-2">
        <span class="text-sm text-gray-500 whitespace-nowrap">Building:</span>
        <select
          v-model="selectedBuilding"
          class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
        >
          <option value="All">All</option>
          <option v-for="building in buildingsList" :key="building" :value="building">
            {{ building }}
          </option>
        </select>
      </div>
    </div>

    <TableSkeleton v-if="isLoading" :columns="skeletonColumns" />
    <!-- Rooms table -->
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
            <tr
              v-for="r in filteredRooms"
              :key="r.id"
              class="hover:bg-gray-50/70 transition-colors"
            >
              <td class="px-6 py-4 font-semibold text-gray-900">
                {{ r.roomNumber }}
              </td>
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
                  @click="openRoomDetail(r)"
                  class="text-emerald-600 hover:text-emerald-800 text-sm font-medium mr-2"
                >
                  View
                </button>
                <button
                  @click="openEditModal(r)"
                  class="text-blue-600 hover:text-blue-900 mr-3 text-sm font-medium"
                >
                  Edit
                </button>
                <button
                  @click="handleDeleteRoom(r)"
                  class="text-rose-600 hover:text-rose-900 text-sm font-medium"
                >
                  Delete
                </button>
              </td>
            </tr>
            <tr v-if="filteredRooms.length === 0">
              <td colspan="6" class="px-6 py-12 text-center text-gray-400">
                No rooms found matching your filter criteria.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Add/Edit dialog modal -->
    <div
      v-if="isAddEditModalOpen"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm"
    >
      <div
        class="bg-white w-full max-w-md rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col"
      >
        <div class="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-base font-bold text-gray-900">
            {{ editingRoomId ? 'Edit Room' : 'New Room' }}
          </h3>
          <button @click="isAddEditModalOpen = false" class="text-gray-400 hover:text-gray-600">
            ✕
          </button>
        </div>

        <div class="p-6 space-y-4">
          <div>
            <div class="flex items-center gap-1 mb-1">
              <label class="text-xs font-semibold text-gray-600 uppercase"> Room Number </label>
              <span class="text-red-500">*</span>
            </div>
            <input
              v-model="newRoom.roomNumber"
              type="text"
              placeholder="e.g. 101, 202"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <div class="flex items-center gap-1 mb-1">
              <label class="text-xs font-semibold text-gray-600 uppercase"> Building </label>
              <span class="text-red-500">*</span>
            </div>
            <select
              v-model="newRoom.buildingName"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
            >
              <option v-for="building in buildingsList" :key="building" :value="building">
                {{ building }}
              </option>
            </select>
          </div>
          <div>
            <div class="flex items-center gap-1 mb-1">
              <label class="text-xs font-semibold text-gray-600 uppercase">
                Monthly Rent ($)
              </label>
              <span class="text-red-500">*</span>
            </div>
            <input
              v-model.number="newRoom.monthlyRent"
              type="number"
              min="0"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <div class="flex items-center gap-1 mb-1">
              <label class="text-xs font-semibold text-gray-600 uppercase">
                Maximum Capacity (Beds)
              </label>
              <span class="text-red-500">*</span>
            </div>
            <input
              v-model.number="newRoom.maximumCapacity"
              type="number"
              min="1"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Status</label>
            <select
              v-model="newRoom.status"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
            >
              <option value="Active">Active</option>
              <option value="Inactive">Inactive</option>
            </select>
          </div>
        </div>

        <div
          class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex items-center justify-end space-x-3"
        >
          <button
            @click="isAddEditModalOpen = false"
            class="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-800 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
          >
            Cancel
          </button>
          <button
            @click="handleSaveRoom"
            class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm"
          >
            Save
          </button>
        </div>
      </div>
    </div>

    <!-- Detail roo & occutants -->
    <div
      v-if="isDetailModalOpen && selectedRoom"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm animate-fade-in"
    >
      <div
        class="bg-white w-full max-w-2xl rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col transform transition-all"
      >
        <!-- Header -->
        <div
          class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/50"
        >
          <div>
            <h3 class="text-base font-bold text-gray-900">
              Room Details — {{ selectedRoom.buildingName }} / Room
              {{ selectedRoom.roomNumber }}
            </h3>
            <p class="text-xs text-gray-500 mt-0.5">
              Hardware specifications and current occupants list
            </p>
          </div>
          <button
            @click="isDetailModalOpen = false"
            class="text-gray-400 hover:text-gray-600 text-lg cursor-pointer"
          >
            ✕
          </button>
        </div>

        <!-- Nội dung Modal -->
        <div class="p-6 space-y-6 overflow-y-auto max-h-[75vh] text-sm">
          <!-- Phần 1: Thông tin chung của phòng -->
          <div class="space-y-3">
            <h4
              class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
            >
              Room Specifications
            </h4>
            <div class="grid grid-cols-3 gap-4 bg-gray-50 p-3 rounded-lg text-center">
              <div>
                <span class="text-xs text-gray-400 block font-medium">Type</span>
                <span class="font-semibold text-gray-800"> Standard</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium">Status</span>
                <span
                  class="inline-block px-2 py-0.5 text-xs font-semibold rounded-full mt-0.5"
                  :class="
                    selectedRoom.status === 'ACTIVE'
                      ? 'bg-green-100 text-green-700'
                      : 'bg-amber-100 text-amber-700'
                  "
                >
                  {{ selectedRoom.status }}
                </span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium">Occupancy</span>
                <span class="font-bold text-blue-600">
                  {{ selectedRoom.currentOccupancy }}/{{ selectedRoom.maximumCapacity }} Beds
                </span>
              </div>
            </div>
          </div>

          <!-- Table listed student in the room -->
          <div class="space-y-3">
            <h4
              class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
            >
              Current Occupants ({{ selectedRoom.currentOccupancy }})
            </h4>

            <!-- If room is empty -->
            <div
              v-if="selectedRoom.currentOccupancy === 0"
              class="text-center py-8 bg-gray-50 rounded-xl border border-dashed border-gray-200"
            >
              <p class="text-sm text-gray-400 font-medium">This room is currently empty.</p>
              <p class="text-xs text-gray-300 mt-0.5">No students have been assigned here yet.</p>
            </div>

            <!-- If room has student -->
            <div v-else class="overflow-hidden border border-gray-100 rounded-lg shadow-sm">
              <table class="min-w-full divide-y divide-gray-100 text-left text-xs">
                <thead class="bg-gray-50 text-gray-500 uppercase font-semibold tracking-wider">
                  <tr>
                    <th class="px-4 py-2.5">Student Code</th>
                    <th class="px-4 py-2.5">Full Name</th>
                    <th class="px-4 py-2.5">Class/Major</th>
                    <th class="px-4 py-2.5">Phone</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-100 text-gray-700 bg-white font-medium">
                  <tr
                    v-for="student in studentsInRoom"
                    :key="student.id"
                    class="hover:bg-gray-50/80 transition-colors"
                  >
                    <td class="px-4 py-3 font-mono text-blue-600 font-bold">
                      {{ student.studentCode }}
                    </td>
                    <td class="px-4 py-3 font-semibold text-gray-900">
                      {{ student.fullName }}
                    </td>
                    <td class="px-4 py-3 text-gray-500">
                      <div>{{ student.className }}</div>
                      <div class="text-[10px] text-gray-400 font-normal mt-0.5">
                        {{ student.major || 'N/A' }}
                      </div>
                    </td>
                    <td class="px-4 py-3 font-mono text-gray-500">
                      {{ student.phoneNumber || '—' }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- Chân Modal -->
        <div class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-end">
          <button
            @click="isDetailModalOpen = false"
            class="px-5 py-2 bg-white border border-gray-200 text-sm font-medium text-gray-600 rounded-lg hover:bg-gray-50 transition-colors shadow-sm"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
