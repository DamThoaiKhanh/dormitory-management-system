import type { Staff } from './staff';

export interface AuthState {
  user: Staff | null;
  token: string | null;
}
