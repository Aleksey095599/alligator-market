import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/source-handler-passport-list/source-handler-passport-list.component')
        .then(c => c.SourceHandlerPassportListComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SourceHandlerPassportRoutingModule {}
