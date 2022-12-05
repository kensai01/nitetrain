import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IntermediateWorkoutDetailComponent } from './intermediate-workout-detail.component';

describe('IntermediateWorkout Management Detail Component', () => {
  let comp: IntermediateWorkoutDetailComponent;
  let fixture: ComponentFixture<IntermediateWorkoutDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IntermediateWorkoutDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ intermediateWorkout: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IntermediateWorkoutDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IntermediateWorkoutDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load intermediateWorkout on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.intermediateWorkout).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
