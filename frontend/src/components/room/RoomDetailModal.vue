<script setup lang="ts">
import type { Room } from '../../types/room';

const isOpen = defineModel<boolean>({ required: true });

defineProps<{
  room: Room | null;
  students: any[];
}>();
</script>

<template>
  <div
    v-if="isOpen && room"
    class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm"
  >
    <div
      class="bg-white w-full max-w-2xl rounded-xl shadow-xl border border-gray-100 overflow-hidden flex flex-col"
    >
      <!-- Header -->
      <div
        class="px-6 py-4 border-b border-gray-100 flex items-center justify-between bg-gray-50/50"
      >
        <div>
          <h3 class="text-base font-bold text-gray-900">
            Room Details — {{ room.buildingName }} / Room {{ room.roomNumber }}
          </h3>
          <p class="text-xs text-gray-500 mt-0.5">
            Hardware specifications and current occupants list
          </p>
        </div>
        <button
          @click="isOpen = false"
          class="text-gray-400 hover:text-gray-600 text-lg cursor-pointer"
        >
          ✕
        </button>
      </div>

      <!-- Specs & Students Table -->
      <div class="p-6 space-y-6 overflow-y-auto max-h-[75vh] text-sm">
        <div class="space-y-3">
          <h4
            class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
          >
            Room Specifications
          </h4>
          <div class="grid grid-cols-3 gap-4 bg-gray-50 p-3 rounded-lg text-center">
            <div>
              <span class="text-xs text-gray-400 block font-medium">Type</span>
              <span class="font-semibold text-gray-800">Standard</span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium">Status</span>
              <span
                class="inline-block px-2 py-0.5 text-xs font-semibold rounded-full mt-0.5"
                :class="
                  room.status === 'ACTIVE'
                    ? 'bg-green-100 text-green-700'
                    : 'bg-amber-100 text-amber-700'
                "
              >
                {{ room.status }}
              </span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium">Occupancy</span>
              <span class="font-bold text-blue-600"
                >{{ room.currentOccupancy }}/{{ room.maximumCapacity }} Beds</span
              >
            </div>
          </div>
        </div>

        <div class="space-y-3">
          <h4
            class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
          >
            Current Occupants ({{ room.currentOccupancy }})
          </h4>

          <div
            v-if="room.currentOccupancy === 0"
            class="text-center py-8 bg-gray-50 rounded-xl border border-dashed border-gray-200"
          >
            <p class="text-sm text-gray-400 font-medium">This room is currently empty.</p>
            <p class="text-xs text-gray-300 mt-0.5">No students have been assigned here yet.</p>
          </div>

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
                  v-for="student in students"
                  :key="student.id"
                  class="hover:bg-gray-50/80 transition-colors"
                >
                  <td class="px-4 py-3 font-mono text-blue-600 font-bold">
                    {{ student.studentCode }}
                  </td>
                  <td class="px-4 py-3 font-semibold text-gray-900">{{ student.fullName }}</td>
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

      <!-- Footer -->
      <div class="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-end">
        <button
          @click="isOpen = false"
          class="px-5 py-2 bg-white border border-gray-200 text-sm font-medium text-gray-600 rounded-lg hover:bg-gray-50 transition-colors shadow-sm cursor-pointer"
        >
          Close
        </button>
      </div>
    </div>
  </div>
</template>
