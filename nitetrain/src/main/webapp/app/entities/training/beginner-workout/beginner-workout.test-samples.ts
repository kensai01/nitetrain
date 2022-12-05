import { IBeginnerWorkout, NewBeginnerWorkout } from './beginner-workout.model';

export const sampleWithRequiredData: IBeginnerWorkout = {
  id: 90193,
  description: 'indigo Games',
};

export const sampleWithPartialData: IBeginnerWorkout = {
  id: 72161,
  description: 'Streamlined experiences',
};

export const sampleWithFullData: IBeginnerWorkout = {
  id: 46260,
  description: 'Dinar River Licensed',
};

export const sampleWithNewData: NewBeginnerWorkout = {
  description: 'incubate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
