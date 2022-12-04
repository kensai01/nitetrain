import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IntermediateWorkoutFormService, IntermediateWorkoutFormGroup } from './intermediate-workout-form.service';
import { IIntermediateWorkout } from '../intermediate-workout.model';
import { IntermediateWorkoutService } from '../service/intermediate-workout.service';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';

@Component({
  selector: 'jhi-intermediate-workout-update',
  templateUrl: './intermediate-workout-update.component.html',
})
export class IntermediateWorkoutUpdateComponent implements OnInit {
  isSaving = false;
  intermediateWorkout: IIntermediateWorkout | null = null;

  workoutsCollection: IWorkout[] = [];

  editForm: IntermediateWorkoutFormGroup = this.intermediateWorkoutFormService.createIntermediateWorkoutFormGroup();

  constructor(
    protected intermediateWorkoutService: IntermediateWorkoutService,
    protected intermediateWorkoutFormService: IntermediateWorkoutFormService,
    protected workoutService: WorkoutService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareWorkout = (o1: IWorkout | null, o2: IWorkout | null): boolean => this.workoutService.compareWorkout(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intermediateWorkout }) => {
      this.intermediateWorkout = intermediateWorkout;
      if (intermediateWorkout) {
        this.updateForm(intermediateWorkout);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intermediateWorkout = this.intermediateWorkoutFormService.getIntermediateWorkout(this.editForm);
    if (intermediateWorkout.id !== null) {
      this.subscribeToSaveResponse(this.intermediateWorkoutService.update(intermediateWorkout));
    } else {
      this.subscribeToSaveResponse(this.intermediateWorkoutService.create(intermediateWorkout));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntermediateWorkout>>): void {
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

  protected updateForm(intermediateWorkout: IIntermediateWorkout): void {
    this.intermediateWorkout = intermediateWorkout;
    this.intermediateWorkoutFormService.resetForm(this.editForm, intermediateWorkout);

    this.workoutsCollection = this.workoutService.addWorkoutToCollectionIfMissing<IWorkout>(
      this.workoutsCollection,
      intermediateWorkout.workout
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workoutService
      .query({ filter: 'intermediateworkout-is-null' })
      .pipe(map((res: HttpResponse<IWorkout[]>) => res.body ?? []))
      .pipe(
        map((workouts: IWorkout[]) =>
          this.workoutService.addWorkoutToCollectionIfMissing<IWorkout>(workouts, this.intermediateWorkout?.workout)
        )
      )
      .subscribe((workouts: IWorkout[]) => (this.workoutsCollection = workouts));
  }
}
