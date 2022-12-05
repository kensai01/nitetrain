import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../workout-step.test-samples';

import { WorkoutStepFormService } from './workout-step-form.service';

describe('WorkoutStep Form Service', () => {
  let service: WorkoutStepFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkoutStepFormService);
  });

  describe('Service methods', () => {
    describe('createWorkoutStepFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkoutStepFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            stepNumber: expect.any(Object),
            workout: expect.any(Object),
            beginnerWorkout: expect.any(Object),
            intermediateWorkout: expect.any(Object),
          })
        );
      });

      it('passing IWorkoutStep should create a new form with FormGroup', () => {
        const formGroup = service.createWorkoutStepFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            stepNumber: expect.any(Object),
            workout: expect.any(Object),
            beginnerWorkout: expect.any(Object),
            intermediateWorkout: expect.any(Object),
          })
        );
      });
    });

    describe('getWorkoutStep', () => {
      it('should return NewWorkoutStep for default WorkoutStep initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWorkoutStepFormGroup(sampleWithNewData);

        const workoutStep = service.getWorkoutStep(formGroup) as any;

        expect(workoutStep).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkoutStep for empty WorkoutStep initial value', () => {
        const formGroup = service.createWorkoutStepFormGroup();

        const workoutStep = service.getWorkoutStep(formGroup) as any;

        expect(workoutStep).toMatchObject({});
      });

      it('should return IWorkoutStep', () => {
        const formGroup = service.createWorkoutStepFormGroup(sampleWithRequiredData);

        const workoutStep = service.getWorkoutStep(formGroup) as any;

        expect(workoutStep).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkoutStep should not enable id FormControl', () => {
        const formGroup = service.createWorkoutStepFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkoutStep should disable id FormControl', () => {
        const formGroup = service.createWorkoutStepFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
