<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type { NewStudent } from '../../types/student';
import type { Room } from '../../types/room';

const studentData = defineModel<NewStudent>({ required: true });

const props = defineProps<{
  buildings: { code: string; name: string }[];
  rooms: Room[];
  isEditMode: boolean;
}>();

const emit = defineEmits<{
  (e: 'save'): void;
  (e: 'cancel'): void;
}>();

// local states
const selectedBuildingCode = ref('');
const errors = ref({ fullName: '', studentCode: '' });

// Filter room base on current selected building
const availableRooms = computed(() => {
  if (!selectedBuildingCode.value) return [];
  return props.rooms.filter(
    (room) =>
      room.buildingCode === selectedBuildingCode.value &&
      (room.currentOccupancy < room.maximumCapacity || room.id === studentData.value.roomId) && // Giữ lại phòng cũ của chính học sinh đó khi Edit
      room.status === 'ACTIVE'
  );
});

// Watch current selected building code -> Reset select room option
watch(selectedBuildingCode, (newVal) => {
  const currentRoom = props.rooms.find((r) => r.id === studentData.value.roomId);
  if (currentRoom && currentRoom.buildingCode !== newVal) {
    studentData.value.roomId = 0; // reset
  }
});

// Đồng bộ ngược lại nếu load dữ liệu cũ lúc Edit
watch(
  () => studentData.value.roomId,
  (roomId) => {
    if (roomId) {
      const room = props.rooms.find((r) => r.id === roomId);
      if (room && !selectedBuildingCode.value) {
        selectedBuildingCode.value = room.buildingCode;
      }
    }
  },
  { immediate: true }
);

const validateAndSubmit = () => {
  let isValid = true;
  errors.value = { fullName: '', studentCode: '' };

  if (!studentData.value.fullName.trim()) {
    errors.value.fullName = 'Full name is required.';
    isValid = false;
  }

  const studentIdRegex = /^[A-Z0-9]{4,12}$/;
  if (!studentData.value.studentCode.trim()) {
    errors.value.studentCode = 'Student ID is required.';
    isValid = false;
  } else if (!studentIdRegex.test(studentData.value.studentCode.toUpperCase())) {
    errors.value.studentCode = 'Student ID must be uppercase alphanumeric (4-12 chars).';
    isValid = false;
  }

  if (isValid) {
    emit('save');
  }
};
</script>

<template>
  <div class="space-y-6">
    <!-- A. Personal information -->
    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
      <h2
        class="text-sm font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-2"
      >
        A. Personal information
      </h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Full Name *</label>
          <input
            v-model="studentData.fullName"
            type="text"
            placeholder="Nguyễn Văn A"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
          <p v-if="errors.fullName" class="text-xs text-rose-500 mt-1">{{ errors.fullName }}</p>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Gender</label>
          <select
            v-model="studentData.gender"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option value="MALE">MALE</option>
            <option value="FEMALE">FEMALE</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Date of Birth</label>
          <input
            v-model="studentData.dateOfBirth"
            type="date"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Citizen ID</label>
          <input
            v-model="studentData.citizenId"
            type="text"
            placeholder="012345678901"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div class="md:col-span-2">
          <label class="block text-xs font-semibold text-gray-600 mb-1">Phone Number</label>
          <input
            v-model="studentData.phoneNumber"
            type="text"
            placeholder="0912345678"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div class="md:col-span-2">
          <label class="block text-xs font-semibold text-gray-600 mb-1">Address</label>
          <textarea
            v-model="studentData.address"
            rows="2"
            placeholder="Permanent residence address..."
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          ></textarea>
        </div>
        <div class="md:col-span-2">
          <label class="block text-xs font-semibold text-gray-600 mb-1">Hometown</label>
          <input
            v-model="studentData.hometown"
            type="text"
            placeholder="Hanoi"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
      </div>
    </div>

    <!-- B. Academic information -->
    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
      <h2
        class="text-sm font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-2"
      >
        B. Academic information
      </h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Student Code *</label>
          <input
            v-model="studentData.studentCode"
            type="text"
            placeholder="e.g B20DCAT001"
            :disabled="isEditMode"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 disabled:bg-gray-100"
          />
          <p v-if="errors.studentCode" class="text-xs text-rose-500 mt-1">
            {{ errors.studentCode }}
          </p>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Class</label>
          <input
            v-model="studentData.className"
            type="text"
            placeholder="e.g. CNTT-01"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Major</label>
          <input
            v-model="studentData.major"
            type="text"
            placeholder="Computer Science"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
          />
        </div>
      </div>
    </div>

    <!-- C. Room assignment -->
    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
      <h2
        class="text-sm font-bold text-blue-600 uppercase tracking-wider border-b border-gray-100 pb-2"
      >
        C. Room assignment
      </h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Building</label>
          <select
            v-model="selectedBuildingCode"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
          >
            <option value="">-- No building assigned --</option>
            <option v-for="b in buildings" :key="b.code" :value="b.code">
              {{ b.name }} ({{ b.code }})
            </option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 mb-1">Room</label>
          <select
            v-model="studentData.roomId"
            :disabled="!selectedBuildingCode"
            class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 disabled:bg-gray-50 disabled:cursor-not-allowed"
          >
            <option :value="0">-- No Room / Leave empty --</option>
            <option v-for="room in availableRooms" :key="room.id" :value="room.id">
              Room {{ room.roomNumber }} ({{ room.currentOccupancy }}/{{
                room.maximumCapacity
              }}
              beds)
            </option>
          </select>
          <p
            v-if="selectedBuildingCode && availableRooms.length === 0"
            class="text-xs text-rose-500 mt-1"
          >
            ⚠️ All rooms in this building are currently full or inactive.
          </p>
          <p v-else-if="selectedBuildingCode" class="text-xs text-emerald-600 mt-1">
            Found {{ availableRooms.length }} available room(s) in this building.
          </p>
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div class="flex items-center justify-end space-x-3 pt-4">
      <button
        type="button"
        @click="$emit('cancel')"
        class="px-5 py-2.5 text-sm font-medium text-gray-600 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer"
      >
        Cancel
      </button>
      <button
        type="button"
        @click="validateAndSubmit"
        class="px-5 py-2.5 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm cursor-pointer"
      >
        Save Profile
      </button>
    </div>
  </div>
</template>
