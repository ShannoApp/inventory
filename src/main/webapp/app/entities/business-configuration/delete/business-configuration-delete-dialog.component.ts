import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBusinessConfiguration } from '../business-configuration.model';
import { BusinessConfigurationService } from '../service/business-configuration.service';

@Component({
  standalone: true,
  templateUrl: './business-configuration-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BusinessConfigurationDeleteDialogComponent {
  businessConfiguration?: IBusinessConfiguration;

  constructor(
    protected businessConfigurationService: BusinessConfigurationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.businessConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
