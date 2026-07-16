<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useStaffStore } from '../stores/staffStore';
import { useBuildingStore } from '../stores/buildingStore';
import { useToast } from 'vue-toastification';
import Swal from 'sweetalert2';
import { Plus } from '@lucide/vue';
import { getErrorMessage } from '../services/api';
import type { NewStaff, Staff } from '../types/staff';

// Imports Components
import StaffFilter from '../components/staff/StaffFilter.vue';
import StaffTable from '../components/staff/StaffTable.vue';
import StaffFormModal from '../components/staff/StaffFormModal.vue';

const staffStore = useStaffStore();
const buildingStore = useBuildingStore();
const toast = useToast();

const searchQuery = ref('');
const selectedBuilding = ref('All');
const isModalOpen = ref(false);
const editingStaffId = ref<number | null>(null);

const currentStaffData = ref<NewStaff>({
  employeeCode: '',
  fullName: '',
  gender: 'MALE',
  dateOfBirth: '',
  citizenId: '',
  phoneNumber: '',
  address: '',
  assignedBuildingCode: '',
});

onMounted(async () => {
  try {
    await Promise.all([staffStore.fetchStaff(), buildingStore.fetchBuildings()]);
  } catch (error) {
    toast.error(getErrorMessage(error));
  }
});

const staffList = computed(() => staffStore.staffList);
const buildingsList = computed(() => buildingStore.buildings.map((b) => b.code));

const isEditMode = computed(() => editingStaffId.value !== null);

const filteredStaff = computed(() => {
  return staffList.value.filter((s) => {
    const matchesSearch =
      s.fullName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      s.employeeCode.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      s.phoneNumber.includes(searchQuery.value);

    const matchesBuilding =
      selectedBuilding.value === 'All' || s.assignedBuildingCode === selectedBuilding.value;

    return matchesSearch && matchesBuilding;
  });
});

const openAddModal = () => {
  editingStaffId.value = null;
  currentStaffData.value = {
    employeeCode: '',
    fullName: '',
    gender: 'MALE',
    dateOfBirth: '',
    citizenId: '',
    phoneNumber: '',
    address: '',
    assignedBuildingCode: 'None',
  };
  isModalOpen.value = true;
};

const openEditModal = (staff: Staff) => {
  editingStaffId.value = staff.id;
  currentStaffData.value = {
    employeeCode: staff.employeeCode,
    fullName: staff.fullName,
    gender: staff.gender,
    dateOfBirth: staff.dateOfBirth,
    citizenId: staff.citizenId,
    phoneNumber: staff.phoneNumber,
    address: staff.address,
    assignedBuildingCode: staff.assignedBuildingCode || 'None',
  };
  isModalOpen.value = true;
};

const handleSaveStaff = async () => {
  if (!currentStaffData.value.fullName.trim()) {
    toast.error('Name cannot be left empty!');
    return;
  }

  try {
    let result;
    if (isEditMode.value && editingStaffId.value) {
      result = await staffStore.updateStaff(editingStaffId.value, { ...currentStaffData.value });
    } else {
      result = await staffStore.addStaff(currentStaffData.value);
    }

    if (result.success) {
      toast.success(
        `${isEditMode.value ? 'Updated' : 'Added'} staff ${result.data.fullName} successfully!`
      );
      isModalOpen.value = false;
    } else {
      toast.error(result.error.message);
    }
  } catch (error) {
    toast.error(getErrorMessage(error));
  }
};

const handleDeleteStaff = async (staff: Staff) => {
  const confirmResult = await Swal.fire({
    title: 'Delete Staff',
    text: `Are you sure you want to remove employee ${staff.fullName}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, delete',
    cancelButtonText: 'Cancel',
    confirmButtonColor: '#d33',
  });

  if (confirmResult.isConfirmed) {
    try {
      const result = await staffStore.deleteStaff(staff.id!);
      if (result.success) {
        Swal.fire('Success', 'Staff member deleted.', 'success');
      } else {
        Swal.fire('Failed', result.error?.message || 'Delete operation failed', 'error');
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  }
};
</script>

<template>
  <div class="p-6 max-w-7xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Staff Management</h1>
        <p class="text-sm text-gray-500 mt-1">Manage dormitory managers and building assignments</p>
      </div>
      <button
        @click="openAddModal"
        class="inline-flex items-center justify-center px-4 py-2.5 gap-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors shadow-sm cursor-pointer"
      >
        <Plus :size="20" />
        <span>Add Staff</span>
      </button>
    </div>

    <!-- Filters component (Dumb) -->
    <StaffFilter
      v-model:searchQuery="searchQuery"
      v-model:selectedBuilding="selectedBuilding"
      :buildingsList="buildingsList"
    />

    <!-- Table component (Dumb) -->
    <StaffTable :staffList="filteredStaff" @edit="openEditModal" @delete="handleDeleteStaff" />

    <!-- Form modal (Dumb) -->
    <StaffFormModal
      v-model="isModalOpen"
      v-model:staffData="currentStaffData"
      :isEditMode="isEditMode"
      :buildingsList="buildingsList"
      @save="handleSaveStaff"
    />
  </div>
</template>
