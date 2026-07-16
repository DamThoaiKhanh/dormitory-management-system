<script setup lang="ts">
import type { Staff } from '../../types/staff';

defineProps<{
  staffList: Staff[];
}>();

defineEmits<{
  (e: 'edit', staff: Staff): void;
  (e: 'delete', staff: Staff): void;
}>();
</script>

<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr
            class="bg-gray-50 border-b border-gray-100 text-xs font-semibold text-gray-500 uppercase tracking-wider"
          >
            <th class="px-6 py-4">Name</th>
            <th class="px-6 py-4">Code</th>
            <th class="px-6 py-4">Phone Number</th>
            <th class="px-6 py-4">Assigned Building</th>
            <th class="px-6 py-4 text-right">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 text-sm text-gray-700">
          <tr v-for="s in staffList" :key="s.id" class="hover:bg-gray-50/70 transition-colors">
            <td class="px-6 py-4 font-semibold text-gray-900">
              {{ s.fullName }}
            </td>
            <td class="px-6 py-4 text-gray-500 font-mono text-xs">{{ s.employeeCode }}</td>
            <td class="px-6 py-4 text-gray-500 font-mono text-xs">{{ s.phoneNumber }}</td>
            <td class="px-6 py-4">
              <span
                :class="
                  s.assignedBuildingName === 'None' || !s.assignedBuildingCode
                    ? 'text-gray-400 italic'
                    : 'font-medium text-gray-950'
                "
              >
                {{ s.assignedBuildingName || 'None' }}
              </span>
            </td>
            <td class="px-6 py-4 text-right space-x-3 text-xs font-medium">
              <button
                @click="$emit('edit', s)"
                class="text-blue-600 hover:text-blue-900 cursor-pointer"
              >
                Edit
              </button>
              <button
                @click="$emit('delete', s)"
                class="text-rose-600 hover:text-rose-900 cursor-pointer"
              >
                Delete
              </button>
            </td>
          </tr>
          <tr v-if="staffList.length === 0">
            <td colspan="5" class="px-6 py-12 text-center text-gray-400">
              No staff members found matching the search filters.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
