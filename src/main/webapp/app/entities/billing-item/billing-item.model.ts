import { IProduct } from 'app/entities/product/product.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IBillingItem {
  description?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  totalAmount?: number | null;
  product?: IProduct | null;
  // invoice?: Pick<IInvoice, 'id'> | null;
}
