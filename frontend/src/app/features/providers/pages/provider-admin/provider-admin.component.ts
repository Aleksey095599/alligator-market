import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatCardModule} from "@angular/material/card";
import {ProviderDto} from '../../models/provider.model';
import {ProviderCreateDto} from '../../models/provider-create.model';
import {ProviderUpdateDto} from '../../models/provider-update.model';
import {ProviderService} from '../../services/provider.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-provider-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatCardModule,
    RouterLink
  ],
  templateUrl: './provider-admin.component.html',
  styleUrl: './provider-admin.component.scss'
})
export class ProviderAdminComponent implements OnInit {

  //=================
  // Табличные данные
  //=================
  displayed: string[] = ['name', 'baseUrl', 'mode', 'apiKey', 'actions'];
  dataSource  = new MatTableDataSource<ProviderDto>([]);

  //============================
  // Форма добавления провайдера
  //============================
  form: FormGroup;

  locked = false; // флаг блокировки кнопки Add
  editing = false; // режим редактирования
  editName: string | null = null; // имя провайдера для редактирования
  editMode: string | null = null; // текущий режим провайдера для редактирования

  constructor(
    private readonly service: ProviderService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      name: [
        '',
        [Validators.required, Validators.maxLength(50)]
      ],

      baseUrl: [
        '',
        [Validators.required, Validators.maxLength(255)]
      ],

      mode: [
        'PULL',
        [Validators.required]
      ],

      apiKey: [
        '',
        [Validators.required, Validators.maxLength(255)]
      ]
    });
  }

  //=======================
  // Активность на странице
  //=======================

  ngOnInit(): void {
    this.refresh();
  }

  onAdd(): void {
    if (this.form.invalid || this.locked) { return; }

    this.locked = true;

    const dto: ProviderCreateDto = this.form.value;

    this.service.add(dto).subscribe({
      next: name => {
        this.snack.open(
          `Provider '${name}' with mode '${dto.mode}' added`, 'OK', { duration: 2500 }
        );
        this.refresh();
        this.form.reset({ mode: 'PULL' });
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Add failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => this.locked = false);
      }
    });
  }

  onEdit(p: ProviderDto): void {
    this.editing = true;
    this.editName = p.name;
    this.editMode = p.mode;
    this.form.setValue({
      name: p.name,
      baseUrl: p.baseUrl,
      mode: p.mode,
      apiKey: p.apiKey
    });
    this.form.controls['name'].disable();
  }

  onSave(): void {
    if (this.form.invalid || this.locked || !this.editName || !this.editMode) { return; }

    this.locked = true;

    const dto: ProviderUpdateDto = {
      baseUrl: this.form.controls['baseUrl'].value,
      mode: this.form.controls['mode'].value,
      apiKey: this.form.controls['apiKey'].value
    } as ProviderUpdateDto;

    this.service.update(this.editName, this.editMode, dto).subscribe({
      next: () => {
        this.snack.open(`Provider '${this.editName}' updated to mode '${dto.mode}'`, 'OK', { duration: 2500 });
        this.refresh();
        this.cancelEdit();
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Update failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => this.locked = false);
      }
    });
  }

  cancelEdit(): void {
    this.editing = false;
    this.editName = null;
    this.editMode = null;
    this.form.reset({ name: '', baseUrl: '', mode: 'PULL', apiKey: '' });
    this.form.controls['name'].enable();
    this.locked = false;
  }

  onDelete(name: string, mode: string): void {
    this.service.delete(name, mode).subscribe({
      next: () => {
        this.snack.open(`Provider '${name}' with mode '${mode}' deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err =>
        this.snack.open(err.error?.message ?? err.message ?? 'Delete failed', 'Close')
    });
  }

  private refresh(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => this.snack.open(err.message ?? 'Load failed', 'Close')
    });
  }
}
