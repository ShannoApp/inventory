import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import SharedModule from '../../shared/shared.module';
import { RouterModule } from '@angular/router';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { BusinessService } from '../../entities/business/service/business.service';
import { IBusiness } from '../../entities/business/business.model';

@Component({
  standalone: true,
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective],
})
export class SidebarComponent {
  businessList: IBusiness[] | null = null;
  deflautBusiness: IBusiness | null = null;

  constructor(protected businessService: BusinessService) {
    this.businessService.businessListData$.subscribe(bl => {
      this.businessList = bl;
    });
    this.businessService.businessData$.subscribe(b => {
      this.deflautBusiness = b;
    });
  }

  changeSelection(business: IBusiness) {
    this.businessService.changeSelectedBusiness(business);
    return;
  }
}
