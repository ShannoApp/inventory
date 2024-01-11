import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IBusiness } from 'app/entities/business/business.model';
import { IBillingItem } from '../billing-item/billing-item.model';

export interface IInvoice {
  id: string;
  invoiceNumber?: string | null;
  issueDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  subTotal?: number | null;
  tax?: number | null;
  discount?: number | null;
  shippingCharges?: number | null;
  totalAmount?: number | null;
  billingItems?: IBillingItem[];
  customer?: ICustomer | null;
  business?: IBusiness | null;
  paid?: boolean;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
