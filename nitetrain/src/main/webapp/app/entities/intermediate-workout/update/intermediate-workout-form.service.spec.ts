import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../intermediate-workout.test-samples';

import { IntermediateWorkoutFormService } from './intermediate-workout-form.service';

describe('IntermediateWorkout Form Service', () => {
  let service: IntermediateWorkoutFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IntermediateWorkoutFormService);
  });

  describe('Service methods', () => {
    describe('createIntermediateWorkoutFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIntermediateWorkoutFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            workout: expect.any(Object),
          })
        );
      });

      it('passing IIntermediateWorkout should create a new form with FormGroup', () => {
        const formGroup = service.createIntermediateWorkoutFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            workout: expect.any(Object),
          })
        );
      });
    });

    describe('getIntermediateWorkout', () => {
      it('should return NewIntermediateWorkout for default IntermediateWorkout initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIntermediateWorkoutFormGroup(sampleWithNewData);

        const intermediateWorkout = service.getIntermediateWorkout(formGroup) as any;

        expect(intermediateWorkout).toMatchObject(sampleWithNewData);
      });

      it('should return NewIntermediateWorkout for empty IntermediateWorkout initial value', () => {
        const formGroup = service.createIntermediateWorkoutFormGroup();

        const intermediateWorkout = service.getIntermediateWorkout(formGroup) as any;

        expect(intermediateWorkout).toMatchObject({});
      });

      it('should return IIntermediateWorkout', () => {
        const formGroup = service.createIntermediateWorkoutFormGroup(sampleWithRequiredData);

        const intermediateWorkout = service.getIntermediateWorkout(formGroup) as any;

        expect(intermediateWorkout).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIntermediateWorkout should not enable id FormControl', () => {
        const formGroup = service.createIntermediateWorkoutFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIntermediateWorkout should disable id FormControl', () => {
        const formGroup = service.createIntermediateWorkoutFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
