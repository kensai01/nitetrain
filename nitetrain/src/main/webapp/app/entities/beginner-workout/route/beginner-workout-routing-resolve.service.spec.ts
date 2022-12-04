import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBeginnerWorkout } from '../beginner-workout.model';
import { BeginnerWorkoutService } from '../service/beginner-workout.service';

import { BeginnerWorkoutRoutingResolveService } from './beginner-workout-routing-resolve.service';

describe('BeginnerWorkout routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BeginnerWorkoutRoutingResolveService;
  let service: BeginnerWorkoutService;
  let resultBeginnerWorkout: IBeginnerWorkout | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(BeginnerWorkoutRoutingResolveService);
    service = TestBed.inject(BeginnerWorkoutService);
    resultBeginnerWorkout = undefined;
  });

  describe('resolve', () => {
    it('should return IBeginnerWorkout returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBeginnerWorkout = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBeginnerWorkout).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBeginnerWorkout = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBeginnerWorkout).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IBeginnerWorkout>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBeginnerWorkout = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBeginnerWorkout).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
