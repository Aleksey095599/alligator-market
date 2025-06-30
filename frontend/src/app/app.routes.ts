import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'currencies',
    loadChildren: () =>
      import('./features/currencies/currency.module').then(m => m.CurrencyModule)
  },
  {
    path: 'pairs',
    loadChildren: () =>
      import('./features/currency_pairs/pair.module').then(m => m.PairModule)
  },
  {
    path: 'providers',
    loadChildren: () =>
      import('./features/providers/provider.module').then(m => m.ProviderModule)
  },
  {
    path: 'stream-configs',
    loadChildren: () =>
      import('./features/quotes/ccypair_feed_settings/settings.module').then(m => m.SettingsModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
