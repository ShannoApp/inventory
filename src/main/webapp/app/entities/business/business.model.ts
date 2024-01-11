export interface IBusiness {
  id: string;
  name?: string | null;
  website?: string | null;
  email?: string | null;
  phone?: string | null;
  deflaut?: boolean | null;
  users?: string[] | null;
}

export type NewBusiness = Omit<IBusiness, 'id'> & { id: null };
