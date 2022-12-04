import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { WorkoutFormService, WorkoutFormGroup } from './workout-form.service';
import { IWorkout } from '../workout.model';
import { WorkoutService } from '../service/workout.service';

@Component({
  selector: 'jhi-workout-update',
  templateUrl: './workout-update.component.html',
})
export class WorkoutUpdateComponent implements OnInit {
  isSaving = false;
  workout: IWorkout | null = null;

  editForm: WorkoutFormGroup = this.workoutFormService.createWorkoutFormGroup();

  constructor(
    protected workoutService: WorkoutService,
    protected workoutFormService: WorkoutFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workout }) => {
      this.workout = workout;
      if (workout) {
        this.updateForm(workout);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workout = this.workoutFormService.getWorkout(this.editForm);
    if (workout.id !== null) {
      this.subscribeToSaveResponse(this.workoutService.update(workout));
    } else {
      this.subscribeToSaveResponse(this.workoutService.create(workout));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkout>>): void {
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

  protected updateForm(workout: IWorkout): void {
    this.workout = workout;
    this.workoutFormService.resetForm(this.editForm, workout);
  }
}
