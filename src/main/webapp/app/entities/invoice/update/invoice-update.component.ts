import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Route, Router, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, finalize, map, switchMap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ParseLinks } from 'app/core/util/parse-links.service';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IBusiness } from 'app/entities/business/business.model';
import { BusinessService } from 'app/entities/business/service/business.service';
import { EntityArrayResponseType, InvoiceService } from '../service/invoice.service';
import { IInvoice } from '../invoice.model';
import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';
import { ProductListPopupComponent } from './product-list-popup/product-list-popup.component';
import { ProductService } from '../../product/service/product.service';
import { IProduct } from '../../product/product.model';
import { IBillingItem } from '../../billing-item/billing-item.model';
import { MultiselectComponent } from '../../../shared/components/multiselect/multiselect.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceDeleteDialogComponent } from '../delete/invoice-delete-dialog.component';
import { BusinessConfigurationService } from '../../business-configuration/service/business-configuration.service';
import { IBusinessConfiguration } from '../../business-configuration/business-configuration.model';
import dayjs from 'dayjs/esm';

/**
 * Component for updating an invoice.
 *
 * Has selector 'jhi-invoice-update' and imports common modules needed for
 * invoice update form. Renders the invoice update view defined in
 * invoice-update.component.html.
 */
@Component({
  standalone: true,
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  styleUrls: ['./invoice-update.component.scss'],
  imports: [SharedModule, FormsModule, ReactiveFormsModule, MultiselectComponent, RouterModule],
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  includeTax = false;
  includeShippingCharges = false;
  includeDiscount = false;

  extraFlieds = { discount: 0, tax: 0, shippingCharges: 0, paid: false };

  invoiceDump = { discount: 0, tax: 0, subTotal: 0 };
  openProductPopUp: boolean = false;
  openCustomerPopUp: boolean = false;
  products: IProduct[] = [];
  billingItems: Set<IBillingItem> = new Set<IBillingItem>();

  customersCollection: ICustomer[] = [];
  businessesSharedCollection: IBusiness[] = [];

  businessConfiguration: IBusinessConfiguration | null = null;

  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();
  selectedCustomer: any = null;
  selectedCustomerSet: Set<any> = new Set();
  selectedproductSet: Set<any> = new Set();
  business: IBusiness | null = null;

  constructor(
    protected invoiceService: InvoiceService,
    protected invoiceFormService: InvoiceFormService,
    protected customerService: CustomerService,
    protected businessService: BusinessService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal,
    protected route: Router,
  ) {
    this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => {
      this.products = res.body ?? [];
    });

    this.businessService.businessData$.subscribe(business => {
      this.business = business;
    });
  }

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareBusiness = (o1: IBusiness | null, o2: IBusiness | null): boolean => this.businessService.compareBusiness(o1, o2);

  ngOnInit(): void {
    this.editForm.get('issueDate')?.setValue(dayjs());
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
        this.extraFlieds.discount = this.invoice?.discount ?? 0;
        this.extraFlieds.tax = this.invoice?.tax ?? 0;
        this.extraFlieds.shippingCharges = this.invoice?.shippingCharges ?? 0;
        this.extraFlieds.paid = this.invoice?.paid ?? false;
      }

      this.loadRelationshipsOptions();
    });
  }

  protected previousState(): void {
    window.history.back();
    return;
  }

  protected addItem(): void {
    const modalRef = this.modalService.open(MultiselectComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.items = this.products.map(product => ({
      name: product.name,
      description: product.description,
      obj: product,
      isSelected: false,
    }));

    modalRef.componentInstance.originalItems = modalRef.componentInstance.items;

    modalRef.componentInstance.selectedItems = this.selectedproductSet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe({
      next: res => {
        // this.onResponseSuccess(res);
        this.selectedproductSet = res.selectedItems;
        this.addProductToInvoice(res.selectedItems);
      },
    });
    return;
  }

  protected addCustomer(): void {
    const modalRef = this.modalService.open(MultiselectComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.items = this.customersCollection.map((customer: ICustomer) => ({
      name: `${customer.firstName} ${customer.lastName}`,
      description: customer.phone,
      obj: customer,
    }));
    modalRef.componentInstance.originalItems = modalRef.componentInstance.items;
    modalRef.componentInstance.selectedItems = this.selectedCustomerSet;
    modalRef.componentInstance.selectOne = true;

    modalRef.closed
      // .pipe(
      //   filter(reason => reason === ITEM_DELETED_EVENT),
      //   switchMap(() => this.loadFromBackendWithRouteInformations()),
      // )
      .subscribe({
        next: res => {
          // this.onResponseSuccess(res);
          this.selectedCustomerSet = res.selectedItems;
          this.addCustomerToInvoice(res.selectedItems);
        },
      });
    return;
  }

  protected save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    invoice.business = this.business;
    invoice.billingItems = Array.from(this.billingItems);
    invoice.paid = this.extraFlieds.paid;

    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
    return;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: res => this.onSaveSuccess(res),
      error: () => this.onSaveError(),
    });
    return;
  }

  protected addCustomerToInvoice(items: Set<any>): void {
    const firstItem = [...items.values()][0]; // Get the first item from the Set

    if (firstItem) {
      this.selectedCustomer = firstItem.obj;
      this.editForm.get('customer')?.setValue(firstItem.obj);
    }

    // No need to return, as the function has a void return type
  }

  protected addProductToInvoice(items: Set<any>): void {
    this.openProductPopUp = false;
    let totalAmount: number = 0;
    this.billingItems = new Set<IBillingItem>();
    let subTotal = 0;

    items.forEach(item => {
      const billingItem: any = {};

      billingItem.quantity = 1;
      billingItem.totalAmount = item.obj.sellingPrice ?? 0 * billingItem.quantity;
      billingItem.unitPrice = item.obj.sellingPrice;
      billingItem.product = item.obj;
      billingItem.description = item.obj.name;
      this.billingItems.add(billingItem);
      subTotal = subTotal + billingItem.totalAmount;
    });

    totalAmount = this.calulateTotalAmount(subTotal);
    this.setValueToForm(subTotal, totalAmount);
  }

  protected setValueToForm(subTotal: number, totalAmount: number): void {
    const subTotalControl = this.editForm.get('subTotal');
    const taxControl = this.editForm.get('tax');
    const discountControl = this.editForm.get('discount');
    const shippingChargesControl = this.editForm.get('shippingCharges');
    const totalAmountControl = this.editForm.get('totalAmount');

    if (subTotalControl) {
      subTotalControl.setValue(subTotal);
    }

    if (taxControl) {
      const taxValue = this.extraFlieds.tax;
      taxControl.setValue(this.getPercentValue(taxValue, subTotal));
    }

    if (discountControl) {
      const discountValue = this.extraFlieds.discount;
      discountControl.setValue(this.getPercentValue(discountValue, subTotal));
    }

    if (shippingChargesControl) {
      const shippingChargesValue = this.extraFlieds.shippingCharges;
      shippingChargesControl.setValue(shippingChargesValue);
    }

    if (totalAmountControl) {
      totalAmountControl.setValue(totalAmount);
    }
  }

  protected calulateTotalAmount(totalAmount: number): number {
    const discountAmount = this.getPercentValue(this.extraFlieds.discount, totalAmount);
    const taxAmount = this.getPercentValue(this.extraFlieds.tax, totalAmount);
    return totalAmount - discountAmount + taxAmount + this.extraFlieds.shippingCharges;
  }

  // protected calulateSubTotal(billingItems: IBillingItem[]): number {
  //   billingItems. forEach(item => {
  //     item.totalAmount = item.unitPrice! * item.quantity!;
  //   });
  // }

  protected onQuantityChange(): void {
    let subTotal = 0;
    let totalAmount: number = 0;
    this.billingItems.forEach(item => {
      item.totalAmount = item.unitPrice! * item.quantity!;
      subTotal = subTotal + item.totalAmount;
    });
    totalAmount = this.calulateTotalAmount(subTotal);
    this.setValueToForm(subTotal, totalAmount);
  }

  protected getPercentValue(percent: number | undefined | null, total: number): number {
    return percent ? (total * percent) / 100 : 0;
  }

  protected addToInvoice(items: Set<IProduct>): void {
    this.openProductPopUp = false;
    let totalAmount: number = 0;

    items.forEach(item => {
      const billingItem: any = {};

      billingItem.quantity = 1;
      billingItem.totalAmount = item.sellingPrice ?? 0 * billingItem.quantity;
      billingItem.unitPrice = item.sellingPrice;
      billingItem.product = item;
      billingItem.description = item.name;
      this.billingItems.add(billingItem);
      totalAmount = totalAmount + billingItem.totalAmount;
    });
    this.editForm.get('totalAmount')?.setValue(totalAmount);
    return;
  }

  protected onSaveSuccess(res: HttpResponse<IInvoice>): void {
    // this.previousState();
    this.route.navigate([`/invoice/${res.body!.id}/view`]);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.customersCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(this.customersCollection, invoice.customer);
    // this.businessesSharedCollection = this.businessService.addBusinessToCollectionIfMissing<IBusiness>(
    //   this.businessesSharedCollection,
    //   invoice.business,
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      // .query({ filter: 'invoice-is-null' })
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.invoice?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => {
        this.customersCollection = customers;
      });

    // this.businessService
    //   .query()
    //   .pipe(map((res: HttpResponse<IBusiness[]>) => res.body ?? []))
    //   .pipe(
    //     map((businesses: IBusiness[]) =>
    //       this.businessService.addBusinessToCollectionIfMissing<IBusiness>(businesses, this.invoice?.business),
    //     ),
    //   )
    //   .subscribe((businesses: IBusiness[]) => {
    //     this.businessesSharedCollection = businesses;
    //   });
  }
}
