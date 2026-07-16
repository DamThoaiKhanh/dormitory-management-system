<script setup lang="ts">
interface BuildingOccupancy {
  name: string;
  rate: number;
}

defineProps<{
  buildingProgress: BuildingOccupancy[];
}>();
</script>

<template>
  <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 space-y-4">
    <h2 class="text-base font-bold text-gray-800">Room occupancy by Building</h2>
    <div class="space-y-4">
      <div v-for="b in buildingProgress" :key="b.name">
        <div class="flex justify-between text-sm font-medium mb-1">
          <span class="text-gray-600">{{ b.name }}</span>
          <span class="text-gray-900">{{ b.rate }}%</span>
        </div>
        <div class="w-full bg-gray-100 h-2 rounded-full">
          <div
            class="h-2 rounded-full transition-all duration-500"
            :class="b.rate > 90 ? 'bg-rose-500' : b.rate > 75 ? 'bg-blue-600' : 'bg-emerald-500'"
            :style="{ width: `${b.rate}%` }"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>
