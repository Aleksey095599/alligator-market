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
    path: 'sources',
    loadChildren: () =>
      import('./features/source_passport/source-passport.module')
        .then(m => m.SourcePassportModule)
  },
  {
    path: 'market-data-capturers',
    loadChildren: () =>
      import('./features/market_data_capturer_passport/market-data-capturer-passport.module')
        .then(m => m.MarketDataCapturerPassportModule)
  },
  {
    path: 'source-plans',
    loadChildren: () =>
      import('./features/source_plan/source-plan.module')
        .then(m => m.SourcePlanModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
