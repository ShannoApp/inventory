import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBusinessConfiguration } from '../business-configuration.model';
import { BusinessConfigurationService } from '../service/business-configuration.service';
import { BusinessConfigurationFormService, BusinessConfigurationFormGroup } from './business-configuration-form.service';
import { BusinessService } from 'app/entities/business/service/business.service';

@Component({
  standalone: true,
  selector: 'jhi-business-configuration-update',
  templateUrl: './business-configuration-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusinessConfigurationUpdateComponent implements OnInit {
  @Input()
  public businessId: string | null | undefined = null;

  isSaving = false;
  businessConfiguration: IBusinessConfiguration | null = null;

  editForm: BusinessConfigurationFormGroup = this.businessConfigurationFormService.createBusinessConfigurationFormGroup();

  constructor(
    protected businessConfigurationService: BusinessConfigurationService,
    protected businessConfigurationFormService: BusinessConfigurationFormService,
    protected activatedRoute: ActivatedRoute,
    protected businessService: BusinessService,
  ) {
    this.businessService.businessData$.subscribe(business => {
      this.businessId = business?.id;
    });
  }

  ngOnInit(): void {
    this.editForm.get('businessId')?.setValue(this.businessId);
    this.activatedRoute.data.subscribe(({ businessConfiguration }) => {
      this.businessConfiguration = businessConfiguration;
      if (businessConfiguration) {
        this.updateForm(businessConfiguration);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const businessConfiguration = this.businessConfigurationFormService.getBusinessConfiguration(this.editForm);
    if (businessConfiguration.id !== null) {
      this.subscribeToSaveResponse(this.businessConfigurationService.update(businessConfiguration));
    } else {
      this.subscribeToSaveResponse(this.businessConfigurationService.create(businessConfiguration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessConfiguration>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(businessConfiguration: IBusinessConfiguration): void {
    this.businessConfiguration = businessConfiguration;
    this.businessConfigurationFormService.resetForm(this.editForm, businessConfiguration);
  }
}
