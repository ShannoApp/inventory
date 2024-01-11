export interface IBusinessConfiguration {
  id: string;
  businessId?: string | null;
  discount?: number | null;
  shippingCharges?: number | null;
  tax?: number | null;

  // [key: string]: any;
}

export type NewBusinessConfiguration = Omit<IBusinessConfiguration, 'id'> & { id: null };
