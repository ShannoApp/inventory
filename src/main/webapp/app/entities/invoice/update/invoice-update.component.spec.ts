import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IBusiness } from 'app/entities/business/business.model';
import { BusinessService } from 'app/entities/business/service/business.service';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { InvoiceFormService } from './invoice-form.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Invoice Management Update Component', () => {
  let comp: InvoiceUpdateComponent;
  let fixture: ComponentFixture<InvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceFormService: InvoiceFormService;
  let invoiceService: InvoiceService;
  let customerService: CustomerService;
  let businessService: BusinessService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InvoiceUpdateComponent],
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
      .overrideTemplate(InvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceFormService = TestBed.inject(InvoiceFormService);
    invoiceService = TestBed.inject(InvoiceService);
    customerService = TestBed.inject(CustomerService);
    businessService = TestBed.inject(BusinessService);

    comp = fixture.componentInstance;
  });

  // describe('ngOnInit', () => {
  //   it('Should call customer query and add missing value', () => {
  //     const invoice: IInvoice = { id: 'CBA' };
  //     const customer: ICustomer = { id: '7a0e35dd-9871-4aef-8726-f29d31266e51' };
  //     invoice.customer = customer;

  //     const customerCollection: ICustomer[] = [{ id: '6fa07e7d-d3f8-4724-8ad0-a3d8473d4eb5' }];
  //     jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
  //     const expectedCollection: ICustomer[] = [customer, ...customerCollection];
  //     jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

  //     activatedRoute.data = of({ invoice });
  //     comp.ngOnInit();

  //     expect(customerService.query).toHaveBeenCalled();
  //     expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(customerCollection, customer);
  //     expect(comp.customersCollection).toEqual(expectedCollection);
  //   });

  //   it('Should call Business query and add missing value', () => {
  //     const invoice: IInvoice = { id: 'CBA' };
  //     const business: IBusiness = { id: '515c8431-b7ce-47e8-b48e-32b480e3c959' };
  //     invoice.business = business;

  //     const businessCollection: IBusiness[] = [{ id: '73785062-d9be-48f3-9273-dd8e2b365797' }];
  //     jest.spyOn(businessService, 'query').mockReturnValue(of(new HttpResponse({ body: businessCollection })));
  //     const additionalBusinesses = [business];
  //     const expectedCollection: IBusiness[] = [...additionalBusinesses, ...businessCollection];
  //     jest.spyOn(businessService, 'addBusinessToCollectionIfMissing').mockReturnValue(expectedCollection);

  //     activatedRoute.data = of({ invoice });
  //     comp.ngOnInit();

  //     expect(businessService.query).toHaveBeenCalled();
  //     expect(businessService.addBusinessToCollectionIfMissing).toHaveBeenCalledWith(
  //       businessCollection,
  //       ...additionalBusinesses.map(expect.objectContaining),
  //     );
  //     expect(comp.businessesSharedCollection).toEqual(expectedCollection);
  //   });

  //   it('Should update editForm', () => {
  //     const invoice: IInvoice = { id: 'CBA' };
  //     const customer: ICustomer = { id: '034c10bc-4e90-4d3f-a7cb-dd09e384df97' };
  //     invoice.customer = customer;
  //     const business: IBusiness = { id: 'd88a19df-3933-4626-b9e8-61073b7f108f' };
  //     invoice.business = business;

  //     activatedRoute.data = of({ invoice });
  //     comp.ngOnInit();

  //     expect(comp.customersCollection).toContain(customer);
  //     expect(comp.businessesSharedCollection).toContain(business);
  //     expect(comp.invoice).toEqual(invoice);
  //   });
  // });

  // describe('save', () => {
  //   it('Should call update service on save for existing entity', () => {
  //     // GIVEN
  //     const saveSubject = new Subject<HttpResponse<IInvoice>>();
  //     const invoice = { id: 'ABC' };
  //     jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue(invoice);
  //     jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
  //     jest.spyOn(comp, 'previousState');
  //     activatedRoute.data = of({ invoice });
  //     comp.ngOnInit();

  //     // WHEN
  //     comp.save();
  //     expect(comp.isSaving).toEqual(true);
  //     saveSubject.next(new HttpResponse({ body: invoice }));
  //     saveSubject.complete();

  //     // THEN
  //     expect(invoiceFormService.getInvoice).toHaveBeenCalled();
  //     expect(comp.previousState).toHaveBeenCalled();
  //     expect(invoiceService.update).toHaveBeenCalledWith(expect.objectContaining(invoice));
  //     expect(comp.isSaving).toEqual(false);
  //   });

  //   it('Should call create service on save for new entity', () => {
  //     // GIVEN
  //     const saveSubject = new Subject<HttpResponse<IInvoice>>();
  //     const invoice = { id: 'ABC' };
  //     jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue({ id: null });
  //     jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
  //     jest.spyOn(comp, 'previousState');
  //     activatedRoute.data = of({ invoice: null });
  //     comp.ngOnInit();

  //     // WHEN
  //     comp.save();
  //     expect(comp.isSaving).toEqual(true);
  //     saveSubject.next(new HttpResponse({ body: invoice }));
  //     saveSubject.complete();

  //     // THEN
  //     expect(invoiceFormService.getInvoice).toHaveBeenCalled();
  //     expect(invoiceService.create).toHaveBeenCalled();
  //     expect(comp.isSaving).toEqual(false);
  //     expect(comp.previousState).toHaveBeenCalled();
  //   });

  //   it('Should set isSaving to false on error', () => {
  //     // GIVEN
  //     const saveSubject = new Subject<HttpResponse<IInvoice>>();
  //     const invoice = { id: 'ABC' };
  //     jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
  //     jest.spyOn(comp, 'previousState');
  //     activatedRoute.data = of({ invoice });
  //     comp.ngOnInit();

  //     // WHEN
  //     comp.save();
  //     expect(comp.isSaving).toEqual(true);
  //     saveSubject.error('This is an error!');

  //     // THEN
  //     expect(invoiceService.update).toHaveBeenCalled();
  //     expect(comp.isSaving).toEqual(false);
  //     expect(comp.previousState).not.toHaveBeenCalled();
  //   });
  // });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
