import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWorkoutStep, NewWorkoutStep } from '../workout-step.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkoutStep for edit and NewWorkoutStepFormGroupInput for create.
 */
type WorkoutStepFormGroupInput = IWorkoutStep | PartialWithRequiredKeyOf<NewWorkoutStep>;

type WorkoutStepFormDefaults = Pick<NewWorkoutStep, 'id'>;

type WorkoutStepFormGroupContent = {
  id: FormControl<IWorkoutStep['id'] | NewWorkoutStep['id']>;
  title: FormControl<IWorkoutStep['title']>;
  description: FormControl<IWorkoutStep['description']>;
  stepNumber: FormControl<IWorkoutStep['stepNumber']>;
  workout: FormControl<IWorkoutStep['workout']>;
  beginnerWorkout: FormControl<IWorkoutStep['beginnerWorkout']>;
  intermediateWorkout: FormControl<IWorkoutStep['intermediateWorkout']>;
};

export type WorkoutStepFormGroup = FormGroup<WorkoutStepFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkoutStepFormService {
  createWorkoutStepFormGroup(workoutStep: WorkoutStepFormGroupInput = { id: null }): WorkoutStepFormGroup {
    const workoutStepRawValue = {
      ...this.getFormDefaults(),
      ...workoutStep,
    };
    return new FormGroup<WorkoutStepFormGroupContent>({
      id: new FormControl(
        { value: workoutStepRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(workoutStepRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(workoutStepRawValue.description, {
        validators: [Validators.required],
      }),
      stepNumber: new FormControl(workoutStepRawValue.stepNumber, {
        validators: [Validators.required],
      }),
      workout: new FormControl(workoutStepRawValue.workout),
      beginnerWorkout: new FormControl(workoutStepRawValue.beginnerWorkout),
      intermediateWorkout: new FormControl(workoutStepRawValue.intermediateWorkout),
    });
  }

  getWorkoutStep(form: WorkoutStepFormGroup): IWorkoutStep | NewWorkoutStep {
    return form.getRawValue() as IWorkoutStep | NewWorkoutStep;
  }

  resetForm(form: WorkoutStepFormGroup, workoutStep: WorkoutStepFormGroupInput): void {
    const workoutStepRawValue = { ...this.getFormDefaults(), ...workoutStep };
    form.reset(
      {
        ...workoutStepRawValue,
        id: { value: workoutStepRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WorkoutStepFormDefaults {
    return {
      id: null,
    };
  }
}
