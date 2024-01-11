export interface IDashboard {
  id: string;
  totalSaleTillDate?: number | null;
  totalProfitTillDate?: number | null;
}

export type NewDashboard = Omit<IDashboard, 'id'> & { id: null };
