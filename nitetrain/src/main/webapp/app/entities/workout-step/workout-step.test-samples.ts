import { IWorkoutStep, NewWorkoutStep } from './workout-step.model';

export const sampleWithRequiredData: IWorkoutStep = {
  id: 43254,
  title: 'Carolina',
  description: 'CFA EXE',
  stepNumber: 52674,
};

export const sampleWithPartialData: IWorkoutStep = {
  id: 87215,
  title: 'Metal Kids',
  description: 'action-items',
  stepNumber: 89747,
};

export const sampleWithFullData: IWorkoutStep = {
  id: 77891,
  title: 'Bedfordshire Focused Inverse',
  description: 'HDD Profound backing',
  stepNumber: 714,
};

export const sampleWithNewData: NewWorkoutStep = {
  title: 'User-centric',
  description: 'Faso actuating Fresh',
  stepNumber: 19162,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
