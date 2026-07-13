import { defineStore } from 'pinia';
import apiClient, { getApiError } from '../services/api';
import type { ApiResult } from '../types/apiError';
import type { Building } from '../types/building';

export const useBuildingStore = defineStore('building', {
  state: () => ({
    buildings: [] as Building[],
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchBuildings(): Promise<ApiResult<Building[]>> {
      this.loading = true;
      this.error = null;

      try {
        const result = await apiClient.get<Building[]>('/buildings');

        if (result.success) {
          this.buildings = result.data;
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

    async addBuilding(building: Building): Promise<ApiResult<Building>> {
      this.error = null;

      try {
        const result = await apiClient.post<Building, Building>('/buildings', building);

        if (result.success) {
          this.buildings.push(result.data);
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

    async updateBuilding(
      code: string,
      updatedData: Partial<Building>
    ): Promise<ApiResult<Building>> {
      this.error = null;

      try {
        const result = await apiClient.put<Building, Partial<Building>>(
          `/buildings/${code}`,
          updatedData
        );

        if (result.success) {
          const index = this.buildings.findIndex((building) => building.code === code);

          if (index !== -1) {
            this.buildings[index] = {
              ...this.buildings[index],
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

    async deleteBuilding(code: string): Promise<ApiResult<void>> {
      this.error = null;

      try {
        const result = await apiClient.delete<void>(`/buildings/${code}`);

        if (result.success) {
          this.buildings = this.buildings.filter((building) => building.code !== code);
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
