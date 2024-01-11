import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 'a85fdb9c-65ef-4c55-8cb2-1cecea3b7da4',
  name: 'phooey past interwork',
  sellingPrice: 29837.14,
  openingQuantity: 27911,
};

export const sampleWithPartialData: IProduct = {
  id: '48a47309-b825-4872-94b0-0231c219bf63',
  name: 'poorly hmph officially',
  sellingPrice: 5170.62,
  openingQuantity: 13351,
};

export const sampleWithFullData: IProduct = {
  id: '4847a72a-d2f2-4322-a7e7-0134eb9049cd',
  name: 'jam-packed',
  description: '../fake-data/blob/hipster.txt',
  sellingPrice: 29601.78,
  openingQuantity: 26526,
};

export const sampleWithNewData: NewProduct = {
  name: 'net',
  sellingPrice: 1335.03,
  openingQuantity: 16614,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
