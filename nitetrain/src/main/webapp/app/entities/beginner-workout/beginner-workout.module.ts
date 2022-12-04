import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BeginnerWorkoutComponent } from './list/beginner-workout.component';
import { BeginnerWorkoutDetailComponent } from './detail/beginner-workout-detail.component';
import { BeginnerWorkoutUpdateComponent } from './update/beginner-workout-update.component';
import { BeginnerWorkoutDeleteDialogComponent } from './delete/beginner-workout-delete-dialog.component';
import { BeginnerWorkoutRoutingModule } from './route/beginner-workout-routing.module';

@NgModule({
  imports: [SharedModule, BeginnerWorkoutRoutingModule],
  declarations: [
    BeginnerWorkoutComponent,
    BeginnerWorkoutDetailComponent,
    BeginnerWorkoutUpdateComponent,
    BeginnerWorkoutDeleteDialogComponent,
  ],
})
export class BeginnerWorkoutModule {}
