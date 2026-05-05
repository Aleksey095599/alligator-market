import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/market-data-capture-process-passport-list/market-data-capture-process-passport-list.component')
        .then(c => c.MarketDataCaptureProcessPassportListComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MarketDataCaptureProcessPassportRoutingModule {}
