import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WorkoutStepService } from '../service/workout-step.service';

import { WorkoutStepComponent } from './workout-step.component';

describe('WorkoutStep Management Component', () => {
  let comp: WorkoutStepComponent;
  let fixture: ComponentFixture<WorkoutStepComponent>;
  let service: WorkoutStepService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'workout-step', component: WorkoutStepComponent }]), HttpClientTestingModule],
      declarations: [WorkoutStepComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(WorkoutStepComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkoutStepComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WorkoutStepService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.workoutSteps?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to workoutStepService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getWorkoutStepIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getWorkoutStepIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
