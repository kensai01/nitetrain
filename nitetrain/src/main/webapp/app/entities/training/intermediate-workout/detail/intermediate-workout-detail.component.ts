import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntermediateWorkout } from '../intermediate-workout.model';

@Component({
  selector: 'jhi-intermediate-workout-detail',
  templateUrl: './intermediate-workout-detail.component.html',
})
export class IntermediateWorkoutDetailComponent implements OnInit {
  intermediateWorkout: IIntermediateWorkout | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intermediateWorkout }) => {
      this.intermediateWorkout = intermediateWorkout;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
