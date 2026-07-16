<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useBuildingStore } from '../stores/buildingStore';
import { useRoomStore } from '../stores/roomStore';
import { useStudentStore } from '../stores/studentStore';
import apiClient, { getErrorMessage } from '../services/api';
import type { NewStudent, Student } from '../types/student';
import { useToast } from 'vue-toastification';
import type { Room } from '../types/room';

const router = useRouter();
const route = useRoute();

const buildingStore = useBuildingStore();
const roomStore = useRoomStore();
const studentStore = useStudentStore();
const toast = useToast();

const isEditMode = computed(() => !!route.params.id);
let oldRoomCode = '—';

const selectedRoomInfo = ref({
  building: '',
  rooms: [] as Room[],
});

const currentStudent = ref<Student | null>(null);
const newStudent = ref<NewStudent>({
  studentCode: '',
  fullName: '',
  gender: 'MALE',
  dateOfBirth: '',
  citizenId: '',
  phoneNumber: '',
  address: '',
  className: '',
  major: '',
  hometown: '',
  roomId: 0,
});

const studentForm = ref({
  name: '',
  gender: 'Male',
  dob: '',
  citizenId: '',
  phone: '',
  address: '',
  studentId: '',
  className: '',
  major: '',
  hometown: '',
  building: '',
  room: '',
});

const errors = ref({ fullName: '', studentCode: '', phone: '', citizenId: '' });

onMounted(async () => {
  try {
    await Promise.all([buildingStore.fetchBuildings(), roomStore.fetchRooms()]);
  } catch (error) {
    toast.error(getErrorMessage(error));
  }

  if (isEditMode.value) {
    try {
      const id = Number(route.params.id);
      const result = await studentStore.fetchStudent(id);
      if (result.success) {
        currentStudent.value = result.data;

        const s = result.data;
        newStudent.value.studentCode = result.data.studentCode;
        newStudent.value.fullName = result.data.fullName;
        newStudent.value.gender = result.data.gender;
        newStudent.value.dateOfBirth = result.data.dateOfBirth;
        newStudent.value.citizenId = result.data.citizenId;
        newStudent.value.phoneNumber = result.data.phoneNumber;
        newStudent.value.address = result.data.address;
        newStudent.value.className = result.data.className;
        newStudent.value.major = result.data.major;
        newStudent.value.hometown = result.data.hometown;
        newStudent.value.roomId = result.data.roomId;

        let bName = '';
        let rNum = '';
        if (s.roomNumber && s.roomNumber !== '—') {
          oldRoomCode = s.roomNumber;
          bName = 'Building ' + s.buildingCode;
          rNum = s.roomNumber;
        }

        studentForm.value = {
          name: s.fullName,
          gender: s.gender || 'Male',
          dob: s.dateOfBirth || '',
          citizenId: s.citizenId || '',
          phone: s.phoneNumber || '',
          address: s.address || '',
          studentId: s.studentCode,
          className: s.className,
          major: s.major || '',
          hometown: s.hometown || '',
          building: bName,
          room: rNum,
        };
      } else {
        toast.error(`Error get detail student ${id}`);
      }
    } catch (err) {
      toast.error(getErrorMessage(err));
    }
  }
});

const validateForm = () => {
  let isValid = true;
  errors.value = { fullName: '', studentCode: '', phone: '', citizenId: '' };

  if (!studentForm.value.name.trim()) {
    errors.value.fullName = 'Full name is required.';
    isValid = false;
  }
  const studentIdRegex = /^[A-Z0-9]{4,12}$/;
  if (!studentForm.value.studentId.trim()) {
    errors.value.studentCode = 'Student ID is required.';
    isValid = false;
  } else if (!studentIdRegex.test(studentForm.value.studentId.toUpperCase())) {
    errors.value.studentCode = 'Student ID must be uppercase alphanumeric (4-12 chars).';
    isValid = false;
  }
  return isValid;
};

// Change buisling -> reset selected room
watch(
  () => studentForm.value.building,
  (newB, oldB) => {
    if (oldB !== undefined) studentForm.value.room = '';
  }
);

watch(
  () => selectedRoomInfo.value.building,
  (newValue) => {
    selectedRoomInfo.value.rooms = roomStore.rooms.filter((r) => r.buildingCode === newValue);
  }
);

watch(
  () => newStudent.value.roomId,
  (newValue) => {
    console.log(newValue);
  }
);

const getAvailableRooms = computed(() => {
  if (!selectedRoomInfo.value.building) return [];
  return roomStore.rooms.filter(
    (room) =>
      room.buildingCode === selectedRoomInfo.value.building &&
      room.currentOccupancy < room.maximumCapacity &&
      room.status === 'ACTIVE'
  );
});

