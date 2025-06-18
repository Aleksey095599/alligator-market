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
import {MatCardModule} from '@angular/material/card';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {StreamConfigDto} from '../../models/stream-config.model';
import {StreamConfigCreateDto} from '../../models/stream-config-create.model';
import {StreamConfigUpdateDto} from '../../models/stream-config-update.model';
import {StreamConfigService} from '../../services/stream-config.service';
import {PairDto} from '../../../pair/models/pair.model';
import {PairService} from '../../../pair/services/pair.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-stream-config-admin',
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
    MatCheckboxModule,
    RouterLink
  ],
  templateUrl: './stream-config-admin.component.html',
  styleUrl: './stream-config-admin.component.scss'
})
export class StreamConfigAdminComponent implements OnInit {

  //=================
  // Табличные данные
  //=================
  displayed: string[] = ['pair', 'provider', 'priority', 'refreshMs', 'enabled', 'actions'];
  dataSource = new MatTableDataSource<StreamConfigDto>([]);

  //==============================
  // Форма добавления конфигурации
  //==============================
  form: FormGroup;

  locked = false; // блокировка кнопки
  editing = false;
  editPair: string | null = null;
  editProvider: string | null = null;
  pairs: PairDto[] = [];

  constructor(
    private readonly service: StreamConfigService,
    private readonly pairService: PairService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      pair: ['', [Validators.required]],
      provider: ['', [Validators.required, Validators.maxLength(50)]],
      priority: [1, [Validators.required, Validators.min(0), Validators.max(32767)]],
      refreshMs: [1000, [Validators.required, Validators.min(0)]],
      enabled: [true]
    });
  }

  //=======================
  // Активность на странице
  //=======================
  ngOnInit(): void {
    this.refresh();
    this.pairService.list().subscribe({
      next: p => this.pairs = p,
      error: err => this.snack.open(err.message ?? 'Load pairs failed', 'Close')
    });
  }

  onAdd(): void {
    if (this.form.invalid || this.locked) { return; }

    this.locked = true;

    const dto: StreamConfigCreateDto = this.form.value;

    this.service.add(dto).subscribe({
      next: id => {
        this.snack.open(`Config '${id}' added`, 'OK', { duration: 2500 });
        this.refresh();
        this.form.reset({ priority: 1, refreshMs: 1000, enabled: true });
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Add failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => this.locked = false);
      }
    });
  }

  onEdit(c: StreamConfigDto): void {
    this.editing = true;
    this.editPair = c.pair;
    this.editProvider = c.provider;
    this.form.setValue({
      pair: c.pair,
      provider: c.provider,
      priority: c.priority,
      refreshMs: c.refreshMs,
      enabled: c.enabled
    });
    this.form.controls['pair'].disable();
    this.form.controls['provider'].disable();
  }

  onSave(): void {
    if (this.form.invalid || this.locked || !this.editPair || !this.editProvider) { return; }

    this.locked = true;

    const dto: StreamConfigUpdateDto = {
      priority: this.form.controls['priority'].value,
      refreshMs: this.form.controls['refreshMs'].value,
      enabled: this.form.controls['enabled'].value
    } as StreamConfigUpdateDto;

    this.service.update(this.editPair, this.editProvider, dto).subscribe({
      next: () => {
        this.snack.open(`Config '${this.editPair}:${this.editProvider}' updated`, 'OK', { duration: 2500 });
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
    this.editPair = null;
    this.editProvider = null;
    this.form.reset({ pair: '', provider: '', priority: 1, refreshMs: 1000, enabled: true });
    this.form.controls['pair'].enable();
    this.form.controls['provider'].enable();
    this.locked = false;
  }

  onDelete(pair: string, provider: string): void {
    this.service.delete(pair, provider).subscribe({
      next: () => {
        this.snack.open(`Config '${pair}:${provider}' deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err => this.snack.open(err.error?.message ?? err.message ?? 'Delete failed', 'Close')
    });
  }

  private refresh(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => this.snack.open(err.message ?? 'Load failed', 'Close')
    });
  }
}
