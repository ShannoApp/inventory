import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BusinessConfigurationService } from '../service/business-configuration.service';
import { IBusinessConfiguration } from '../business-configuration.model';
import { BusinessConfigurationFormService } from './business-configuration-form.service';

import { BusinessConfigurationUpdateComponent } from './business-configuration-update.component';

describe('BusinessConfiguration Management Update Component', () => {
  let comp: BusinessConfigurationUpdateComponent;
  let fixture: ComponentFixture<BusinessConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessConfigurationFormService: BusinessConfigurationFormService;
  let businessConfigurationService: BusinessConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BusinessConfigurationUpdateComponent],
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
      .overrideTemplate(BusinessConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessConfigurationFormService = TestBed.inject(BusinessConfigurationFormService);
    businessConfigurationService = TestBed.inject(BusinessConfigurationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const businessConfiguration: IBusinessConfiguration = { id: 'CBA' };

      activatedRoute.data = of({ businessConfiguration });
      comp.ngOnInit();

      expect(comp.businessConfiguration).toEqual(businessConfiguration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessConfiguration>>();
      const businessConfiguration = { id: 'ABC' };
      jest.spyOn(businessConfigurationFormService, 'getBusinessConfiguration').mockReturnValue(businessConfiguration);
      jest.spyOn(businessConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessConfiguration }));
      saveSubject.complete();

      // THEN
      expect(businessConfigurationFormService.getBusinessConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(businessConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessConfiguration>>();
      const businessConfiguration = { id: 'ABC' };
      jest.spyOn(businessConfigurationFormService, 'getBusinessConfiguration').mockReturnValue({ id: null });
      jest.spyOn(businessConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessConfiguration }));
      saveSubject.complete();

      // THEN
      expect(businessConfigurationFormService.getBusinessConfiguration).toHaveBeenCalled();
      expect(businessConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessConfiguration>>();
      const businessConfiguration = { id: 'ABC' };
      jest.spyOn(businessConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessConfigurationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
