import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/instrument-selection/quote-monitor-instrument-selection.component')
        .then(c => c.QuoteMonitorInstrumentSelectionComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuoteMonitorRoutingModule {}
