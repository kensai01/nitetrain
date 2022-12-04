import { IPrice, NewPrice } from './price.model';

export const sampleWithRequiredData: IPrice = {
  id: 41746,
  name: 'Chips',
  location: 'Consultant Metrics pink',
  description: 'Mobility cross-media',
};

export const sampleWithPartialData: IPrice = {
  id: 54370,
  name: 'Avon Hungary',
  location: 'calculating seize connecting',
  description: 'Leu deposit',
};

export const sampleWithFullData: IPrice = {
  id: 47210,
  name: 'Interactions',
  location: 'Ethiopian Diverse',
  description: 'cyan Berkshire Guyana',
  cost: 5414,
};

export const sampleWithNewData: NewPrice = {
  name: 'e-enable 24/7 Mali',
  location: 'Cambridgeshire',
  description: 'deposit panel',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
