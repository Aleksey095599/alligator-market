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
    path: 'provider-passports',
    loadChildren: () =>
      import('./features/provider_passport/provider-passport.module')
        .then(m => m.ProviderPassportModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
