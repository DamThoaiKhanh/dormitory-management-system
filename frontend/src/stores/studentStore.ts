import { defineStore } from 'pinia';
import apiClient, { getApiError } from '../services/api';
import type { ApiResult } from '../types/apiError';
import type { Student } from '../types/student';
import { delaySecond } from '../utils/delay';

export const useStudentStore = defineStore('student', {
  state: () => ({
    students: [] as Student[],
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchStudents(): Promise<ApiResult<Student[]>> {
      this.loading = true;
      this.error = null;

      try {
        const result = await apiClient.get<Student[]>('/students');

        if (result.success) {
          this.students = result.data;
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

    async fetchStudent(id: number): Promise<ApiResult<Student>> {
      this.loading = true;
      this.error = null;

      try {
        const result = await apiClient.get<Student>(`/students/${id}`);

        if (result.success) {
          return result;
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

    async addStudent(student: Student): Promise<ApiResult<Student>> {
      this.error = null;

      try {
        const result = await apiClient.post<Student, Student>('/students', student);

        if (result.success) {
          this.students.push(result.data);
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

    async deleteStudent(id: number): Promise<ApiResult<void>> {
      this.error = null;

      try {
        const result = await apiClient.delete<void>(`/students/${id}`);

        if (result.success) {
          this.students = this.students.filter((student) => student.id !== id);
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
