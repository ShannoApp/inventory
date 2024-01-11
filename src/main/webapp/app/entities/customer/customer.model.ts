import { IBusiness } from 'app/entities/business/business.model';

export interface ICustomer {
  id: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phone?: string | null;
  address?: string | null;
  business?: Pick<IBusiness, 'id'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
