import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'currencies',
    loadChildren: () =>
      import('./currency/currency.module').then(m => m.CurrencyModule)
  },
  { path: '', redirectTo: 'currencies', pathMatch: 'full' },
  { path: '**', redirectTo: 'currencies' }
];
