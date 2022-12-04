import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWorkout, NewWorkout } from '../workout.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkout for edit and NewWorkoutFormGroupInput for create.
 */
type WorkoutFormGroupInput = IWorkout | PartialWithRequiredKeyOf<NewWorkout>;

type WorkoutFormDefaults = Pick<NewWorkout, 'id'>;

type WorkoutFormGroupContent = {
  id: FormControl<IWorkout['id'] | NewWorkout['id']>;
  title: FormControl<IWorkout['title']>;
  description: FormControl<IWorkout['description']>;
  time: FormControl<IWorkout['time']>;
  videoId: FormControl<IWorkout['videoId']>;
  scaling: FormControl<IWorkout['scaling']>;
};

export type WorkoutFormGroup = FormGroup<WorkoutFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkoutFormService {
  createWorkoutFormGroup(workout: WorkoutFormGroupInput = { id: null }): WorkoutFormGroup {
    const workoutRawValue = {
      ...this.getFormDefaults(),
      ...workout,
    };
    return new FormGroup<WorkoutFormGroupContent>({
      id: new FormControl(
        { value: workoutRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(workoutRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(workoutRawValue.description, {
        validators: [Validators.required],
      }),
      time: new FormControl(workoutRawValue.time, {
        validators: [Validators.min(0), Validators.max(180)],
      }),
      videoId: new FormControl(workoutRawValue.videoId),
      scaling: new FormControl(workoutRawValue.scaling),
    });
  }

  getWorkout(form: WorkoutFormGroup): IWorkout | NewWorkout {
    return form.getRawValue() as IWorkout | NewWorkout;
  }

  resetForm(form: WorkoutFormGroup, workout: WorkoutFormGroupInput): void {
    const workoutRawValue = { ...this.getFormDefaults(), ...workout };
    form.reset(
      {
        ...workoutRawValue,
        id: { value: workoutRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WorkoutFormDefaults {
    return {
      id: null,
    };
  }
}
