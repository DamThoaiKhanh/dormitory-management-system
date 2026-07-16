<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useBuildingStore } from '../stores/buildingStore';
import { useRoomStore } from '../stores/roomStore';
import { useToast } from 'vue-toastification';
import { Search, Plus } from '@lucide/vue';
import TableSkeleton, { type SkeletonColumn } from '../components/common/TableSkeleton.vue';
import type { Building } from '../types/building.ts';
import { getErrorMessage } from '../services/api.ts';
import { X } from '@lucide/vue';

const buildingStore = useBuildingStore();
const roomStore = useRoomStore();
const toast = useToast();

const buildings = computed(() => buildingStore.buildings);
const isLoading = computed(() => buildingStore.loading);

onMounted(async () => {
  await buildingStore.fetchBuildings();
  await roomStore.fetchRooms();
});

const searchQuery = ref('');
const statusFilter = ref('All');
const isOpenDialog = ref(false);
const editingBuildingCode = ref<string | null>(null);

const newBuilding = ref<Building>({
  code: '',
  name: '',
  numberOfFloors: 1,
  status: 'ACTIVE' as 'ACTIVE' | 'INACTIVE',
});

const filteredBuildings = computed((): Building[] => {
  return buildings.value.filter((b) => {
    const matchesSearch =
      b.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      b.code.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesStatus = statusFilter.value === 'All' || b.status === statusFilter.value;
    return matchesSearch && matchesStatus;
  });
});

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

const openEditModal = (building: Building) => {
  editingBuildingCode.value = building.code;
  newBuilding.value = {
    code: building.code,
    name: building.name,
    address: building.address,
    numberOfFloors: building.numberOfFloors,
    description: building.description,
    status: building.status,
  };
  isOpenDialog.value = true;
};

const handleSaveBuilding = async () => {
  if (newBuilding.value.code.trim() === '' || newBuilding.value.name.trim() === '') {
    toast.error('Building code and building name can not leave empty!');
    return;
  }

  if (newBuilding.value.numberOfFloors <= 0) {
    toast.error('Number of floors must be a positive number!');
    return;
  }

  if (editingBuildingCode.value) {
    try {
      const result = await buildingStore.updateBuilding(editingBuildingCode.value, {
        ...newBuilding.value,
      });

      if (result.success) {
        toast.success('Building updated successfully!');
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  } else {
    try {
      const result = await buildingStore.addBuilding({ ...newBuilding.value });
      if (result.success) {
        toast.success('Building created successfully!');
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  }

  isOpenDialog.value = false;
};

const handleDeleteBuilding = async (building: any) => {
  const hasRooms = roomStore.rooms.some(
    (r) => r.buildingCode === building.name || r.buildingCode === `Building ${building.code}`
  );
  if (hasRooms) {
    alert(`Không thể xóa ${building.name} vì đang có phòng trực thuộc tòa nhà này!`);
    return;
  }

  if (confirm(`Bạn có chắc muốn xóa tòa nhà ${building.name}?`)) {
    await buildingStore.deleteBuilding(building.id);
  }
};

const skeletonColumns: SkeletonColumn[] = [
  {
    key: 'code',
    label: 'Code',
    width: '10%',
    skeletonWidth: '150px',
  },
  {
    key: 'building_name',
    label: 'Building Name',
    width: '30%',
    skeletonWidth: '70px',
  },
  {
    key: 'floors',
    label: 'Floors',
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

    <!-- Search anf filter -->
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
          placeholder="Search buildings by code or name..."
          class="w-full pl-9 pr-4 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
        />
      </div>
      <div class="w-full sm:w-48 flex items-center space-x-2">
        <span class="text-sm text-gray-500 whitespace-nowrap">Status:</span>
        <select
          v-model="statusFilter"
          class="w-full px-3 py-2 bg-gray-50 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 focus:bg-white transition-colors"
        >
          <option value="All">All</option>
          <option value="ACTIVE">Active</option>
          <option value="INACTIVE">Inactive</option>
        </select>
      </div>
    </div>

    <!-- Display building table -->
    <TableSkeleton v-if="isLoading" :columns="skeletonColumns" :rows="5" />
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
            <tr
              v-for="b in filteredBuildings"
              :key="b.code"
              class="hover:bg-gray-50/70 transition-colors"
            >
              <td class="px-6 py-4 font-semibold text-gray-900">
                {{ b.code }}
              </td>
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
                <button @click="openEditModal(b)" class="text-blue-600 hover:text-blue-900 mr-2">
                  Edit
                </button>
                <button @click="handleDeleteBuilding(b)" class="text-rose-600 hover:text-rose-900">
                  Delete
                </button>
              </td>
              <td class="px-6 py-4">{{ b.address }}</td>
            </tr>
            <tr v-if="filteredBuildings.length === 0">
              <td colspan="5" class="px-6 py-12 text-center text-gray-400">
                No buildings found matching your criteria.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Add new/ Edit building modal dialog -->
    <div
      v-if="isOpenDialog"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm animate-fade-in"
    >
      <div
        class="bg-white w-full max-w-lg rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col"
      >
        <div class="px-6 py-2 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-base font-bold text-gray-900">
            {{ editingBuildingCode ? 'Edit Building' : 'Add Building' }}
          </h3>
          <button
            @click="isOpenDialog = false"
            class="text-gray-400 p-2 text-lg cursor-pointer hover:text-red-600 hover:bg-gray-100 rounded-full"
          >
            <X :size="20" stroke-width="3" />
          </button>
        </div>

        <div class="p-6 space-y-4 overflow-y-auto max-h-[70vh]">
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
              >Building Code *</label
            >
            <input
              v-model="newBuilding.code"
              :disabled="!!editingBuildingCode"
              type="text"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 disabled:bg-gray-100"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
              >Building Name *</label
            >
            <input
              v-model="newBuilding.name"
              type="text"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Address</label>
            <input
              v-model="newBuilding.address"
              type="text"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
              >Number of floors</label
            >
            <input
              v-model.number="newBuilding.numberOfFloors"
              type="number"
              min="1"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1"
              >Description</label
            >
            <textarea
              v-model="newBuilding.description"
              rows="3"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            ></textarea>
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 uppercase mb-1">Status</label>
            <select
              v-model="newBuilding.status"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
            >
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
          </div>
        </div>

        <div
          class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex items-center justify-end space-x-3"
        >
          <button
            @click="isOpenDialog = false"
            class="px-4 py-2 text-sm font-medium text-gray-600 hover:text-gray-800 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer"
          >
            Cancel
          </button>
          <button
            @click="handleSaveBuilding"
            class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm cursor-pointer"
          >
            Save
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
