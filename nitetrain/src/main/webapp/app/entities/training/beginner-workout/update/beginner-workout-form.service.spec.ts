import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../beginner-workout.test-samples';

import { BeginnerWorkoutFormService } from './beginner-workout-form.service';

describe('BeginnerWorkout Form Service', () => {
  let service: BeginnerWorkoutFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeginnerWorkoutFormService);
  });

  describe('Service methods', () => {
    describe('createBeginnerWorkoutFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBeginnerWorkoutFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            workout: expect.any(Object),
          })
        );
      });

      it('passing IBeginnerWorkout should create a new form with FormGroup', () => {
        const formGroup = service.createBeginnerWorkoutFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            workout: expect.any(Object),
          })
        );
      });
    });

    describe('getBeginnerWorkout', () => {
      it('should return NewBeginnerWorkout for default BeginnerWorkout initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBeginnerWorkoutFormGroup(sampleWithNewData);

        const beginnerWorkout = service.getBeginnerWorkout(formGroup) as any;

        expect(beginnerWorkout).toMatchObject(sampleWithNewData);
      });

      it('should return NewBeginnerWorkout for empty BeginnerWorkout initial value', () => {
        const formGroup = service.createBeginnerWorkoutFormGroup();

        const beginnerWorkout = service.getBeginnerWorkout(formGroup) as any;

        expect(beginnerWorkout).toMatchObject({});
      });

      it('should return IBeginnerWorkout', () => {
        const formGroup = service.createBeginnerWorkoutFormGroup(sampleWithRequiredData);

        const beginnerWorkout = service.getBeginnerWorkout(formGroup) as any;

        expect(beginnerWorkout).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBeginnerWorkout should not enable id FormControl', () => {
        const formGroup = service.createBeginnerWorkoutFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBeginnerWorkout should disable id FormControl', () => {
        const formGroup = service.createBeginnerWorkoutFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
