export interface Building {
  code: string;
  name: string;
  address?: string;
  numberOfFloors: number;
  description?: string;
  status: 'ACTIVE' | 'INACTIVE';
}
