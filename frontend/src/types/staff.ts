import { type Gender } from '../utils/gender';

export type UserRole = 'Manager' | 'Accountant';

export interface Staff {
  id: number;
  employeeCode: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  citizenId: string;
  phoneNumber: string;
  address: string;
  assignedBuildingCode: string;
  assignedBuildingName: string;
  username: string;
  role: UserRole;
  password?: string;
}

export interface NewStaff {
  employeeCode: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  citizenId: string;
  phoneNumber: string;
  address: string;
  assignedBuildingCode: string;
}
