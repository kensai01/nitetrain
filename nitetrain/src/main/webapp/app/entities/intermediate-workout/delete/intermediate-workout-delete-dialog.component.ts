import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntermediateWorkout } from '../intermediate-workout.model';
import { IntermediateWorkoutService } from '../service/intermediate-workout.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './intermediate-workout-delete-dialog.component.html',
})
export class IntermediateWorkoutDeleteDialogComponent {
  intermediateWorkout?: IIntermediateWorkout;

  constructor(protected intermediateWorkoutService: IntermediateWorkoutService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.intermediateWorkoutService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
