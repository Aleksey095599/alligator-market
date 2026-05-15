import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxChange, MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';

import { QuoteMonitorInstrumentOption } from '../../models/quote-monitor-instrument-selection.model';
import { QuoteMonitorInstrumentSelectionService } from '../../services/quote-monitor-instrument-selection.service';

@Component({
  selector: 'app-quote-monitor-instrument-selection',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSnackBarModule,
    MatTableModule,
    MatTooltipModule
  ],
  templateUrl: './quote-monitor-instrument-selection.component.html',
  styleUrl: './quote-monitor-instrument-selection.component.scss'
})
export class QuoteMonitorInstrumentSelectionComponent implements OnInit {
  displayedColumns: string[] = ['selected', 'instrumentCode', 'state'];
  options: QuoteMonitorInstrumentOption[] = [];
  selectedInstrumentCodes = new Set<string>();
  private savedInstrumentCodes = new Set<string>();

  filterValue = '';
  loading = false;
  saving = false;

  constructor(
    private readonly service: QuoteMonitorInstrumentSelectionService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.reload();
  }

  get filteredOptions(): QuoteMonitorInstrumentOption[] {
    const normalizedFilter = this.filterValue.trim().toUpperCase();

    if (normalizedFilter.length === 0) {
      return this.options;
    }

    return this.options.filter(option =>
      option.instrumentCode.includes(normalizedFilter)
    );
  }

  get selectedCount(): number {
    return this.selectedInstrumentCodes.size;
  }

  get candidateCount(): number {
    return this.options.length;
  }

  get hasChanges(): boolean {
    return !this.sameSet(this.selectedInstrumentCodes, this.savedInstrumentCodes);
  }

  get allVisibleSelected(): boolean {
    const visible = this.filteredOptions;

    return visible.length > 0
      && visible.every(option => this.selectedInstrumentCodes.has(option.instrumentCode));
  }

  get someVisibleSelected(): boolean {
    return this.filteredOptions.some(option =>
      this.selectedInstrumentCodes.has(option.instrumentCode)
    );
  }

  get locked(): boolean {
    return this.loading || this.saving;
  }

  reload(): void {
    if (this.locked) {
      return;
    }

    this.loading = true;

    this.service.getOptions().subscribe({
      next: options => {
        this.options = options;
        this.selectedInstrumentCodes = new Set(
          options
            .filter(option => option.selected)
            .map(option => option.instrumentCode)
        );
        this.savedInstrumentCodes = new Set(this.selectedInstrumentCodes);
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.snack.open(this.resolveErrorMessage(err, 'Load quote monitor instruments failed'), 'Close');
      }
    });
  }

  save(): void {
    if (!this.hasChanges || this.locked) {
      return;
    }

    this.saving = true;

    this.service
      .replaceSelectedInstrumentCodes(Array.from(this.selectedInstrumentCodes))
      .subscribe({
        next: () => {
          this.savedInstrumentCodes = new Set(this.selectedInstrumentCodes);
          this.options = this.options.map(option => ({
            ...option,
            selected: this.selectedInstrumentCodes.has(option.instrumentCode)
          }));
          this.saving = false;
          this.snack.open('Quote monitor instruments saved', 'OK', { duration: 2500 });
        },
        error: err => {
          this.saving = false;
          this.snack.open(this.resolveErrorMessage(err, 'Save failed'), 'Close');
        }
      });
  }

  onFilter(value: string): void {
    this.filterValue = value.toUpperCase();
  }

  clearFilter(): void {
    this.filterValue = '';
  }

  onToggle(instrumentCode: string, selected: boolean): void {
    if (selected) {
      this.selectedInstrumentCodes.add(instrumentCode);
      return;
    }

    this.selectedInstrumentCodes.delete(instrumentCode);
  }

  onToggleVisible(change: MatCheckboxChange): void {
    const checked = change.checked;

    for (const option of this.filteredOptions) {
      this.onToggle(option.instrumentCode, checked);
    }
  }

  isSelected(instrumentCode: string): boolean {
    return this.selectedInstrumentCodes.has(instrumentCode);
  }

  isSaved(instrumentCode: string): boolean {
    return this.savedInstrumentCodes.has(instrumentCode);
  }

  rowStateLabel(instrumentCode: string): string {
    const selected = this.isSelected(instrumentCode);
    const saved = this.isSaved(instrumentCode);

    if (selected && saved) {
      return 'Selected';
    }

    if (selected) {
      return 'New';
    }

    if (saved) {
      return 'Removed';
    }

    return 'Available';
  }

  private sameSet(left: Set<string>, right: Set<string>): boolean {
    if (left.size !== right.size) {
      return false;
    }

    for (const value of left) {
      if (!right.has(value)) {
        return false;
      }
    }

    return true;
  }

  private resolveErrorMessage(err: unknown, fallback: string): string {
    if (!err || typeof err !== 'object') {
      return fallback;
    }

    const body = (err as { error?: unknown }).error;
    if (body && typeof body === 'object') {
      const detail = (body as { detail?: unknown }).detail;
      if (typeof detail === 'string' && detail.trim().length > 0) {
        return detail;
      }

      const title = (body as { title?: unknown }).title;
      if (typeof title === 'string' && title.trim().length > 0) {
        return title;
      }
    }

    const message = (err as { message?: unknown }).message;
    if (typeof message === 'string' && message.trim().length > 0) {
      return message;
    }

    return fallback;
  }
}
