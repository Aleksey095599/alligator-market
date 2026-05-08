import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/market-data-capturer-passport-list/market-data-capturer-passport-list.component')
        .then(c => c.MarketDataCapturerPassportListComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MarketDataCapturerPassportRoutingModule {}
