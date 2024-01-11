import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: '947cb33a-973f-4123-be45-c649c026ef38',
  firstName: 'Nolan',
  lastName: 'Lockman',
  email: 'Gladys_Nader82@gmail.com',
};

export const sampleWithPartialData: ICustomer = {
  id: '24346cac-f3e1-4c7f-a3df-613ad541bc11',
  firstName: 'Aryanna',
  lastName: 'Klocko',
  email: 'Pablo.Wisozk25@gmail.com',
  phone: '858-707-7242 x070',
};

export const sampleWithFullData: ICustomer = {
  id: '09b2b39e-cb4a-4c96-9203-e2c6adae02ae',
  firstName: 'Lawson',
  lastName: 'Maggio',
  email: 'Keeley_Kling86@gmail.com',
  phone: '(212) 477-0815 x154',
  address: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Kailee',
  lastName: 'Marks',
  email: 'Clint74@hotmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
