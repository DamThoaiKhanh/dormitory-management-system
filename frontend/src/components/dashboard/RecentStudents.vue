<script setup lang="ts">
interface DisplayStudent {
  id: number;
  fullName: string;
  roomCode: string;
}

defineProps<{
  recentStudents: DisplayStudent[];
}>();
</script>

<template>
  <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
    <h2 class="text-base font-bold text-gray-800">Recent student check-ins</h2>
    <div class="divide-y divide-gray-100">
      <div
        v-for="student in recentStudents"
        :key="student.id"
        class="flex justify-between items-center py-3"
      >
        <span class="text-sm font-medium text-gray-700">{{ student.fullName }}</span>
        <span class="text-xs font-semibold px-2.5 py-1 bg-gray-100 text-gray-600 rounded-md">
          {{ student.roomCode }}
        </span>
      </div>
      <div v-if="recentStudents.length === 0" class="py-6 text-center text-gray-400 text-sm">
        No check-ins recorded.
      </div>
    </div>
  </div>
</template>
