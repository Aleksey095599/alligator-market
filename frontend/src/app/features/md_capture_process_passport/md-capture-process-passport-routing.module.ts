import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/md-capture-process-passport-list/md-capture-process-passport-list.component')
        .then(c => c.MDCaptureProcessPassportListComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MDCaptureProcessPassportRoutingModule {}
