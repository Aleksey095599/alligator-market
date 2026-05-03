import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/market-data-source-plan-admin/market-data-source-plan-admin.component')
        .then(c => c.MarketDataSourcePlanAdminComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MarketDataSourcePlanRoutingModule {}
