import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
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
  readonly readonlyColumns: string[] = ['instrumentCode'];
  readonly editColumns: string[] = ['pick', 'instrumentCode'];

  monitorColumns = this.readonlyColumns;
  candidateColumns = this.readonlyColumns;

  private options: QuoteMonitorInstrumentOption[] = [];
  private selectedInstrumentCodes = new Set<string>();

  monitorInstrumentCodes: string[] = [];
  candidateInstrumentCodes: string[] = [];
  pickedMonitorInstrumentCodes = new Set<string>();
  pickedCandidateInstrumentCodes = new Set<string>();

  filterValue = '';
  loading = false;
  saving = false;
  monitorEditing = false;
  candidateEditing = false;

  constructor(
    private readonly service: QuoteMonitorInstrumentSelectionService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.reload();
  }

  get monitorCount(): number {
    return this.selectedInstrumentCodes.size;
  }

  get candidateCount(): number {
    return this.options.length - this.selectedInstrumentCodes.size;
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
        this.resetEditState();
        this.syncLists();
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.snack.open(this.resolveErrorMessage(err, 'Load quote monitor instruments failed'), 'Close');
      }
    });
  }

  onFilter(value: string): void {
    this.filterValue = value.toUpperCase();
    this.syncLists();
  }

  clearFilter(): void {
    this.filterValue = '';
    this.syncLists();
  }

  editMonitor(): void {
    if (this.locked) {
      return;
    }

    this.cancelCandidateEdit();
    this.monitorEditing = true;
    this.monitorColumns = this.editColumns;
    this.pickedMonitorInstrumentCodes.clear();
  }

  editCandidates(): void {
    if (this.locked) {
      return;
    }

    this.cancelMonitorEdit();
    this.candidateEditing = true;
    this.candidateColumns = this.editColumns;
    this.pickedCandidateInstrumentCodes.clear();
  }

  cancelMonitorEdit(): void {
    this.monitorEditing = false;
    this.monitorColumns = this.readonlyColumns;
    this.pickedMonitorInstrumentCodes.clear();
  }

  cancelCandidateEdit(): void {
    this.candidateEditing = false;
    this.candidateColumns = this.readonlyColumns;
    this.pickedCandidateInstrumentCodes.clear();
  }

  toggleMonitorPick(instrumentCode: string, checked: boolean): void {
    this.togglePickedCode(this.pickedMonitorInstrumentCodes, instrumentCode, checked);
  }

  toggleCandidatePick(instrumentCode: string, checked: boolean): void {
    this.togglePickedCode(this.pickedCandidateInstrumentCodes, instrumentCode, checked);
  }

  removePickedFromMonitor(): void {
    if (this.locked || this.pickedMonitorInstrumentCodes.size === 0) {
      return;
    }

    const nextSelectedInstrumentCodes = new Set(this.selectedInstrumentCodes);
    for (const instrumentCode of this.pickedMonitorInstrumentCodes) {
      nextSelectedInstrumentCodes.delete(instrumentCode);
    }

    this.replaceSelection(
      nextSelectedInstrumentCodes,
      `${this.pickedMonitorInstrumentCodes.size} instrument(s) removed from monitor`
    );
  }

  addPickedToMonitor(): void {
    if (this.locked || this.pickedCandidateInstrumentCodes.size === 0) {
      return;
    }

    const nextSelectedInstrumentCodes = new Set(this.selectedInstrumentCodes);
    for (const instrumentCode of this.pickedCandidateInstrumentCodes) {
      nextSelectedInstrumentCodes.add(instrumentCode);
    }

    this.replaceSelection(
      nextSelectedInstrumentCodes,
      `${this.pickedCandidateInstrumentCodes.size} instrument(s) added to monitor`
    );
  }

  isPickedForMonitorRemoval(instrumentCode: string): boolean {
    return this.pickedMonitorInstrumentCodes.has(instrumentCode);
  }

  isPickedForMonitorAddition(instrumentCode: string): boolean {
    return this.pickedCandidateInstrumentCodes.has(instrumentCode);
  }

  private replaceSelection(nextSelectedInstrumentCodes: Set<string>, successMessage: string): void {
    this.saving = true;

    this.service
      .replaceSelectedInstrumentCodes(Array.from(nextSelectedInstrumentCodes))
      .subscribe({
        next: () => {
          this.selectedInstrumentCodes = nextSelectedInstrumentCodes;
          this.resetEditState();
          this.syncLists();
          this.saving = false;
          this.snack.open(successMessage, 'OK', { duration: 2500 });
        },
        error: err => {
          this.saving = false;
          this.snack.open(this.resolveErrorMessage(err, 'Save failed'), 'Close');
        }
      });
  }

  private resetEditState(): void {
    this.cancelMonitorEdit();
    this.cancelCandidateEdit();
  }

  private syncLists(): void {
    const normalizedFilter = this.filterValue.trim().toUpperCase();
    const instrumentCodes = this.options
      .map(option => option.instrumentCode)
      .filter(instrumentCode =>
        normalizedFilter.length === 0 || instrumentCode.includes(normalizedFilter)
      );

    this.monitorInstrumentCodes = instrumentCodes
      .filter(instrumentCode => this.selectedInstrumentCodes.has(instrumentCode));
    this.candidateInstrumentCodes = instrumentCodes
      .filter(instrumentCode => !this.selectedInstrumentCodes.has(instrumentCode));
  }

  private togglePickedCode(
    pickedInstrumentCodes: Set<string>,
    instrumentCode: string,
    checked: boolean
  ): void {
    if (checked) {
      pickedInstrumentCodes.add(instrumentCode);
      return;
    }

    pickedInstrumentCodes.delete(instrumentCode);
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
