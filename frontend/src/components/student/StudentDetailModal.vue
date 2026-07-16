<script setup lang="ts">
import { X } from '@lucide/vue';
import type { Student } from '../../types/student';

const isOpen = defineModel<boolean>({ required: true });

defineProps<{
  student: Student | null;
  getBuildingName: (code: string) => string;
}>();
</script>

<template>
  <div
    v-if="isOpen && student"
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
          <h3 class="text-base font-bold text-gray-900">Student Profile Detail</h3>
          <p class="text-xs text-gray-500 mt-0.5">Full comprehensive system registry profile</p>
        </div>
        <button
          @click="isOpen = false"
          class="p-1.5 text-gray-400 hover:text-gray-600 rounded-full hover:bg-gray-100 cursor-pointer"
        >
          <X :size="20" stroke-width="2.5" />
        </button>
      </div>

      <!-- Details Content -->
      <div class="p-6 space-y-6 overflow-y-auto max-h-[75vh] text-sm">
        <div class="space-y-3">
          <h4
            class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
          >
            A. Personal Information
          </h4>
          <div class="grid grid-cols-2 gap-x-6 gap-y-3">
            <div>
              <span class="text-xs text-gray-400 block font-medium uppercase">Full Name</span>
              <span class="font-semibold text-gray-900">{{ student.fullName }}</span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium uppercase">Gender</span>
              <span class="text-gray-700 font-medium">{{ student.gender || 'MALE' }}</span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium uppercase">Date of Birth</span>
              <span class="text-gray-700 font-mono">{{ student.dateOfBirth || '—' }}</span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium uppercase">Citizen ID</span>
              <span class="text-gray-700 font-mono">{{ student.citizenId || '—' }}</span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium uppercase">Phone Number</span>
              <span class="text-gray-700 font-mono">{{ student.phoneNumber || '—' }}</span>
            </div>
            <div>
              <span class="text-xs text-gray-400 block font-medium uppercase">Hometown</span>
              <span class="text-gray-700">{{ student.hometown || '—' }}</span>
            </div>
            <div class="col-span-2">
              <span class="text-xs text-gray-400 block font-medium uppercase"
                >Permanent Address</span
              >
              <span class="text-gray-700">{{ student.address || '—' }}</span>
            </div>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 pt-2">
          <div class="space-y-3">
            <h4
              class="text-xs font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-1"
            >
              B. Academic Information
            </h4>
            <div class="space-y-2">
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Student ID</span>
                <span class="font-semibold text-blue-700 font-mono">{{ student.studentCode }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Class</span>
                <span class="text-gray-700 font-medium">{{ student.className || '—' }}</span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Major</span>
                <span class="text-gray-700 font-medium">{{ student.major || '—' }}</span>
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
                <span class="block text-xs font-medium uppercase text-gray-400">Room Number</span>
                <span
                  class="mt-1 inline-block rounded px-2.5 py-1 text-xs font-bold"
                  :class="
                    student.roomNumber
                      ? 'bg-blue-50 text-blue-700 border border-blue-100'
                      : 'bg-gray-100 text-gray-400'
                  "
                >
                  {{ student.roomNumber ? `Room ${student.roomNumber}` : 'Not Assigned' }}
                </span>
              </div>
              <div>
                <span class="text-xs text-gray-400 block font-medium uppercase">Building Code</span>
                <span
                  class="inline-block mt-1 px-2.5 py-1 text-xs font-semibold rounded bg-gray-100 text-gray-700"
                >
                  {{ student.buildingCode ? getBuildingName(student.buildingCode) : '—' }}
                </span>
              </div>
            </div>
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
