import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'business',
        data: { pageTitle: 'Businesses' },
        loadChildren: () => import('./business/business.routes'),
      },
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.routes'),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'Invoices' },
        loadChildren: () => import('./invoice/invoice.routes'),
      },
      {
        path: 'category',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./category/category.routes'),
      },
      {
        path: 'product',
        data: { pageTitle: 'Products' },
        loadChildren: () => import('./product/product.routes'),
      },
      {
        path: 'business-configuration',
        data: { pageTitle: 'BusinessConfigurations' },
        loadChildren: () => import('./business-configuration/business-configuration.routes'),
      },
      {
        path: 'dashboard',
        data: { pageTitle: 'Dashboards' },
        loadChildren: () => import('./dashboard/dashboard.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
