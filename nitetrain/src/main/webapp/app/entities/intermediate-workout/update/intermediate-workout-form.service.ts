import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIntermediateWorkout, NewIntermediateWorkout } from '../intermediate-workout.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIntermediateWorkout for edit and NewIntermediateWorkoutFormGroupInput for create.
 */
type IntermediateWorkoutFormGroupInput = IIntermediateWorkout | PartialWithRequiredKeyOf<NewIntermediateWorkout>;

type IntermediateWorkoutFormDefaults = Pick<NewIntermediateWorkout, 'id'>;

type IntermediateWorkoutFormGroupContent = {
  id: FormControl<IIntermediateWorkout['id'] | NewIntermediateWorkout['id']>;
  description: FormControl<IIntermediateWorkout['description']>;
  workout: FormControl<IIntermediateWorkout['workout']>;
};

export type IntermediateWorkoutFormGroup = FormGroup<IntermediateWorkoutFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IntermediateWorkoutFormService {
  createIntermediateWorkoutFormGroup(intermediateWorkout: IntermediateWorkoutFormGroupInput = { id: null }): IntermediateWorkoutFormGroup {
    const intermediateWorkoutRawValue = {
      ...this.getFormDefaults(),
      ...intermediateWorkout,
    };
    return new FormGroup<IntermediateWorkoutFormGroupContent>({
      id: new FormControl(
        { value: intermediateWorkoutRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(intermediateWorkoutRawValue.description, {
        validators: [Validators.required],
      }),
      workout: new FormControl(intermediateWorkoutRawValue.workout),
    });
  }

  getIntermediateWorkout(form: IntermediateWorkoutFormGroup): IIntermediateWorkout | NewIntermediateWorkout {
    return form.getRawValue() as IIntermediateWorkout | NewIntermediateWorkout;
  }

  resetForm(form: IntermediateWorkoutFormGroup, intermediateWorkout: IntermediateWorkoutFormGroupInput): void {
    const intermediateWorkoutRawValue = { ...this.getFormDefaults(), ...intermediateWorkout };
    form.reset(
      {
        ...intermediateWorkoutRawValue,
        id: { value: intermediateWorkoutRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IntermediateWorkoutFormDefaults {
    return {
      id: null,
    };
  }
}
