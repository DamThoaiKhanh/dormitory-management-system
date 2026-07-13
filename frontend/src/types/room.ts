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
  status: 'ACTIVE' | 'INACTIVE';
}
