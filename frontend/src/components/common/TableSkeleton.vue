<script setup lang="ts">
type ColumnAlign = 'left' | 'center' | 'right';
type SkeletonType = 'text' | 'badge' | 'actions';

export interface SkeletonColumn {
  key: string;
  label?: string;
  width?: string;
  skeletonWidth?: string;
  align?: ColumnAlign;
  type?: SkeletonType;
}

withDefaults(
  defineProps<{
    columns: SkeletonColumn[];
    rows?: number;
    showHeader?: boolean;
  }>(),
  {
    rows: 5,
    showHeader: true,
  }
);

const getTextAlignment = (align?: ColumnAlign) => {
  if (align === 'center') return 'text-center';
  if (align === 'right') return 'text-right';

  return 'text-left';
};

const getSkeletonAlignment = (align?: ColumnAlign) => {
  if (align === 'center') return 'mx-auto';
  if (align === 'right') return 'ml-auto';

  return '';
};
</script>

<template>
  <div
    class="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm"
    role="status"
    aria-busy="true"
    aria-label="Loading table data"
  >
    <table class="w-full border-collapse">
      <colgroup>
        <col v-for="column in columns" :key="column.key" :style="{ width: column.width }" />
      </colgroup>

      <thead v-if="showHeader" class="bg-gray-50">
        <tr>
          <th
            v-for="column in columns"
            :key="column.key"
            class="px-8 py-5 text-xs font-bold uppercase tracking-wide text-gray-500"
            :class="getTextAlignment(column.align)"
          >
            {{ column.label }}
          </th>
        </tr>
      </thead>

      <tbody class="divide-y divide-gray-100">
        <tr v-for="row in rows" :key="row" class="animate-pulse">
          <td v-for="column in columns" :key="`${row}-${column.key}`" class="px-8 py-4">
            <!-- Badge skeleton, for example room number -->
            <div
              v-if="column.type === 'badge'"
              class="h-8 w-12 rounded-md bg-gray-200"
              :class="getSkeletonAlignment(column.align)"
            />

            <!-- Action buttons skeleton -->
            <div
              v-else-if="column.type === 'actions'"
              class="flex items-center gap-3"
              :class="{
                'justify-center': column.align === 'center',
                'justify-end': column.align === 'right',
              }"
            >
              <div class="h-4 w-10 rounded bg-gray-200" />
              <div class="h-4 w-9 rounded bg-gray-200" />
            </div>

            <!-- Normal text skeleton -->
            <div
              v-else
              class="h-4 rounded bg-gray-200"
              :class="getSkeletonAlignment(column.align)"
              :style="{ width: column.skeletonWidth ?? '70%' }"
            />
          </td>
        </tr>
      </tbody>
    </table>

    <span class="sr-only">Loading...</span>
  </div>
</template>
