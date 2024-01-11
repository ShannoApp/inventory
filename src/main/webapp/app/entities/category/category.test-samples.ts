import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: '775c3f2f-3cd1-4de6-ad06-902eedde862f',
  name: 'unaccountably yahoo',
};

export const sampleWithPartialData: ICategory = {
  id: '68302839-e0f6-4846-8f83-b592b37899a0',
  name: 'defenseless pish mosque',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ICategory = {
  id: '1a8ee4f9-85d8-4e6a-9746-c3880ae88fad',
  name: 'nor',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCategory = {
  name: 'before mmm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
