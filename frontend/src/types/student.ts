import type { Gender } from '../utils/gender';

export interface Student {
  id: number;
  studentCode: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  citizenId: string;
  phoneNumber: string;
  address: string;
  className: string;
  major: string;
  hometown: string;
  roomId?: number;
  roomNumber?: string;
  buildingCode?: string;
}

export interface NewStudent {
  studentCode: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  citizenId: string;
  phoneNumber: string;
  address: string;
  className: string;
  major: string;
  hometown: string;
  roomId?: number;
}
