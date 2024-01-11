import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BusinessConfigurationComponent } from './list/business-configuration.component';
import { BusinessConfigurationDetailComponent } from './detail/business-configuration-detail.component';
import { BusinessConfigurationUpdateComponent } from './update/business-configuration-update.component';
import BusinessConfigurationResolve from './route/business-configuration-routing-resolve.service';

const businessConfigurationRoute: Routes = [
  {
    path: '',
    component: BusinessConfigurationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessConfigurationDetailComponent,
    resolve: {
      businessConfiguration: BusinessConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessConfigurationUpdateComponent,
    resolve: {
      businessConfiguration: BusinessConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessConfigurationUpdateComponent,
    resolve: {
      businessConfiguration: BusinessConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default businessConfigurationRoute;
