import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeginnerWorkoutFormService } from './beginner-workout-form.service';
import { BeginnerWorkoutService } from '../service/beginner-workout.service';
import { IBeginnerWorkout } from '../beginner-workout.model';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';

import { BeginnerWorkoutUpdateComponent } from './beginner-workout-update.component';

describe('BeginnerWorkout Management Update Component', () => {
  let comp: BeginnerWorkoutUpdateComponent;
  let fixture: ComponentFixture<BeginnerWorkoutUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beginnerWorkoutFormService: BeginnerWorkoutFormService;
  let beginnerWorkoutService: BeginnerWorkoutService;
  let workoutService: WorkoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeginnerWorkoutUpdateComponent],
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
      .overrideTemplate(BeginnerWorkoutUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeginnerWorkoutUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beginnerWorkoutFormService = TestBed.inject(BeginnerWorkoutFormService);
    beginnerWorkoutService = TestBed.inject(BeginnerWorkoutService);
    workoutService = TestBed.inject(WorkoutService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call workout query and add missing value', () => {
      const beginnerWorkout: IBeginnerWorkout = { id: 456 };
      const workout: IWorkout = { id: 25366 };
      beginnerWorkout.workout = workout;

      const workoutCollection: IWorkout[] = [{ id: 60815 }];
      jest.spyOn(workoutService, 'query').mockReturnValue(of(new HttpResponse({ body: workoutCollection })));
      const expectedCollection: IWorkout[] = [workout, ...workoutCollection];
      jest.spyOn(workoutService, 'addWorkoutToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beginnerWorkout });
      comp.ngOnInit();

      expect(workoutService.query).toHaveBeenCalled();
      expect(workoutService.addWorkoutToCollectionIfMissing).toHaveBeenCalledWith(workoutCollection, workout);
      expect(comp.workoutsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const beginnerWorkout: IBeginnerWorkout = { id: 456 };
      const workout: IWorkout = { id: 85978 };
      beginnerWorkout.workout = workout;

      activatedRoute.data = of({ beginnerWorkout });
      comp.ngOnInit();

      expect(comp.workoutsCollection).toContain(workout);
      expect(comp.beginnerWorkout).toEqual(beginnerWorkout);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeginnerWorkout>>();
      const beginnerWorkout = { id: 123 };
      jest.spyOn(beginnerWorkoutFormService, 'getBeginnerWorkout').mockReturnValue(beginnerWorkout);
      jest.spyOn(beginnerWorkoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beginnerWorkout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beginnerWorkout }));
      saveSubject.complete();

      // THEN
      expect(beginnerWorkoutFormService.getBeginnerWorkout).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(beginnerWorkoutService.update).toHaveBeenCalledWith(expect.objectContaining(beginnerWorkout));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeginnerWorkout>>();
      const beginnerWorkout = { id: 123 };
      jest.spyOn(beginnerWorkoutFormService, 'getBeginnerWorkout').mockReturnValue({ id: null });
      jest.spyOn(beginnerWorkoutService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beginnerWorkout: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beginnerWorkout }));
      saveSubject.complete();

      // THEN
      expect(beginnerWorkoutFormService.getBeginnerWorkout).toHaveBeenCalled();
      expect(beginnerWorkoutService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeginnerWorkout>>();
      const beginnerWorkout = { id: 123 };
      jest.spyOn(beginnerWorkoutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beginnerWorkout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beginnerWorkoutService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorkout', () => {
      it('Should forward to workoutService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(workoutService, 'compareWorkout');
        comp.compareWorkout(entity, entity2);
        expect(workoutService.compareWorkout).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
