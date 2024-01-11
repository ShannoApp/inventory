import { IBusiness } from 'app/entities/business/business.model';

export interface ICategory {
  id: string;
  name?: string | null;
  description?: string | null;
  business?: Pick<IBusiness, 'id'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
