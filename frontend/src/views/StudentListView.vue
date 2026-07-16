<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStudentStore } from '../stores/studentStore';
import { useBuildingStore } from '../stores/buildingStore.ts';
import { useRoomStore } from '../stores/roomStore';
import Swal from 'sweetalert2';
import { Plus } from '@lucide/vue';
import type { Student } from '../types/student';

// Imports
import StudentFilter from '../components/student/StudentFilter.vue';
import StudentTable from '../components/student/StudentTable.vue';
import StudentDetailModal from '../components/student/StudentDetailModal.vue';

const router = useRouter();
const studentStore = useStudentStore();
const buildingStore = useBuildingStore();
const roomStore = useRoomStore();

const searchQuery = ref('');
const selectedBuilding = ref('All');
const isDetailModalOpen = ref(false);
const selectedStudentDetail = ref<Student | null>(null);

onMounted(async () => {
  await Promise.all([
    studentStore.fetchStudents(),
    buildingStore.fetchBuildings(),
    roomStore.fetchRooms(),
  ]);
});

const isLoading = computed(() => studentStore.loading);
const students = computed(() => studentStore.students);
const buildingCodeList = computed(() => buildingStore.buildings.map((b) => b.code));

const getBuildingNameByCode = (code: string) => {
  return buildingStore.buildings.find((b) => b.code === code)?.name || code;
};

// Lọc kết quả tìm kiếm học sinh
const filteredStudents = computed(() => {
  return students.value.filter((s) => {
    const matchesSearch =
      s.fullName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      s.studentCode.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesBuilding =
      selectedBuilding.value === 'All' || s.buildingCode === selectedBuilding.value;
    return matchesSearch && matchesBuilding;
  });
});

const handleView = (student: Student) => {
  selectedStudentDetail.value = student;
  isDetailModalOpen.value = true;
};

const handleEdit = (student: Student) => {
  router.push(`/students/edit/${student.id}`);
};

const handleDelete = async (student: Student) => {
  const result = await Swal.fire({
    title: 'Remove student',
    text: `Do you want to delete ${student.fullName}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, delete',
    cancelButtonText: 'Cancel',
    confirmButtonColor: '#d33',
  });

  if (result.isConfirmed) {
    const delResult = await studentStore.deleteStudent(student.id);
    if (delResult.success) {
      Swal.fire('Deleted!', `${student.fullName} has been removed.`, 'success');
      await studentStore.fetchStudents(); // Cập nhật lại danh sách sau khi xóa thành công
    } else {
      Swal.fire('Error', delResult.error?.message || 'Failed to delete', 'error');
    }
  }
};
</script>

<template>
  <div class="p-6 max-w-7xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Students</h1>
        <p class="text-sm text-gray-500 mt-1">
          Manage student profiles and dormitory room assignments
        </p>
      </div>
      <button
        @click="router.push('/students/new')"
        class="inline-flex items-center justify-center px-4 py-2.5 gap-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors shadow-sm cursor-pointer"
      >
        <Plus :size="20" />
        <span>Add Student</span>
      </button>
    </div>

    <!-- Filter -->
    <StudentFilter
      v-model:searchQuery="searchQuery"
      v-model:selectedBuilding="selectedBuilding"
      :buildingCodeList="buildingCodeList"
    />

    <!-- Table -->
    <StudentTable
      :students="filteredStudents"
      :isLoading="isLoading"
      :getBuildingName="getBuildingNameByCode"
      @view="handleView"
      @edit="handleEdit"
      @delete="handleDelete"
    />

    <!-- Detail Modal -->
    <StudentDetailModal
      v-model="isDetailModalOpen"
      :student="selectedStudentDetail"
      :getBuildingName="getBuildingNameByCode"
    />
  </div>
</template>
