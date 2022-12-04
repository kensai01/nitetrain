import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BeginnerWorkoutFormService, BeginnerWorkoutFormGroup } from './beginner-workout-form.service';
import { IBeginnerWorkout } from '../beginner-workout.model';
import { BeginnerWorkoutService } from '../service/beginner-workout.service';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';

@Component({
  selector: 'jhi-beginner-workout-update',
  templateUrl: './beginner-workout-update.component.html',
})
export class BeginnerWorkoutUpdateComponent implements OnInit {
  isSaving = false;
  beginnerWorkout: IBeginnerWorkout | null = null;

  workoutsCollection: IWorkout[] = [];

  editForm: BeginnerWorkoutFormGroup = this.beginnerWorkoutFormService.createBeginnerWorkoutFormGroup();

  constructor(
    protected beginnerWorkoutService: BeginnerWorkoutService,
    protected beginnerWorkoutFormService: BeginnerWorkoutFormService,
    protected workoutService: WorkoutService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareWorkout = (o1: IWorkout | null, o2: IWorkout | null): boolean => this.workoutService.compareWorkout(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beginnerWorkout }) => {
      this.beginnerWorkout = beginnerWorkout;
      if (beginnerWorkout) {
        this.updateForm(beginnerWorkout);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beginnerWorkout = this.beginnerWorkoutFormService.getBeginnerWorkout(this.editForm);
    if (beginnerWorkout.id !== null) {
      this.subscribeToSaveResponse(this.beginnerWorkoutService.update(beginnerWorkout));
    } else {
      this.subscribeToSaveResponse(this.beginnerWorkoutService.create(beginnerWorkout));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeginnerWorkout>>): void {
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

  protected updateForm(beginnerWorkout: IBeginnerWorkout): void {
    this.beginnerWorkout = beginnerWorkout;
    this.beginnerWorkoutFormService.resetForm(this.editForm, beginnerWorkout);

    this.workoutsCollection = this.workoutService.addWorkoutToCollectionIfMissing<IWorkout>(
      this.workoutsCollection,
      beginnerWorkout.workout
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workoutService
      .query({ filter: 'beginnerworkout-is-null' })
      .pipe(map((res: HttpResponse<IWorkout[]>) => res.body ?? []))
      .pipe(
        map((workouts: IWorkout[]) =>
          this.workoutService.addWorkoutToCollectionIfMissing<IWorkout>(workouts, this.beginnerWorkout?.workout)
        )
      )
      .subscribe((workouts: IWorkout[]) => (this.workoutsCollection = workouts));
  }
}
