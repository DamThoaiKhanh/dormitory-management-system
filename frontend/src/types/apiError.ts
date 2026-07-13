export interface ApiErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  fieldErrors: Record<string, string>;
}

export type ApiResult<T> =
  | {
      success: true;
      data: T;
    }
  | {
      success: false;
      error: ApiErrorResponse;
    };
