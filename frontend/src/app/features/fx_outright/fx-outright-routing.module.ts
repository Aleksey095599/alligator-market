import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/fx-outright-list/fx-outright-list.component')
        .then(c => c.FxOutrightListComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
/* Маршрутизация для списка FX OUTRIGHT. */
export class FxOutrightRoutingModule { }
