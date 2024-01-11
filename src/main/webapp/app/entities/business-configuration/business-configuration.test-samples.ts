import { IBusinessConfiguration, NewBusinessConfiguration } from './business-configuration.model';

export const sampleWithRequiredData: IBusinessConfiguration = {
  id: '90c56178-b93f-4d02-a122-56601ff15620',
  businessId: 'cling victoriously hardcover',
};

export const sampleWithPartialData: IBusinessConfiguration = {
  id: 'cd9c6c90-0f4d-4f73-9384-709751c2a5bb',
  businessId: 'because hyperventilate great',
  discount: 26410.08,
  tax: 17872.2,
};

export const sampleWithFullData: IBusinessConfiguration = {
  id: 'fd95f054-0fef-46e8-82b2-a309ed4be4f2',
  businessId: 'rebuke quick-witted rotation',
  discount: 29189.25,
  shippingCharges: 2783.38,
  tax: 1553.59,
};

export const sampleWithNewData: NewBusinessConfiguration = {
  businessId: 'ignorant or pet',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
