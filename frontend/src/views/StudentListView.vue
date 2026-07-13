<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStudentStore } from '../stores/studentStore';
import { useRoomStore } from '../stores/roomStore';
import apiClient from '../services/api';
import Swal from 'sweetalert2';
import { Search, X, Plus } from '@lucide/vue';
import type { Student } from '../types/student';
import TableSkeleton, { type SkeletonColumn } from '../components/common/TableSkeleton.vue';

const router = useRouter();
const studentStore = useStudentStore();
const roomStore = useRoomStore();

const students = computed(() => studentStore.students);
const isLoading = computed(() => studentStore.loading);

onMounted(async () => {
  await studentStore.fetchStudents();
});

const searchQuery = ref('');
const selectedBuilding = ref('All');

const filteredStudents = computed(() => {
  return students.value.filter((s) => {
    const matchesSearch =
      s.fullName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      s.studentCode.toLowerCase().includes(searchQuery.value.toLowerCase());

    let matchesBuilding = true;
    if (selectedBuilding.value !== 'All') {
      if (selectedBuilding.value === 'Building A') matchesBuilding = s.buildingCode === 'A';
      else if (selectedBuilding.value === 'Building B') matchesBuilding = s.buildingCode === 'B';
      else if (selectedBuilding.value === 'Building C') matchesBuilding = s.buildingCode === 'C';
    }

    return matchesSearch && matchesBuilding;
  });
});

const goToAddStudent = () => {
  router.push('/students/new');
};

// View detail states
const isDetailModalOpen = ref(false);
const selectedStudentDetail = ref<Student | null>(null);

const openDetailModal = (student: Student) => {
  selectedStudentDetail.value = student;
  isDetailModalOpen.value = true;
};

const closeDetailModal = () => {
  selectedStudentDetail.value = null;
  isDetailModalOpen.value = false;
};

const gotoEditStudent = (student: Student) => {
  router.push(`/students/edit/${student.id}`);
};

