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
    path: '',
    loadComponent: () =>
      import('./home/home.component').then(c => c.HomeComponent)
  },
  { path: '**', redirectTo: '' }
];
