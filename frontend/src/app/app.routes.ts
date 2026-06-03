import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'currencies',
    loadChildren: () =>
      import('./features/currency/currency.module').then(m => m.CurrencyModule)
  },
  {
    path: 'fx-spot',
    loadChildren: () =>
      import('./features/fx_spot/fx-spot.module').then(m => m.FxSpotModule)
  },
  {
    path: 'instruments',
    loadChildren: () =>
      import('./features/instrument_catalog/instrument-catalog.module')
        .then(m => m.InstrumentCatalogModule)
  },
  {
    path: 'source-passports',
    loadChildren: () =>
      import('./features/source_passport/source-passport.module')
        .then(m => m.SourcePassportModule)
  },
  {
    path: 'capturer-passports',
    loadChildren: () =>
      import('./features/capturer_passport/capturer-passport.module')
        .then(m => m.CapturerPassportModule)
  },
  {
    path: 'capturer-feeding',
    loadComponent: () =>
      import('./features/capturer_feeding/pages/capturer-feeding/capturer-feeding.component')
        .then(c => c.CapturerFeedingComponent)
  },
  {
    path: 'source-plans',
    loadChildren: () =>
      import('./features/source_plan/source-plan.module')
        .then(m => m.SourcePlanModule)
  },
  {
    path: 'quote-monitor',
    loadChildren: () =>
      import('./features/quote_monitor/quote-monitor.module')
        .then(m => m.QuoteMonitorModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: 'sources', redirectTo: 'source-passports', pathMatch: 'full' },
  { path: 'capturers', redirectTo: 'capturer-passports', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];