const handleDeleteStudent = async (student: any) => {
  const result = await Swal.fire({
    title: 'Remove student',
    text: `Do you want to delete ${student.name}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, delete',
    cancelButtonText: 'Cancel',
  });

  if (result.isConfirmed) {
    try {
      // Return bed (if student has room)
      if (student.roomCode && student.roomCode !== '—') {
        const parts = student.roomCode.split('-');
        const bCode = 'Building ' + parts[0];
        const rNum = parts[1];

        await roomStore.fetchRooms();
        const targetRoom = roomStore.rooms.find(
          (r) => r.roomNumber === rNum && r.buildingCode === bCode
        );

        if (targetRoom && targetRoom.currentOccupancy > 0) {
          await apiClient.patch(`/rooms/${targetRoom.id}`, {
            occupied: targetRoom.currentOccupancy - 1,
          });
        }
      }

      // Delete student
      await studentStore.deleteStudent(student.id);

      // Refresh student list - THIS IS IMPORTANT
      await studentStore.fetchStudents();

      // Show success message
      Swal.fire({
        title: 'Deleted!',
        text: `${student.name} has been removed.`,
        icon: 'success',
        timer: 1500,
      });
    } catch (err) {
      console.error(err);
      Swal.fire({
        title: 'Error',
        text: 'Failed to delete student',
        icon: 'error',
      });
    }
  }
};

const studentSkeletonColumns: SkeletonColumn[] = [
  {
    key: 'student',
    label: 'Student',
    width: '26%',
    skeletonWidth: '150px',
  },
  {
    key: 'id',
    label: 'ID',
    width: '14%',
    skeletonWidth: '70px',
  },
  {
    key: 'class',
    label: 'Class',
    width: '17%',
    skeletonWidth: '90px',
  },
  {
    key: 'room',
    label: 'Room',
    width: '13%',
    type: 'badge',
  },
  {
    key: 'actions',
    label: 'Actions',
    width: '30%',
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
        <h1 class="text-2xl font-bold text-gray-900">Students</h1>
        <p class="text-sm text-gray-500 mt-1">
          Manage student profiles and dormitory room assignments
        </p>
      </div>
      <button
        :disabled="isLoading"
        @click="goToAddStudent"
        class="inline-flex items-center justify-center px-4 py-2.5 gap-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors shadow-sm cursor-pointer disabled:cursor-not-allowed disabled:bg-blue-400"
      >
        <Plus :size="20" />
        <span>Add student</span>
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
          placeholder="Search by student name or ID..."
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
          <option value="Building A">Building A</option>
          <option value="Building B">Building B</option>
          <option value="Building C">Building C</option>
        </select>
      </div>
    </div>

    <!-- Show skeleton -->
    <TableSkeleton v-if="isLoading" :columns="studentSkeletonColumns" :rows="5" />

    <!-- Students table -->
    <div v-else class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse">
          <thead>
            <tr
              class="bg-gray-50 border-b border-gray-100 text-xs font-semibold text-gray-500 uppercase tracking-wider"
            >
              <th class="px-6 py-4">Student</th>
              <th class="px-6 py-4">ID</th>
              <th class="px-6 py-4">Class</th>
              <th class="px-6 py-4">Room</th>
              <th class="px-6 py-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100 text-sm text-gray-700">
            <tr
              v-for="s in filteredStudents"
              :key="s.id"
              class="hover:bg-gray-50/70 transition-colors"
            >
              <td class="px-6 py-4 font-semibold text-gray-900">
                {{ s.fullName }}
              </td>
              <td class="px-6 py-4 text-gray-500">{{ s.studentCode }}</td>
              <td class="px-6 py-4 text-gray-500">{{ s.className }}</td>
              <td class="px-6 py-4">
                <span
                  :class="
                    s.roomNumber != null
                      ? 'font-medium text-gray-900 bg-gray-100 px-2 py-1 rounded'
                      : 'text-gray-400 font-normal'
                  "
                >
                  {{ s.roomNumber ?? '—' }}
                </span>
              </td>
              <td class="px-6 py-4 text-right space-x-3 text-xs font-medium">
                <button
                  @click="openDetailModal(s)"
                  class="text-emerald-600 hover:text-emerald-800 text-sm font-medium mr-2"
                >
                  View
                </button>
                <button
                  @click="gotoEditStudent(s)"
                  class="text-blue-600 hover:text-blue-900 text-sm font-medium"
                >
                  Edit
                </button>
                <button
                  @click="handleDeleteStudent(s)"
                  class="text-rose-600 hover:text-rose-900 text-sm font-medium"
                >
                  Delete
                </button>
              </td>
            </tr>
            <tr v-if="filteredStudents.length === 0">
              <td colspan="5" class="px-6 py-12 text-center text-gray-400">
                No students found matching your criteria.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- View student detail -->
    <div
      v-if="isDetailModalOpen && selectedStudentDetail"
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
            <h3 class="text-base font-bold text-gray-900">Student Profile Detail</h3>
            <p class="text-xs text-gray-500 mt-0.5">Full comprehensive system registry profile</p>
          </div>
          <button
            @click="closeDetailModal"
            class="p-2 text-gray-400 hover:text-red-600 rounded-full hover:bg-gray-100 transition-colors focus:outline-none focus:ring-2 focus:ring-red-500"
            aria-label="Close modal"
          >
            <X :size="20" stroke-width="3" />
          </button>
        </div>

        <div class="p-6 space-y-6 overflow-y-auto max-h-[75vh] text-sm">
          <!-- Personal Information -->
          <div class="space-y-3">
            <h4
              class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
            >
              A. Personal Information
            </h4>
            <div class="grid grid-cols-2 gap-x-6 gap-y-3">
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Full Name</span>
                <span class="font-semibold text-gray-900">{{
                  selectedStudentDetail.fullName
                }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Gender</span>
                <span class="text-gray-700">{{ selectedStudentDetail.gender || 'Male' }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Date of Birth</span>
                <span class="text-gray-700">{{ selectedStudentDetail.dateOfBirth || '—' }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Citizen ID</span>
                <span class="text-gray-700 font-mono">{{
                  selectedStudentDetail.citizenId || '—'
                }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Phone Number</span>
                <span class="text-gray-700 font-mono">{{
                  selectedStudentDetail.phoneNumber || '—'
                }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Hometown</span>
                <span class="text-gray-700">{{ selectedStudentDetail.hometown || '—' }}</span>
              </div>
              <div class="col-span-2">
                <span class="text-xs text-gray-400 block font-medium uppercase"
                  >Permanent Address</span
                >
                <span class="text-gray-700">{{ selectedStudentDetail.address || '—' }}</span>
              </div>
            </div>
          </div>

          <!-- Academic & Room Info -->
          <div class="grid grid-cols-2 gap-6 pt-2">
            <div class="space-y-3">
              <h4
                class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
              >
                B. Academic Information
              </h4>
              <div class="space-y-2">
                <div>
                  <span class="text-xs text-gray-400 block font-medium uppercase">Student ID</span>
                  <span class="font-semibold text-blue-700 font-mono">{{
                    selectedStudentDetail.studentCode
                  }}</span>
                </div>
                <div>
                  <span class="text-xs text-gray-400 block font-medium uppercase">Class</span>
                  <span class="text-gray-700">{{ selectedStudentDetail.className }}</span>
                </div>
                <div>
                  <span class="text-xs text-gray-400 block font-medium uppercase">Major</span>
                  <span class="text-gray-700">{{ selectedStudentDetail.major || '—' }}</span>
                </div>
              </div>
            </div>

            <div class="space-y-3">
              <h4
                class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
              >
                C. Room Assignment
              </h4>
              <div class="space-y-2">
                <div>
                  <span class="block text-xs font-medium uppercase text-gray-400">
                    Room Number
                  </span>
                  <span
                    class="mt-0.5 inline-block rounded px-2.5 py-1 text-xs font-bold"
                    :class="
                      selectedStudentDetail.roomNumber
                        ? 'border border-blue-100 bg-blue-50 text-blue-700'
                        : 'bg-gray-100 text-gray-400'
                    "
                  >
                    {{ selectedStudentDetail.roomNumber ?? '-' }}
                  </span>
                </div>
                <div>
                  <span class="text-xs text-gray-400 block font-medium uppercase"
                    >Building Code</span
                  >
                  <span
                    class="inline-block mt-0.5 px-2.5 py-1 text-xs font-bold rounded"
                    :class="'bg-gray-100 text-gray-400'"
                  >
                    {{ selectedStudentDetail.buildingCode ?? '-' }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Close button -->
        <div class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-end">
          <button
            @click="closeDetailModal"
            class="px-5 py-2 bg-white border border-gray-200 text-sm font-medium text-gray-600 rounded-lg hover:bg-gray-50 transition-colors shadow-sm cursor-pointer"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
