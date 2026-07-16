<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useStudentStore } from '../stores/studentStore';
import { useBuildingStore } from '../stores/buildingStore';
import { useRoomStore } from '../stores/roomStore';
import { useToast } from 'vue-toastification';
import { getErrorMessage } from '../services/api';
import type { NewStudent } from '../types/student';

import StudentForm from '../components/student/StudentForm.vue';

const router = useRouter();
const route = useRoute();
const toast = useToast();

const studentStore = useStudentStore();
const buildingStore = useBuildingStore();
const roomStore = useRoomStore();

const isEditMode = computed(() => !!route.params.id);

const studentData = ref<NewStudent>({
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

onMounted(async () => {
  try {
    await Promise.all([buildingStore.fetchBuildings(), roomStore.fetchRooms()]);

    if (isEditMode.value) {
      const studentId = Number(route.params.id);
      const res = await studentStore.fetchStudent(studentId);
      if (res.success && res.data) {
        studentData.value = {
          studentCode: res.data.studentCode,
          fullName: res.data.fullName,
          gender: res.data.gender || 'MALE',
          dateOfBirth: res.data.dateOfBirth || '',
          citizenId: res.data.citizenId || '',
          phoneNumber: res.data.phoneNumber || '',
          address: res.data.address || '',
          className: res.data.className || '',
          major: res.data.major || '',
          hometown: res.data.hometown || '',
          roomId: res.data.roomId || 0,
        };
      } else {
        toast.error('Could not load student profile.');
        router.push('/students');
      }
    }
  } catch (error) {
    toast.error(getErrorMessage(error));
  }
});

const handleSave = async () => {
  try {
    let result;
    if (isEditMode.value) {
      result = await studentStore.updateStudent(Number(route.params.id), studentData.value);
    } else {
      result = await studentStore.addStudent(studentData.value);
    }

    if (result.success) {
      toast.success(
        isEditMode.value ? 'Student updated successfully!' : 'Student created successfully!'
      );
      router.push('/students');
    } else {
      toast.error(result.error?.message || 'Action failed.');
    }
  } catch (error) {
    toast.error(getErrorMessage(error));
  }
};

const handleCancel = () => {
  router.push('/students');
};
</script>

<template>
  <div class="p-6 max-w-4xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex items-center space-x-4">
      <button
        @click="handleCancel"
        class="p-2 bg-white hover:bg-gray-100 text-gray-600 rounded-lg border border-gray-200 text-sm transition-colors cursor-pointer"
      >
        ← Back
      </button>
      <div>
        <h1 class="text-2xl font-bold text-gray-900">
          {{ isEditMode ? 'Edit Student Profile' : 'Add New Student' }}
        </h1>
        <p class="text-sm text-gray-500 mt-0.5">
          {{
            isEditMode
              ? 'Modify existing system registry details'
              : 'Register a new student and assign a room'
          }}
        </p>
      </div>
    </div>

    <!-- Form -->
    <StudentForm
      v-model="studentData"
      :buildings="buildingStore.buildings"
      :rooms="roomStore.rooms"
      :isEditMode="isEditMode"
      @save="handleSave"
      @cancel="handleCancel"
    />
  </div>
</template>
