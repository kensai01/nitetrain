import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'workout',
        data: { pageTitle: 'nitetrainApp.trainingWorkout.home.title' },
        loadChildren: () => import('./training/workout/workout.module').then(m => m.TrainingWorkoutModule),
      },
      {
        path: 'beginner-workout',
        data: { pageTitle: 'nitetrainApp.trainingBeginnerWorkout.home.title' },
        loadChildren: () => import('./training/beginner-workout/beginner-workout.module').then(m => m.TrainingBeginnerWorkoutModule),
      },
      {
        path: 'workout-step',
        data: { pageTitle: 'nitetrainApp.trainingWorkoutStep.home.title' },
        loadChildren: () => import('./training/workout-step/workout-step.module').then(m => m.TrainingWorkoutStepModule),
      },
      {
        path: 'price',
        data: { pageTitle: 'nitetrainApp.billingPrice.home.title' },
        loadChildren: () => import('./billing/price/price.module').then(m => m.BillingPriceModule),
      },
      {
        path: 'intermediate-workout',
        data: { pageTitle: 'nitetrainApp.trainingIntermediateWorkout.home.title' },
        loadChildren: () =>
          import('./training/intermediate-workout/intermediate-workout.module').then(m => m.TrainingIntermediateWorkoutModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
