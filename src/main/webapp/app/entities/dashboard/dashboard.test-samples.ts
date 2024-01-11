import { IDashboard, NewDashboard } from './dashboard.model';

export const sampleWithRequiredData: IDashboard = {
  id: '26735d79-751f-4be3-91cb-8f0702915c99',
};

export const sampleWithPartialData: IDashboard = {
  id: '0abb795b-931f-4dcf-a02c-4f7040bca1df',
  totalSaleTillDate: 8982.47,
};

export const sampleWithFullData: IDashboard = {
  id: 'df715ff1-51ba-45cf-b472-1fc6ddaad136',
  totalSaleTillDate: 29367.19,
  totalProfitTillDate: 7647.48,
};

export const sampleWithNewData: NewDashboard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
