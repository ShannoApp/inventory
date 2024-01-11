import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { ICustomer } from '../customer.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';

@Component({
  standalone: true,
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomerDetailComponent implements OnInit {
  @Input() customer: ICustomer | null = null;

  invoices: IInvoice[] | null = [];
  balances: { pendingAmount: number; paidAmount: number; pendingInvoices: number; paidInvoices: number } | null = null;
  isLoading: boolean = true;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected invoiceService: InvoiceService,
  ) {}

  ngOnInit(): void {
    this.invoiceService.findInvoiceByCustomer(this.customer?.id ?? '').subscribe(res => {
      if (res.body != null) {
        this.invoices = res.body;
        this.balances = this.invoiceService.calculatePendingAndPaidAmount(this.invoices);
        this.isLoading = false;
        console.log(this.balances);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
