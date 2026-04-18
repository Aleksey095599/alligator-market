import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/instrument-source-plan-admin/instrument-source-plan-admin.component')
        .then(c => c.InstrumentSourcePlanAdminComponent)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InstrumentSourcePlanRoutingModule {}
