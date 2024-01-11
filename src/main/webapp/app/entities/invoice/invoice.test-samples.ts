import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: '44d52770-ef83-43db-a74a-c80ccf9cd1e4',
  invoiceNumber: 'however dear indeed',
  issueDate: dayjs('2023-11-24'),
  dueDate: dayjs('2023-11-23'),
  totalAmount: 24184.98,
};

export const sampleWithPartialData: IInvoice = {
  id: '3dccf452-6f71-4bfc-a2a4-ebdddd8cf525',
  invoiceNumber: 'vaguely incidentally',
  issueDate: dayjs('2023-11-24'),
  dueDate: dayjs('2023-11-24'),
  totalAmount: 3494.88,
};

export const sampleWithFullData: IInvoice = {
  id: '95e35e17-5e05-47f7-bbfe-d1c08e7402fe',
  invoiceNumber: 'abaft dog questioningly',
  issueDate: dayjs('2023-11-23'),
  dueDate: dayjs('2023-11-24'),
  totalAmount: 886.76,
};

export const sampleWithNewData: NewInvoice = {
  invoiceNumber: 'hm bronco',
  issueDate: dayjs('2023-11-23'),
  dueDate: dayjs('2023-11-23'),
  totalAmount: 1351.01,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
