import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Subscription } from 'rxjs';

import { LiveQuoteRow, LiveQuoteUpdate } from '../../models/quote-monitor-live-quote.model';
import { LiveQuoteMonitorRuntimeStatus } from '../../models/quote-monitor-runtime.model';
import { QuoteMonitorRuntimeService } from '../../services/quote-monitor-runtime.service';

@Component({
  selector: 'app-quote-monitor-live-quotes',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressBarModule,
    MatSnackBarModule,
    MatTableModule,
    MatTooltipModule
  ],
  templateUrl: './quote-monitor-live-quotes.component.html',
  styleUrl: './quote-monitor-live-quotes.component.scss'
})
export class QuoteMonitorLiveQuotesComponent implements OnInit, OnDestroy {
  readonly displayedColumns: string[] = [
    'instrumentCode',
    'lastPrice',
    'sourceCode',
    'sourceTimestamp',
    'receivedAt',
    'status'
  ];

  rows: LiveQuoteRow[] = [];
  runtimeStatus = 'STOPPED';
  lastTickAt: string | null = null;
  loading = false;
  commandRunning = false;

  private readonly rowsByInstrumentCode = new Map<string, LiveQuoteRow>();
  private readonly subscriptions = new Subscription();

  constructor(
    private readonly runtimeService: QuoteMonitorRuntimeService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.reload();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get running(): boolean {
    return this.runtimeStatus === 'RUNNING';
  }

  get locked(): boolean {
    return this.loading || this.commandRunning;
  }

  reload(): void {
    if (this.locked) {
      return;
    }

    this.loading = true;
    const subscription = this.runtimeService.status().subscribe({
      next: status => {
        this.applyRuntimeStatus(status);
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.snack.open(this.resolveErrorMessage(err, 'Load quote monitor runtime status failed'), 'Close');
      }
    });

    this.subscriptions.add(subscription);
  }

  start(): void {
    if (this.locked || this.running) {
      return;
    }

    this.runCommand(this.runtimeService.start(), 'Quote monitor started');
  }

  stop(): void {
    if (this.locked || !this.running) {
      return;
    }

    this.runCommand(this.runtimeService.stop(), 'Quote monitor stopped');
  }

  formatDateTime(value: string | null): string {
    if (!value) {
      return '—';
    }

    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return value;
    }

    return new Intl.DateTimeFormat(undefined, {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    }).format(date);
  }

  trackByInstrumentCode(_index: number, row: LiveQuoteRow): string {
    return row.instrumentCode;
  }

  private runCommand(
    command: ReturnType<QuoteMonitorRuntimeService['start']>,
    successMessage: string
  ): void {
    this.commandRunning = true;

    const subscription = command.subscribe({
      next: status => {
        this.applyRuntimeStatus(status);
        this.commandRunning = false;
        this.snack.open(successMessage, 'OK', { duration: 2200 });
      },
      error: err => {
        this.commandRunning = false;
        this.snack.open(this.resolveErrorMessage(err, 'Quote monitor command failed'), 'Close');
      }
    });

    this.subscriptions.add(subscription);
  }

  private applyRuntimeStatus(status: LiveQuoteMonitorRuntimeStatus): void {
    this.runtimeStatus = status.status;
    this.lastTickAt = status.lastTickAt;

    for (const instrumentCode of status.monitoredInstrumentCodes) {
      const current = this.rowsByInstrumentCode.get(instrumentCode);
      this.rowsByInstrumentCode.set(instrumentCode, {
        instrumentCode,
        lastPrice: current?.lastPrice ?? null,
        sourceCode: current?.sourceCode ?? null,
        sourceTimestamp: current?.sourceTimestamp ?? null,
        receivedAt: current?.receivedAt ?? null,
        status: status.status
      });
    }

    for (const [instrumentCode, row] of this.rowsByInstrumentCode.entries()) {
      if (!status.monitoredInstrumentCodes.includes(instrumentCode)) {
        this.rowsByInstrumentCode.set(instrumentCode, {
          ...row,
          status: status.status
        });
      }
    }

    this.syncRows();
  }

  private applyQuoteUpdate(update: LiveQuoteUpdate): void {
    this.rowsByInstrumentCode.set(update.instrumentCode, {
      instrumentCode: update.instrumentCode,
      lastPrice: update.lastPrice,
      sourceCode: update.sourceCode,
      sourceTimestamp: update.sourceTimestamp,
      receivedAt: update.receivedAt,
      status: this.runtimeStatus
    });
    this.lastTickAt = update.receivedAt;
    this.syncRows();
  }

  private syncRows(): void {
    this.rows = Array.from(this.rowsByInstrumentCode.values())
      .sort((left, right) => left.instrumentCode.localeCompare(right.instrumentCode));
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
