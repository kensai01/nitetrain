import { IWorkout, NewWorkout } from './workout.model';

export const sampleWithRequiredData: IWorkout = {
  id: 88410,
  title: 'bluetooth Computers AI',
  description: 'Account quantify',
};

export const sampleWithPartialData: IWorkout = {
  id: 12721,
  title: 'Brook transform Soap',
  description: 'Berkshire India Cotton',
  time: 99,
  scaling: 'forecast methodologies',
};

export const sampleWithFullData: IWorkout = {
  id: 10244,
  title: 'Table uniform Wyoming',
  description: 'Pennsylvania',
  time: 55,
  videoId: 'Zimbabwe Concrete solid',
  scaling: 'holistic',
};

export const sampleWithNewData: NewWorkout = {
  title: 'Automotive Buckinghamshire',
  description: 'Shoes',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
