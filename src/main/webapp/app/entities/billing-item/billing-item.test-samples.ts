import { IBillingItem } from './billing-item.model';

export const sampleWithRequiredData: IBillingItem = {
  description: 'whereas',
  quantity: 10368,
  unitPrice: 25837.8,
  totalAmount: 9678.48,
};

export const sampleWithPartialData: IBillingItem = {
  description: 'but blah',
  quantity: 13279,
  unitPrice: 7294.9,
  totalAmount: 23179.24,
};

export const sampleWithFullData: IBillingItem = {
  description: 'phew yowza since',
  quantity: 3033,
  unitPrice: 9797.53,
  totalAmount: 12225.92,
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
