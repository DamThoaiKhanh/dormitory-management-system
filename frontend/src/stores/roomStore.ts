import { defineStore } from 'pinia';
import apiClient, { getApiError } from '../services/api';
import type { ApiResult } from '../types/apiError';
import type { Room } from '../types/room';
import { delaySecond } from '../utils/delay';

type CreateRoomRequest = Omit<Room, 'id' | 'occupied'> & {
  occupied: number;
};

export const useRoomStore = defineStore('room', {
  state: () => ({
    rooms: [] as Room[],
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchRooms(): Promise<ApiResult<Room[]>> {
      this.loading = true;
      this.error = null;

      try {
        const result = await apiClient.get<Room[]>('/rooms');

        if (result.success) {
          this.rooms = result.data;
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

    async addRoom(room: Omit<Room, 'id' | 'occupied'>): Promise<ApiResult<Room>> {
      this.error = null;

      try {
        const payload: CreateRoomRequest = {
          ...room,
          occupied: 0,
        };

        const result = await apiClient.post<Room, CreateRoomRequest>('/rooms', payload);

        if (result.success) {
          this.rooms.push(result.data);
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

    async updateRoom(id: number, updatedData: Partial<Room>): Promise<ApiResult<Room>> {
      this.error = null;

      try {
        const result = await apiClient.patch<Room, Partial<Room>>(`/rooms/${id}`, updatedData);

        if (result.success) {
          const index = this.rooms.findIndex((room) => room.id === id);

          if (index !== -1) {
            this.rooms[index] = {
              ...this.rooms[index],
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

    async deleteRoom(id: number): Promise<ApiResult<void>> {
      this.error = null;

      try {
        const result = await apiClient.delete<void>(`/rooms/${id}`);

        if (result.success) {
          this.rooms = this.rooms.filter((room) => room.id !== id);
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
