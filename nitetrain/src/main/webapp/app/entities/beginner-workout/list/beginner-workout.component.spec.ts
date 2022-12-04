import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BeginnerWorkoutService } from '../service/beginner-workout.service';

import { BeginnerWorkoutComponent } from './beginner-workout.component';

describe('BeginnerWorkout Management Component', () => {
  let comp: BeginnerWorkoutComponent;
  let fixture: ComponentFixture<BeginnerWorkoutComponent>;
  let service: BeginnerWorkoutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'beginner-workout', component: BeginnerWorkoutComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [BeginnerWorkoutComponent],
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
      .overrideTemplate(BeginnerWorkoutComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeginnerWorkoutComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BeginnerWorkoutService);

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
    expect(comp.beginnerWorkouts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to beginnerWorkoutService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBeginnerWorkoutIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBeginnerWorkoutIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
