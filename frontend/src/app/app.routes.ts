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
      import('./features/pairs/pair.module').then(m => m.PairModule)
  },
  {
    path: 'stream-configs',
    loadChildren: () =>
      import('./features/quotes/stream/settings/settings.module').then(m => m.SettingsModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