const handleSave = async () => {
  if (!validateForm()) return;
  if (isEditMode.value) {
    try {
      const result = await studentStore.updateStudent(Number(route.params.id), newStudent.value);
      if (result.success) {
        toast.success('ok');
      } else {
        toast.error(result.error.message);
      }
    } catch (error) {
      toast.error(getErrorMessage(error));
    }
  } else {
    // await studentStore.addStudent(currentStudent.value?);
    // if (selectedRoomCode !== '—') {
    //   const roomNumber = selectedRoomCode.split('-')[1];
    //   const targetRoom = roomStore.rooms.find(
    //     (r) => r.number === roomNumber && r.building === studentForm.value.building
    //   );
    //   if (targetRoom) {
    //     await apiClient.patch(`/rooms/${targetRoom.id}`, {
    //       occupied: targetRoom.occupied + 1,
    //     });
    //   }
    // }
  }

  router.push('/students');
};

const handleCancel = () => router.push('/students');
</script>

<template>
  <div class="p-6 max-w-4xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex items-center space-x-4">
      <button
        @click="handleCancel"
        class="p-2 bg-white hover:bg-gray-100 text-gray-600 rounded-lg border border-gray-200 text-sm transition-colors"
      >
        ← Back
      </button>
      <div>
        <h1 class="text-2xl font-bold text-gray-900">
          {{ isEditMode ? 'Edit student profile' : 'Add student' }}
        </h1>
        <p class="text-sm text-gray-500 mt-0.5">Create a new student profile and system account</p>
      </div>
    </div>

    <div class="space-y-6">
      <!-- Personal information -->
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
              v-model="newStudent.fullName"
              type="text"
              placeholder="Nguyễn Văn A"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
            <p v-if="errors.fullName" class="text-xs text-rose-500 mt-1">
              {{ errors.fullName }}
            </p>
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Gender</label>
            <select
              v-model="newStudent.gender"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
            >
              <option value="MALE">MALE</option>
              <option value="FEMALE">FEMALE</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Date of Birth</label>
            <input
              v-model="newStudent.dateOfBirth"
              type="date"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Citizen ID</label>
            <input
              v-model="newStudent.citizenId"
              type="text"
              placeholder="012345678901"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Phone Number</label>
            <input
              v-model="newStudent.phoneNumber"
              type="text"
              placeholder="0912345678"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
            <p v-if="errors.phone" class="text-xs text-rose-500 mt-1">
              {{ errors.phone }}
            </p>
          </div>
          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Address</label>
            <textarea
              v-model="newStudent.address"
              rows="2"
              placeholder="Permanent residence address..."
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            ></textarea>
          </div>
          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Hometown</label>
            <input
              v-model="newStudent.hometown"
              type="text"
              placeholder="Hanoi"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
        </div>
      </div>

      <!-- Academic information -->
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
              v-model="newStudent.studentCode"
              type="text"
              placeholder=""
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
            <p v-if="errors.studentCode" class="text-xs text-rose-500 mt-1">
              {{ errors.studentCode }}
            </p>
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Class</label>
            <input
              v-model="newStudent.className"
              type="text"
              placeholder="e.g. CNTT-01"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Major</label>
            <input
              v-model="newStudent.major"
              type="text"
              placeholder="Computer Science"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500"
            />
          </div>
        </div>
      </div>

      <!-- Room assignment -->
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
              v-model="selectedRoomInfo.building"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm bg-white focus:outline-none focus:border-blue-500"
            >
              <option
                v-for="buildingCode in buildingStore.buildings.map((b) => b.code)"
                :key="buildingCode"
                :value="buildingCode"
              >
                {{ buildingCode }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Room</label>
            <select
              v-model="newStudent.roomId"
              class="w-full px-3 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:border-blue-500 disabled:bg-gray-50 disabled:cursor-not-allowed"
            >
              <option v-for="room in selectedRoomInfo.rooms" :key="room.id" :value="room.id">
                Room {{ room.roomNumber }} ({{ room.currentOccupancy }}/{{ room.maximumCapacity }}
                beds)
              </option>
            </select>
            <p
              v-if="selectedRoomInfo.building && getAvailableRooms.length === 0"
              class="text-xs text-rose-500 mt-1"
            >
              ⚠️ All rooms in this building are currently full or inactive.
            </p>
            <p v-else-if="selectedRoomInfo.building" class="text-xs text-emerald-600 mt-1">
              Found {{ getAvailableRooms.length }} available room(s) in this building.
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- Save/Cancel buttons -->
    <div class="flex items-center justify-end space-x-3 pt-4">
      <button
        @click="handleCancel"
        class="px-5 py-2.5 text-sm font-medium text-gray-600 bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors cursor-pointer"
      >
        Cancel
      </button>
      <button
        @click="handleSave"
        class="px-5 py-2.5 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors shadow-sm cursor-pointer"
      >
        Save
      </button>
    </div>
  </div>
</template>
