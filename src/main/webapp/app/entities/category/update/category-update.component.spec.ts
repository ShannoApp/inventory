import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IBusiness } from 'app/entities/business/business.model';
import { BusinessService } from 'app/entities/business/service/business.service';
import { CategoryService } from '../service/category.service';
import { ICategory } from '../category.model';
import { CategoryFormService } from './category-form.service';

import { CategoryUpdateComponent } from './category-update.component';

describe('Category Management Update Component', () => {
  let comp: CategoryUpdateComponent;
  let fixture: ComponentFixture<CategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoryFormService: CategoryFormService;
  let categoryService: CategoryService;
  let businessService: BusinessService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CategoryUpdateComponent],
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
      .overrideTemplate(CategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoryFormService = TestBed.inject(CategoryFormService);
    categoryService = TestBed.inject(CategoryService);
    businessService = TestBed.inject(BusinessService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    // it('Should call Business query and add missing value', () => {
    //   const category: ICategory = { id: 'CBA' };
    //   const business: IBusiness = { id: '6d110505-caed-4c9f-bc3e-ae77b3adc91a' };
    //   category.business = business;

    //   const businessCollection: IBusiness[] = [{ id: '650334f3-94cc-40d3-b81d-0c8534a439cb' }];
    //   jest.spyOn(businessService, 'query').mockReturnValue(of(new HttpResponse({ body: businessCollection })));
    //   const additionalBusinesses = [business];
    //   const expectedCollection: IBusiness[] = [...additionalBusinesses, ...businessCollection];
    //   jest.spyOn(businessService, 'addBusinessToCollectionIfMissing').mockReturnValue(expectedCollection);

    //   activatedRoute.data = of({ category });
    //   comp.ngOnInit();

    //   expect(businessService.query).toHaveBeenCalled();
    //   expect(businessService.addBusinessToCollectionIfMissing).toHaveBeenCalledWith(
    //     businessCollection,
    //     ...additionalBusinesses.map(expect.objectContaining),
    //   );
    //   expect(comp.businessesSharedCollection).toEqual(expectedCollection);
    // });

    it('Should update editForm', () => {
      const category: ICategory = { id: 'CBA' };
      const business: IBusiness = { id: 'fbfff2ab-bb00-45c1-b165-df347bc6225e' };
      category.business = business;

      activatedRoute.data = of({ category });
      comp.ngOnInit();

      // expect(comp.businessesSharedCollection).toContain(business);
      expect(comp.category).toEqual(category);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategory>>();
      const category = { id: 'ABC' };
      jest.spyOn(categoryFormService, 'getCategory').mockReturnValue(category);
      jest.spyOn(categoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ category });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: category }));
      saveSubject.complete();

      // THEN
      expect(categoryFormService.getCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoryService.update).toHaveBeenCalledWith(expect.objectContaining(category));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategory>>();
      const category = { id: 'ABC' };
      jest.spyOn(categoryFormService, 'getCategory').mockReturnValue({ id: null });
      jest.spyOn(categoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ category: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: category }));
      saveSubject.complete();

      // THEN
      expect(categoryFormService.getCategory).toHaveBeenCalled();
      expect(categoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategory>>();
      const category = { id: 'ABC' };
      jest.spyOn(categoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ category });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBusiness', () => {
      it('Should forward to businessService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(businessService, 'compareBusiness');
        comp.compareBusiness(entity, entity2);
        expect(businessService.compareBusiness).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
