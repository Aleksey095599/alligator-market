import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'currencies',
    loadChildren: () =>
      import('./features/currency/currency.module').then(m => m.CurrencyModule)
  },
  {
    path: 'pairs',
    loadChildren: () =>
      import('./features/currency_pair/pair.module').then(m => m.PairModule)
  },
  {
    path: 'fx-spots',
    loadChildren: () =>
      import('./features/fx_spot/fx-spot.module').then(m => m.FxSpotModule)
  },
  {
    path: 'providers',
    loadChildren: () =>
      import('./features/provider_profile/provider-profile.module')
        .then(m => m.ProviderProfileModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
