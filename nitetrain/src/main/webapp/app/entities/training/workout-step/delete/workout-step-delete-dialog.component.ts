import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkoutStep } from '../workout-step.model';
import { WorkoutStepService } from '../service/workout-step.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './workout-step-delete-dialog.component.html',
})
export class WorkoutStepDeleteDialogComponent {
  workoutStep?: IWorkoutStep;

  constructor(protected workoutStepService: WorkoutStepService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workoutStepService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
