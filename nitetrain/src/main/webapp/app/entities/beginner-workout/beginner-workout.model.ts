import { IWorkout } from 'app/entities/workout/workout.model';

export interface IBeginnerWorkout {
  id: number;
  description?: string | null;
  workout?: Pick<IWorkout, 'id'> | null;
}

export type NewBeginnerWorkout = Omit<IBeginnerWorkout, 'id'> & { id: null };
