import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInvoice } from '../invoice.model';
import jspdf from 'jspdf';
import html2canvas from 'html2canvas';
import { FormsModule } from '@angular/forms';
import { InvoiceService } from '../service/invoice.service';

@Component({
  selector: 'jhi-invoice-detail',
  templateUrl: './invoice-detail.component.html',
  styleUrls: ['./invoice-detail.component.scss'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, FormsModule],
  standalone: true, // Note: RouterModule, pipes, and services should not be listed here
})
export class InvoiceDetailComponent {
  @Input() invoice: IInvoice | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private invoiceService: InvoiceService,
  ) {}

  /**
   * Navigates to the previous state in the browser's history.
   */
  previousState(): void {
    window.history.back();
  }

  /**
   * Updates the 'paid' status of the invoice.
   * @param event - The checkbox change event.
   */
  isPaid(event: any): void {
    this.invoice!.paid = event.target.checked;
    const invoice = this.invoice;
    if (invoice) {
      // Update the invoice's 'paid' status through the service
      this.invoiceService.update(invoice).subscribe(response => {
        this.invoice = response.body;
      });
    }
  }

  /**
   * Downloads the invoice as a PDF.
   */
  downloadPdf(): void {
    const data = document.getElementById('invoice-detail') ?? document.body;

    // Capture the HTML content as an image using html2canvas
    html2canvas(data).then(canvas => {
      const imgWidth = this.getImgWidth(); // Calculate image width based on device type
      const pageDimensions = this.getPageDimensions(); // Set page dimensions based on device type

      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      const heightLeft = imgHeight;

      const contentDataURL = canvas.toDataURL('image/png');
      const pdf = new jspdf('p', 'mm', pageDimensions as [number, number]); // Create a new PDF document
      const position = 0;

      // Add the captured image to the PDF
      pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight);

      // Save the generated PDF
      pdf.save('invoice.pdf');
    });
  }

  getImgWidth(): number {
    // Adjust image width based on screen width
    return window.innerWidth > 768 ? 208 : 140; // Adjust these values as needed
  }

  getPageDimensions(): [number, number] | [string, string] {
    // Set page dimensions based on screen width
    return window.innerWidth > 768 ? [210, 297] : [140, 330]; // Adjust these values as needed
  }
}
