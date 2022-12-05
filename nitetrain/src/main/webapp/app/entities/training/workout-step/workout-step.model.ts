import { IWorkout } from 'app/entities/training/workout/workout.model';
import { IBeginnerWorkout } from 'app/entities/training/beginner-workout/beginner-workout.model';
import { IIntermediateWorkout } from 'app/entities/training/intermediate-workout/intermediate-workout.model';

export interface IWorkoutStep {
  id: number;
  title?: string | null;
  description?: string | null;
  stepNumber?: number | null;
  workout?: Pick<IWorkout, 'id'> | null;
  beginnerWorkout?: Pick<IBeginnerWorkout, 'id'> | null;
  intermediateWorkout?: Pick<IIntermediateWorkout, 'id'> | null;
}

export type NewWorkoutStep = Omit<IWorkoutStep, 'id'> & { id: null };
