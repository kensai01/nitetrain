import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBeginnerWorkout } from '../beginner-workout.model';
import { BeginnerWorkoutService } from '../service/beginner-workout.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './beginner-workout-delete-dialog.component.html',
})
export class BeginnerWorkoutDeleteDialogComponent {
  beginnerWorkout?: IBeginnerWorkout;

  constructor(protected beginnerWorkoutService: BeginnerWorkoutService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.beginnerWorkoutService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
