import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BusinessConfigurationService } from '../service/business-configuration.service';

import { BusinessConfigurationComponent } from './business-configuration.component';

describe('BusinessConfiguration Management Component', () => {
  let comp: BusinessConfigurationComponent;
  let fixture: ComponentFixture<BusinessConfigurationComponent>;
  let service: BusinessConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'business-configuration', component: BusinessConfigurationComponent }]),
        HttpClientTestingModule,
        BusinessConfigurationComponent,
      ],
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
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(BusinessConfigurationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessConfigurationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BusinessConfigurationService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.businessConfigurations?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to businessConfigurationService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getBusinessConfigurationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBusinessConfigurationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
