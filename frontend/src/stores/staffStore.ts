import { defineStore } from 'pinia';
import apiClient, { getApiError } from '../services/api';
import type { ApiResult } from '../types/apiError';
import type { Staff } from '../types/staff';

export const useStaffStore = defineStore('staff', {
  state: () => ({
    staffList: [] as Staff[],
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchStaff(): Promise<ApiResult<Staff[]>> {
      this.loading = true;
      this.error = null;

      try {
        const result = await apiClient.get<Staff[]>('/staff');

        if (result.success) {
          this.staffList = result.data;
        } else {
          this.error = result.error.message;
        }

        return result;
      } catch (exception) {
        const apiError = getApiError(exception);
        this.error = apiError.message;
        return { success: false, error: apiError };
      } finally {
        this.loading = false;
      }
    },

    async addStaff(staff: Staff): Promise<ApiResult<Staff>> {
      this.error = null;

      try {
        const result = await apiClient.post<Staff, Staff>('/staff', staff);

        if (result.success) {
          this.staffList.push(result.data);
        } else {
          this.error = result.error.message;
        }

        return result;
      } catch (exception) {
        const apiError = getApiError(exception);
        this.error = apiError.message;
        return { success: false, error: apiError };
      }
    },

    async updateStaff(id: number, updatedData: Partial<Staff>): Promise<ApiResult<Staff>> {
      this.error = null;

      try {
        const result = await apiClient.put<Staff, Partial<Staff>>(`/staff/${id}`, updatedData);

        if (result.success) {
          const index = this.staffList.findIndex((staff) => staff.id === id);

          if (index !== -1) {
            this.staffList[index] = {
              ...this.staffList[index],
              ...result.data,
            };
          }
        } else {
          this.error = result.error.message;
        }

        return result;
      } catch (exception) {
        const apiError = getApiError(exception);
        this.error = apiError.message;
        return { success: false, error: apiError };
      }
    },

    async deleteStaff(id: number): Promise<ApiResult<void>> {
      this.error = null;

      try {
        const result = await apiClient.delete<void>(`/staff/${id}`);

        if (result.success) {
          this.staffList = this.staffList.filter((staff) => staff.id !== id);
        } else {
          this.error = result.error.message;
        }

        return result;
      } catch (exception) {
        const apiError = getApiError(exception);
        this.error = apiError.message;
        return { success: false, error: apiError };
      }
    },
  },
});
