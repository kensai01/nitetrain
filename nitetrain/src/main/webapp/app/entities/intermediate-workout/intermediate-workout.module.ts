import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IntermediateWorkoutComponent } from './list/intermediate-workout.component';
import { IntermediateWorkoutDetailComponent } from './detail/intermediate-workout-detail.component';
import { IntermediateWorkoutUpdateComponent } from './update/intermediate-workout-update.component';
import { IntermediateWorkoutDeleteDialogComponent } from './delete/intermediate-workout-delete-dialog.component';
import { IntermediateWorkoutRoutingModule } from './route/intermediate-workout-routing.module';

@NgModule({
  imports: [SharedModule, IntermediateWorkoutRoutingModule],
  declarations: [
    IntermediateWorkoutComponent,
    IntermediateWorkoutDetailComponent,
    IntermediateWorkoutUpdateComponent,
    IntermediateWorkoutDeleteDialogComponent,
  ],
})
export class IntermediateWorkoutModule {}
