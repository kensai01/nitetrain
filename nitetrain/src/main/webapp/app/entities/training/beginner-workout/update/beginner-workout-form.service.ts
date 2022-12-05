import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBeginnerWorkout, NewBeginnerWorkout } from '../beginner-workout.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBeginnerWorkout for edit and NewBeginnerWorkoutFormGroupInput for create.
 */
type BeginnerWorkoutFormGroupInput = IBeginnerWorkout | PartialWithRequiredKeyOf<NewBeginnerWorkout>;

type BeginnerWorkoutFormDefaults = Pick<NewBeginnerWorkout, 'id'>;

type BeginnerWorkoutFormGroupContent = {
  id: FormControl<IBeginnerWorkout['id'] | NewBeginnerWorkout['id']>;
  description: FormControl<IBeginnerWorkout['description']>;
  workout: FormControl<IBeginnerWorkout['workout']>;
};

export type BeginnerWorkoutFormGroup = FormGroup<BeginnerWorkoutFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BeginnerWorkoutFormService {
  createBeginnerWorkoutFormGroup(beginnerWorkout: BeginnerWorkoutFormGroupInput = { id: null }): BeginnerWorkoutFormGroup {
    const beginnerWorkoutRawValue = {
      ...this.getFormDefaults(),
      ...beginnerWorkout,
    };
    return new FormGroup<BeginnerWorkoutFormGroupContent>({
      id: new FormControl(
        { value: beginnerWorkoutRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(beginnerWorkoutRawValue.description, {
        validators: [Validators.required],
      }),
      workout: new FormControl(beginnerWorkoutRawValue.workout),
    });
  }

  getBeginnerWorkout(form: BeginnerWorkoutFormGroup): IBeginnerWorkout | NewBeginnerWorkout {
    return form.getRawValue() as IBeginnerWorkout | NewBeginnerWorkout;
  }

  resetForm(form: BeginnerWorkoutFormGroup, beginnerWorkout: BeginnerWorkoutFormGroupInput): void {
    const beginnerWorkoutRawValue = { ...this.getFormDefaults(), ...beginnerWorkout };
    form.reset(
      {
        ...beginnerWorkoutRawValue,
        id: { value: beginnerWorkoutRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BeginnerWorkoutFormDefaults {
    return {
      id: null,
    };
  }
}
