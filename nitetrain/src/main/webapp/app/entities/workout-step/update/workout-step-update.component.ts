import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { WorkoutStepFormService, WorkoutStepFormGroup } from './workout-step-form.service';
import { IWorkoutStep } from '../workout-step.model';
import { WorkoutStepService } from '../service/workout-step.service';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';
import { IBeginnerWorkout } from 'app/entities/beginner-workout/beginner-workout.model';
import { BeginnerWorkoutService } from 'app/entities/beginner-workout/service/beginner-workout.service';
import { IIntermediateWorkout } from 'app/entities/intermediate-workout/intermediate-workout.model';
import { IntermediateWorkoutService } from 'app/entities/intermediate-workout/service/intermediate-workout.service';

@Component({
  selector: 'jhi-workout-step-update',
  templateUrl: './workout-step-update.component.html',
})
export class WorkoutStepUpdateComponent implements OnInit {
  isSaving = false;
  workoutStep: IWorkoutStep | null = null;

  workoutsSharedCollection: IWorkout[] = [];
  beginnerWorkoutsSharedCollection: IBeginnerWorkout[] = [];
  intermediateWorkoutsSharedCollection: IIntermediateWorkout[] = [];

  editForm: WorkoutStepFormGroup = this.workoutStepFormService.createWorkoutStepFormGroup();

  constructor(
    protected workoutStepService: WorkoutStepService,
    protected workoutStepFormService: WorkoutStepFormService,
    protected workoutService: WorkoutService,
    protected beginnerWorkoutService: BeginnerWorkoutService,
    protected intermediateWorkoutService: IntermediateWorkoutService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareWorkout = (o1: IWorkout | null, o2: IWorkout | null): boolean => this.workoutService.compareWorkout(o1, o2);

  compareBeginnerWorkout = (o1: IBeginnerWorkout | null, o2: IBeginnerWorkout | null): boolean =>
    this.beginnerWorkoutService.compareBeginnerWorkout(o1, o2);

  compareIntermediateWorkout = (o1: IIntermediateWorkout | null, o2: IIntermediateWorkout | null): boolean =>
    this.intermediateWorkoutService.compareIntermediateWorkout(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutStep }) => {
      this.workoutStep = workoutStep;
      if (workoutStep) {
        this.updateForm(workoutStep);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workoutStep = this.workoutStepFormService.getWorkoutStep(this.editForm);
    if (workoutStep.id !== null) {
      this.subscribeToSaveResponse(this.workoutStepService.update(workoutStep));
    } else {
      this.subscribeToSaveResponse(this.workoutStepService.create(workoutStep));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkoutStep>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workoutStep: IWorkoutStep): void {
    this.workoutStep = workoutStep;
    this.workoutStepFormService.resetForm(this.editForm, workoutStep);

    this.workoutsSharedCollection = this.workoutService.addWorkoutToCollectionIfMissing<IWorkout>(
      this.workoutsSharedCollection,
      workoutStep.workout
    );
    this.beginnerWorkoutsSharedCollection = this.beginnerWorkoutService.addBeginnerWorkoutToCollectionIfMissing<IBeginnerWorkout>(
      this.beginnerWorkoutsSharedCollection,
      workoutStep.beginnerWorkout
    );
    this.intermediateWorkoutsSharedCollection =
      this.intermediateWorkoutService.addIntermediateWorkoutToCollectionIfMissing<IIntermediateWorkout>(
        this.intermediateWorkoutsSharedCollection,
        workoutStep.intermediateWorkout
      );
  }

  protected loadRelationshipsOptions(): void {
    this.workoutService
      .query()
      .pipe(map((res: HttpResponse<IWorkout[]>) => res.body ?? []))
      .pipe(
        map((workouts: IWorkout[]) => this.workoutService.addWorkoutToCollectionIfMissing<IWorkout>(workouts, this.workoutStep?.workout))
      )
      .subscribe((workouts: IWorkout[]) => (this.workoutsSharedCollection = workouts));

    this.beginnerWorkoutService
      .query()
      .pipe(map((res: HttpResponse<IBeginnerWorkout[]>) => res.body ?? []))
      .pipe(
        map((beginnerWorkouts: IBeginnerWorkout[]) =>
          this.beginnerWorkoutService.addBeginnerWorkoutToCollectionIfMissing<IBeginnerWorkout>(
            beginnerWorkouts,
            this.workoutStep?.beginnerWorkout
          )
        )
      )
      .subscribe((beginnerWorkouts: IBeginnerWorkout[]) => (this.beginnerWorkoutsSharedCollection = beginnerWorkouts));

    this.intermediateWorkoutService
      .query()
      .pipe(map((res: HttpResponse<IIntermediateWorkout[]>) => res.body ?? []))
      .pipe(
        map((intermediateWorkouts: IIntermediateWorkout[]) =>
          this.intermediateWorkoutService.addIntermediateWorkoutToCollectionIfMissing<IIntermediateWorkout>(
            intermediateWorkouts,
            this.workoutStep?.intermediateWorkout
          )
        )
      )
      .subscribe((intermediateWorkouts: IIntermediateWorkout[]) => (this.intermediateWorkoutsSharedCollection = intermediateWorkouts));
  }
}
