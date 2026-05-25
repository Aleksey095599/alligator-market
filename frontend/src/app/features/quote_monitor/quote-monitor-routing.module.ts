import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'live-quotes'
  },
  {
    path: 'instruments',
    loadComponent: () =>
      import('./pages/instrument-selection/quote-monitor-instrument-selection.component')
        .then(c => c.QuoteMonitorInstrumentSelectionComponent)
  },
  {
    path: 'live-quotes',
    loadComponent: () =>
      import('./pages/live-quotes/quote-monitor-live-quotes.component')
        .then(c => c.QuoteMonitorLiveQuotesComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuoteMonitorRoutingModule {}
