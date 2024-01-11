import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBusiness } from '../business.model';
import { BusinessConfigurationDetailComponent } from 'app/entities/business-configuration/detail/business-configuration-detail.component';
import { BusinessConfigurationService } from 'app/entities/business-configuration/service/business-configuration.service';

@Component({
  standalone: true,
  selector: 'jhi-business-detail',
  templateUrl: './business-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, BusinessConfigurationDetailComponent, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BusinessDetailComponent {
  @Input() business: IBusiness | null = null;
  businessConfiguration: any;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
