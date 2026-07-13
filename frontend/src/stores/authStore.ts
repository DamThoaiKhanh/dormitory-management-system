import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { useToast } from 'vue-toastification';
import apiClient, { getApiError } from '../services/api';
import type { Staff } from '../types/staff';
import { delayMs } from '../utils/delay';

const USE_BYPASS_LOGIN = true;

export const useAuthStore = defineStore('auth', () => {
  const user = ref<Staff | null>(JSON.parse(localStorage.getItem('user') || 'null'));
  const token = ref<string | null>(localStorage.getItem('token'));
  const toast = useToast();

  const isAuthenticated = computed(() => Boolean(token.value));
  const userRole = computed(() => user.value?.role ?? null);

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      await delayMs(1000);

      if (USE_BYPASS_LOGIN) {
        const mockStaff: Staff = {
          id: 1,
          employeeCode: 'ST001',
          fullName: 'Dam Thoai Khanh',
          gender: 'MALE',
          dateOfBirth: '1998-10-18',
          citizenId: '',
          phoneNumber: '0981111111',
          address: 'Vo Nhai, Thai Nguyen',
          assignedBuildingCode: 'A',
          assignedBuildingName: 'Building A',
          username: 'TestAccount',
          role: 'Manager',
        };

        user.value = mockStaff;
        token.value = 'mock-jwt-token-bypass-manager';

        localStorage.setItem('user', JSON.stringify(mockStaff));
        localStorage.setItem('token', token.value);

        console.log('Login bypass mode activated');
        toast.success(`Welcome back ${mockStaff.fullName}!`);
        return true;
      }

      const result = await apiClient.get<Staff[]>('/staff', {
        params: {
          username: username.trim(),
          password: password.trim(),
        },
      });

      if (!result.success) {
        toast.error(result.error.message);
        return false;
      }

      const foundStaff = result.data[0];

      if (!foundStaff) {
        toast.error('Incorrect username or password!');
        return false;
      }

      user.value = foundStaff;
      token.value = `mock-jwt-token-xyz-${foundStaff.id}`;

      localStorage.setItem('user', JSON.stringify(foundStaff));
      localStorage.setItem('token', token.value);

      toast.success(`Welcome back ${foundStaff.fullName}!`);
      return true;
    } catch (exception) {
      const apiError = getApiError(exception);
      toast.error(apiError.message);
      return false;
    }
  };

  const logout = (): void => {
    user.value = null;
    token.value = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    toast.info('Đã đăng xuất khỏi hệ thống.');
  };

  return {
    user,
    token,
    isAuthenticated,
    userRole,
    login,
    logout,
  };
});
