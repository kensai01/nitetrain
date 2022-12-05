import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BeginnerWorkoutDetailComponent } from './beginner-workout-detail.component';

describe('BeginnerWorkout Management Detail Component', () => {
  let comp: BeginnerWorkoutDetailComponent;
  let fixture: ComponentFixture<BeginnerWorkoutDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BeginnerWorkoutDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ beginnerWorkout: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BeginnerWorkoutDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BeginnerWorkoutDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load beginnerWorkout on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.beginnerWorkout).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
