export type RoomStatus = 'ACTIVE' | 'INACTIVE';

export interface Room {
  id: number;
  roomNumber: string;
  buildingCode: string;
  buildingName: string;
  monthlyRent: number;
  maximumCapacity: number;
  currentOccupancy: number;
  availableBeds: number;
  occupancyStatus: 'EMPTY' | 'AVAILABLE' | 'FULL';
  status: RoomStatus;
}

export interface NewRoom {
  roomNumber: string;
  buildingCode: string;
  monthlyRent: number;
  maximumCapacity: number;
  status: RoomStatus;
}
