<script setup lang="ts">
import TableSkeleton, { type SkeletonColumn } from '../common/TableSkeleton.vue';
import type { Student } from '../../types/student';

defineProps<{
  students: Student[];
  isLoading: boolean;
  getBuildingName: (code: string) => string;
}>();

defineEmits<{
  (e: 'view', student: Student): void;
  (e: 'edit', student: Student): void;
  (e: 'delete', student: Student): void;
}>();

const skeletonColumns: SkeletonColumn[] = [
  { key: 'student', label: 'Student', width: '26%', skeletonWidth: '150px' },
  { key: 'id', label: 'ID', width: '14%', skeletonWidth: '70px' },
  { key: 'class', label: 'Class', width: '17%', skeletonWidth: '90px' },
  { key: 'building', label: 'Building Name', width: '15%', skeletonWidth: '100px' },
  { key: 'room', label: 'Room', width: '13%', type: 'badge' },
  { key: 'actions', label: 'Actions', width: '15%', align: 'right', type: 'actions' },
];
</script>

<template>
  <TableSkeleton v-if="isLoading" :columns="skeletonColumns" :rows="5" />

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
            <th class="px-6 py-4">Building Name</th>
            <th class="px-6 py-4">Room</th>
            <th class="px-6 py-4 text-right">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 text-sm text-gray-700">
          <tr v-for="s in students" :key="s.id" class="hover:bg-gray-50/70 transition-colors">
            <td class="px-6 py-4 font-semibold text-gray-900">{{ s.fullName }}</td>
            <td class="px-6 py-4 font-mono text-xs text-gray-500">{{ s.studentCode }}</td>
            <td class="px-6 py-4 text-gray-500">{{ s.className }}</td>
            <td class="px-6 py-4 text-gray-500">
              {{ s.buildingCode ? getBuildingName(s.buildingCode) : '—' }}
            </td>
            <td class="px-6 py-4">
              <span
                :class="
                  s.roomNumber
                    ? 'font-bold text-blue-700 bg-blue-50 px-2 py-1 rounded text-xs border border-blue-100'
                    : 'text-gray-400 font-normal'
                "
              >
                {{ s.roomNumber ?? '—' }}
              </span>
            </td>
            <td class="px-6 py-4 text-right space-x-3 text-xs font-medium">
              <button
                @click="$emit('view', s)"
                class="text-emerald-600 hover:text-emerald-800 font-medium cursor-pointer"
              >
                View
              </button>
              <button
                @click="$emit('edit', s)"
                class="text-blue-600 hover:text-blue-900 font-medium cursor-pointer"
              >
                Edit
              </button>
              <button
                @click="$emit('delete', s)"
                class="text-rose-600 hover:text-rose-900 font-medium cursor-pointer"
              >
                Delete
              </button>
            </td>
          </tr>
          <tr v-if="students.length === 0">
            <td colspan="6" class="px-6 py-12 text-center text-gray-400">
              No students found matching your criteria.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
