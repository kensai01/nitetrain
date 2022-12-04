import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'price',
        data: { pageTitle: 'nitetrainApp.price.home.title' },
        loadChildren: () => import('./price/price.module').then(m => m.PriceModule),
      },
      {
        path: 'beginner-workout',
        data: { pageTitle: 'nitetrainApp.beginnerWorkout.home.title' },
        loadChildren: () => import('./beginner-workout/beginner-workout.module').then(m => m.BeginnerWorkoutModule),
      },
      {
        path: 'intermediate-workout',
        data: { pageTitle: 'nitetrainApp.intermediateWorkout.home.title' },
        loadChildren: () => import('./intermediate-workout/intermediate-workout.module').then(m => m.IntermediateWorkoutModule),
      },
      {
        path: 'workout',
        data: { pageTitle: 'nitetrainApp.workout.home.title' },
        loadChildren: () => import('./workout/workout.module').then(m => m.WorkoutModule),
      },
      {
        path: 'workout-step',
        data: { pageTitle: 'nitetrainApp.workoutStep.home.title' },
        loadChildren: () => import('./workout-step/workout-step.module').then(m => m.WorkoutStepModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
