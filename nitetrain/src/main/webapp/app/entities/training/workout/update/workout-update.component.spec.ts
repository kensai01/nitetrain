import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkoutFormService } from './workout-form.service';
import { WorkoutService } from '../service/workout.service';
import { IWorkout } from '../workout.model';

import { WorkoutUpdateComponent } from './workout-update.component';

describe('Workout Management Update Component', () => {
  let comp: WorkoutUpdateComponent;
  let fixture: ComponentFixture<WorkoutUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workoutFormService: WorkoutFormService;
  let workoutService: WorkoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WorkoutUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WorkoutUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkoutUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workoutFormService = TestBed.inject(WorkoutFormService);
    workoutService = TestBed.inject(WorkoutService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const workout: IWorkout = { id: 456 };

      activatedRoute.data = of({ workout });
      comp.ngOnInit();

      expect(comp.workout).toEqual(workout);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkout>>();
      const workout = { id: 123 };
      jest.spyOn(workoutFormService, 'getWorkout').mockReturnValue(workout);
      jest.spyOn(workoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workout }));
      saveSubject.complete();

      // THEN
      expect(workoutFormService.getWorkout).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workoutService.update).toHaveBeenCalledWith(expect.objectContaining(workout));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkout>>();
      const workout = { id: 123 };
      jest.spyOn(workoutFormService, 'getWorkout').mockReturnValue({ id: null });
      jest.spyOn(workoutService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workout: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workout }));
      saveSubject.complete();

      // THEN
      expect(workoutFormService.getWorkout).toHaveBeenCalled();
      expect(workoutService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkout>>();
      const workout = { id: 123 };
      jest.spyOn(workoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workoutService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
