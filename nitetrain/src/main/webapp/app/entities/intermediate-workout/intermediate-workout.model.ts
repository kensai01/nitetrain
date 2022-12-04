import { IWorkout } from 'app/entities/workout/workout.model';

export interface IIntermediateWorkout {
  id: number;
  description?: string | null;
  workout?: Pick<IWorkout, 'id'> | null;
}

export type NewIntermediateWorkout = Omit<IIntermediateWorkout, 'id'> & { id: null };
