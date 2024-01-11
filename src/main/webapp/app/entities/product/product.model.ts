import { IBusiness } from 'app/entities/business/business.model';
import { ICategory } from 'app/entities/category/category.model';
import dayjs from 'dayjs';

export interface IProduct {
  id: string;
  name?: string | null;
  description?: string | null;
  unit?: string | null;
  sellingPrice?: number | null;
  openingQuantity?: number | null;
  business?: Pick<IBusiness, 'id'> | null;
  category?: ICategory | null;
  purchasePrice?: number | null;
  asOfDate?: dayjs.Dayjs | null;
  minStockToMaintain?: number | null;
  location?: string | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
