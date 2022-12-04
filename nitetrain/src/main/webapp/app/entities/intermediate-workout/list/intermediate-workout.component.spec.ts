import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IntermediateWorkoutService } from '../service/intermediate-workout.service';

import { IntermediateWorkoutComponent } from './intermediate-workout.component';

describe('IntermediateWorkout Management Component', () => {
  let comp: IntermediateWorkoutComponent;
  let fixture: ComponentFixture<IntermediateWorkoutComponent>;
  let service: IntermediateWorkoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'intermediate-workout', component: IntermediateWorkoutComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [IntermediateWorkoutComponent],
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
      .overrideTemplate(IntermediateWorkoutComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IntermediateWorkoutComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IntermediateWorkoutService);

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
    expect(comp.intermediateWorkouts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to intermediateWorkoutService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getIntermediateWorkoutIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getIntermediateWorkoutIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
