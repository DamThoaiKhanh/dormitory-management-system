export interface Student {
  id: number;
  studentCode: string;
  fullName: string;
  gender: 'MALE' | 'FEMALE';
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
