import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'currencies',
    loadChildren: () =>
      import('./fx/currency/currency.module').then(m => m.CurrencyModule)
  },
  {
    path: 'pairs',
    loadChildren: () =>
      import('./fx/pair/pair.module').then(m => m.PairModule)
  },
  {
    path: 'stream-configs',
    loadChildren: () =>
      import('./fx/stream-config/stream-config.module').then(m => m.StreamConfigModule)
  },
  {
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
