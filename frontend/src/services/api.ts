import axios, { type AxiosRequestConfig } from 'axios';
import type { ApiErrorResponse, ApiResult } from '../types/apiError';

const LOCAL_JSON_SERVER = 'http://localhost:3000';
const LOCAL_JAVA_SPRING = 'http://localhost:8080/api/v1';

// const BASE_SERVER_URL = LOCAL_JSON_SERVER;
const BASE_SERVER_URL = LOCAL_JAVA_SPRING;

const axiosClient = axios.create({
  baseURL: BASE_SERVER_URL,
  timeout: 10_000,
  headers: {
    Accept: 'application/json',
  },
});

export function getApiError(error: unknown): ApiErrorResponse {
  if (axios.isAxiosError<ApiErrorResponse>(error)) {
    if (error.response?.data) {
      return error.response.data;
    }

    return {
      timestamp: new Date().toISOString(),
      status: 0,
      error: 'Network Error',
      message:
        error.code === 'ECONNABORTED'
          ? 'The request timed out.'
          : 'Could not connect to the server.',
      path: error.config?.url ?? '',
      fieldErrors: {},
    };
  }

  return {
    timestamp: new Date().toISOString(),
    status: 0,
    error: 'Unexpected Error',
    message: error instanceof Error ? error.message : 'An unexpected error occurred.',
    path: '',
    fieldErrors: {},
  };
}

export function getErrorMessage(error: unknown): string {
  return getApiError(error).message;
}

async function request<T>(config: AxiosRequestConfig): Promise<ApiResult<T>> {
  try {
    const response = await axiosClient.request<T>(config);

    return {
      success: true,
      data: response.data,
    };
  } catch (error: unknown) {
    return {
      success: false,
      error: getApiError(error),
    };
  }
}

const apiClient = {
  get<T>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>> {
    return request<T>({
      ...config,
      method: 'GET',
      url,
    });
  },

  post<TResponse, TBody = unknown>(
    url: string,
    data: TBody,
    config?: AxiosRequestConfig
  ): Promise<ApiResult<TResponse>> {
    return request<TResponse>({
      ...config,
      method: 'POST',
      url,
      data,
    });
  },

  put<TResponse, TBody = unknown>(
    url: string,
    data: TBody,
    config?: AxiosRequestConfig
  ): Promise<ApiResult<TResponse>> {
    return request<TResponse>({
      ...config,
      method: 'PUT',
      url,
      data,
    });
  },

  patch<TResponse, TBody = unknown>(
    url: string,
    data: TBody,
    config?: AxiosRequestConfig
  ): Promise<ApiResult<TResponse>> {
    return request<TResponse>({
      ...config,
      method: 'PATCH',
      url,
      data,
    });
  },

  delete<T = void>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>> {
    return request<T>({
      ...config,
      method: 'DELETE',
      url,
    });
  },
};

export default apiClient;
