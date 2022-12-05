import { IIntermediateWorkout, NewIntermediateWorkout } from './intermediate-workout.model';

export const sampleWithRequiredData: IIntermediateWorkout = {
  id: 99744,
  description: 'Concrete Personal',
};

export const sampleWithPartialData: IIntermediateWorkout = {
  id: 78029,
  description: 'Account Malta',
};

export const sampleWithFullData: IIntermediateWorkout = {
  id: 14147,
  description: 'Small',
};

export const sampleWithNewData: NewIntermediateWorkout = {
  description: 'programming digital',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
