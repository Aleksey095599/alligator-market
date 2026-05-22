import { CommonModule } from '@angular/common';
import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Observable, Subscription } from 'rxjs';

import { LiveQuoteRow, LiveQuoteUpdate } from '../../models/quote-monitor-live-quote.model';
import { LiveQuoteMonitorRuntimeStatus } from '../../models/quote-monitor-runtime.model';
import { QuoteMonitorLiveQuoteService } from '../../services/quote-monitor-live-quote.service';
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
    'sourceTickTime',
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
  private quoteEventSource: EventSource | null = null;

  constructor(
    private readonly runtimeService: QuoteMonitorRuntimeService,
    private readonly liveQuoteService: QuoteMonitorLiveQuoteService,
    private readonly snack: MatSnackBar,
    private readonly zone: NgZone
  ) {}

  ngOnInit(): void {
    this.loadInitialState();
  }

  ngOnDestroy(): void {
    this.stopQuoteStream();
    this.subscriptions.unsubscribe();
  }

  get running(): boolean {
    return this.runtimeStatus === 'RUNNING';
  }

  get locked(): boolean {
    return this.loading || this.commandRunning;
  }

  private loadInitialState(): void {
    if (this.locked) {
      return;
    }

    this.loading = true;
    const subscription = this.runtimeService.status().subscribe({
      next: status => {
        this.applyRuntimeStatus(status);
        this.loadSnapshot();
        this.refreshQuoteStream();
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

    this.commandRunning = true;
    const subscription = this.runtimeService.start().subscribe({
      next: status => {
        this.rowsByInstrumentCode.clear();
        this.applyRuntimeStatus(status);
        this.loadSnapshot();
        this.refreshQuoteStream();
        this.commandRunning = false;
        this.snack.open('Quote monitor started', 'OK', { duration: 2200 });
      },
      error: err => {
        this.commandRunning = false;
        this.snack.open(this.resolveErrorMessage(err, 'Quote monitor command failed'), 'Close');
      }
    });

    this.subscriptions.add(subscription);
  }

  stop(): void {
    if (this.locked || !this.running) {
      return;
    }

    this.runCommand(this.runtimeService.stop(), 'Quote monitor stopped');
  }

  formatDateTime(value: string | null): string {
    if (!value) {
      return '-';
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
    command: Observable<LiveQuoteMonitorRuntimeStatus>,
    successMessage: string
  ): void {
    this.commandRunning = true;

    const subscription = command.subscribe({
      next: status => {
        this.applyRuntimeStatus(status);
        this.loadSnapshot();
        this.refreshQuoteStream();
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
        sourceTickTime: current?.sourceTickTime ?? null,
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

  private loadSnapshot(): void {
    const subscription = this.liveQuoteService.getSnapshot().subscribe({
      next: updates => this.applyQuoteSnapshot(updates),
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load live quote snapshot failed'), 'Close');
      }
    });

    this.subscriptions.add(subscription);
  }

  private refreshQuoteStream(): void {
    if (this.running) {
      this.startQuoteStream();
      return;
    }

    this.stopQuoteStream();
  }

  private startQuoteStream(): void {
    if (this.quoteEventSource) {
      return;
    }

    const eventSource = this.liveQuoteService.openStream();
    this.quoteEventSource = eventSource;

    eventSource.addEventListener('live-quote', event => {
      this.zone.run(() => {
        try {
          this.applyQuoteUpdate(this.liveQuoteService.parseStreamMessage((event as MessageEvent<string>).data));
        } catch {
          this.snack.open('Live quote stream message is invalid', 'Close');
        }
      });
    });

    eventSource.onerror = () => {
      if (!this.running) {
        this.stopQuoteStream();
      }
    };
  }

  private stopQuoteStream(): void {
    this.quoteEventSource?.close();
    this.quoteEventSource = null;
  }

  private applyQuoteSnapshot(updates: LiveQuoteUpdate[]): void {
    const updatedInstrumentCodes = new Set(updates.map(update => update.instrumentCode));

    for (const [instrumentCode, row] of this.rowsByInstrumentCode.entries()) {
      if (!updatedInstrumentCodes.has(instrumentCode)) {
        this.rowsByInstrumentCode.set(instrumentCode, {
          ...row,
          lastPrice: null,
          sourceCode: null,
          sourceTickTime: null,
          receivedAt: null
        });
      }
    }

    for (const update of updates) {
      this.applyQuoteUpdate(update, false);
    }

    this.syncRows();
  }

  private applyQuoteUpdate(update: LiveQuoteUpdate, sync = true): void {
    this.rowsByInstrumentCode.set(update.instrumentCode, {
      instrumentCode: update.instrumentCode,
      lastPrice: update.lastPrice,
      sourceCode: update.sourceCode,
      sourceTickTime: update.sourceTickTime,
      receivedAt: update.receivedAt,
      status: this.runtimeStatus
    });
    this.lastTickAt = update.receivedAt;
    if (sync) {
      this.syncRows();
    }
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
