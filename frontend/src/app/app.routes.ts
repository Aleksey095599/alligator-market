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
    path: 'market-data-source-passports',
    loadChildren: () =>
      import('./features/market_data_source_passport/market-data-source-passport.module')
        .then(m => m.MarketDataSourcePassportModule)
  },
  {
    path: 'market-data-capture-processes',
    loadChildren: () =>
      import('./features/market_data_capture_process_passport/market-data-capture-process-passport.module')
        .then(m => m.MarketDataCaptureProcessPassportModule)
  },
  {
    path: 'market-data-source-plans',
    loadChildren: () =>
      import('./features/source_plan/market-data-source-plan.module')
        .then(m => m.MarketDataSourcePlanModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
