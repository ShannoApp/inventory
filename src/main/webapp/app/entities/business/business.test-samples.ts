import { IBusiness, NewBusiness } from './business.model';

export const sampleWithRequiredData: IBusiness = {
  id: '1787ebbb-61bd-4a48-9e10-9166b133468c',
  name: 'yahoo whoever',
};

export const sampleWithPartialData: IBusiness = {
  id: '5703a739-8b2c-4ef5-9f49-e02fb6b26003',
  name: 'honorable mockingly',
  website: 'snarl',
  email: 'Deshaun_Kuhlman63@hotmail.com',
  phone: '644-498-0410 x72649',
};

export const sampleWithFullData: IBusiness = {
  id: '75bbf554-7f01-4ac9-b2a3-5a650fa392a9',
  name: 'aw flicker pro',
  website: 'although heavily cold',
  email: 'Damaris_Upton@hotmail.com',
  phone: '297.202.1162 x799',
};

export const sampleWithNewData: NewBusiness = {
  name: 'forthright geez enraged',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
