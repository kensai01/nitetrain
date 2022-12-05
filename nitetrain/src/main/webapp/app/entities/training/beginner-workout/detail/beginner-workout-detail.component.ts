import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBeginnerWorkout } from '../beginner-workout.model';

@Component({
  selector: 'jhi-beginner-workout-detail',
  templateUrl: './beginner-workout-detail.component.html',
})
export class BeginnerWorkoutDetailComponent implements OnInit {
  beginnerWorkout: IBeginnerWorkout | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beginnerWorkout }) => {
      this.beginnerWorkout = beginnerWorkout;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
