import { CommonModule } from '@angular/common';
import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router } from '@angular/router';
import { forkJoin, Observable, Subscription } from 'rxjs';

import { LiveQuoteUpdate } from '../../models/quote-monitor-live-quote.model';
import { LiveQuoteMonitorRuntimeStatus } from '../../models/quote-monitor-runtime.model';
import { QuoteMonitorSourcePlanStatus } from '../../models/quote-monitor-instrument-selection.model';
import { QuoteMonitorInstrumentSelectionService } from '../../services/quote-monitor-instrument-selection.service';
import { QuoteMonitorLiveQuoteService } from '../../services/quote-monitor-live-quote.service';
import { QuoteMonitorRuntimeService } from '../../services/quote-monitor-runtime.service';

interface HighlightSegment {
  text: string;
  match: boolean;
}

type QuoteMonitorRowStatus =
  | 'STOPPED'
  | 'NOT_AVAILABLE'
  | 'WAITING_FOR_QUOTE'
  | 'LIVE'
  | 'NOT_MONITORED';

interface QuoteMonitorInstrumentRow {
  instrumentCode: string;
  lastPrice: string | null;
  sourceCode: string | null;
  sourcePlanStatus: QuoteMonitorSourcePlanStatus | null;
  sourceTickTime: string | null;
  receivedAt: string | null;
  status: QuoteMonitorRowStatus;
  candidateAvailable: boolean;
  monitored: boolean;
}

@Component({
  selector: 'app-quote-monitor-live-quotes',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatProgressBarModule,
    MatSnackBarModule,
    MatTableModule,
    MatTooltipModule
  ],
  templateUrl: './quote-monitor-live-quotes.component.html',
  styleUrl: './quote-monitor-live-quotes.component.scss'
})
export class QuoteMonitorLiveQuotesComponent implements OnInit, OnDestroy {
  private static readonly liveQuoteMonitorCapturerCode = 'LIVE_QUOTE_MONITOR';

  readonly monitoredColumns: string[] = [
    'instrumentCode',
    'status',
    'lastPrice',
    'sourceCode',
    'sourcePlanStatus',
    'sourceTickTime',
    'receivedAt',
    'actions'
  ];

  rows: QuoteMonitorInstrumentRow[] = [];
  runtimeStatus = 'STOPPED';
  lastTickAt: string | null = null;
  liveQuoteFilterValue = '';
  loading = false;
  saving = false;
  commandRunning = false;

  private selectedInstrumentCodes = new Set<string>();
  private sourcePlanStatusesByInstrumentCode = new Map<string, QuoteMonitorSourcePlanStatus>();
  private candidateInstrumentCodeSet = new Set<string>();
  private monitoredInstrumentCodes = new Set<string>();
  private readonly liveQuotesByInstrumentCode = new Map<string, LiveQuoteUpdate>();
  private readonly subscriptions = new Subscription();
  private quoteEventSource: EventSource | null = null;

  constructor(
    private readonly instrumentSelectionService: QuoteMonitorInstrumentSelectionService,
    private readonly runtimeService: QuoteMonitorRuntimeService,
    private readonly liveQuoteService: QuoteMonitorLiveQuoteService,
    private readonly snack: MatSnackBar,
    private readonly zone: NgZone,
    private readonly router: Router
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
    return this.loading || this.saving || this.commandRunning;
  }

  get selectionLocked(): boolean {
    return this.locked || this.running;
  }

  get selectedCount(): number {
    return this.selectedInstrumentCodes.size;
  }

  get monitoredCount(): number {
    return this.monitoredInstrumentCodes.size;
  }

  reload(): void {
    this.loadInitialState();
  }

  start(): void {
    if (this.locked || this.running) {
      return;
    }

    this.commandRunning = true;
    const subscription = this.runtimeService.start().subscribe({
      next: status => {
        this.liveQuotesByInstrumentCode.clear();
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

  onLiveQuoteFilter(value: string): void {
    this.liveQuoteFilterValue = value.toUpperCase();
    this.syncRows();
  }

  clearLiveQuoteFilter(): void {
    this.liveQuoteFilterValue = '';
    this.syncRows();
  }

  openInstrumentSelection(): void {
    if (this.selectionLocked) {
      return;
    }

    void this.router.navigate(['/quote-monitor/instruments']);
  }

  removeInstrumentFromMonitor(instrumentCode: string): void {
    if (this.selectionLocked) {
      return;
    }

    if (!this.selectedInstrumentCodes.has(instrumentCode)) {
      return;
    }

    const nextSelectedInstrumentCodes = new Set(this.selectedInstrumentCodes);
    nextSelectedInstrumentCodes.delete(instrumentCode);

    this.replaceSelection(
      nextSelectedInstrumentCodes,
      `${instrumentCode} removed from monitor`
    );
  }

  viewSourcePlan(instrumentCode: string): void {
    void this.router.navigate(['/source-plans'], {
      queryParams: {
        capturerCode: QuoteMonitorLiveQuotesComponent.liveQuoteMonitorCapturerCode,
        instrumentCode
      }
    });
  }

  statusLabel(status: QuoteMonitorRowStatus): string {
    switch (status) {
      case 'STOPPED':
        return 'STOPPED';
      case 'NOT_AVAILABLE':
        return 'NOT AVAILABLE';
      case 'WAITING_FOR_QUOTE':
        return 'WAITING';
      case 'LIVE':
        return 'LIVE';
      case 'NOT_MONITORED':
        return 'NOT MONITORED';
    }
  }

  statusClass(status: QuoteMonitorRowStatus): string {
    return `row-status--${status.toLowerCase().split('_').join('-')}`;
  }

  sourcePlanStatusLabel(status: QuoteMonitorSourcePlanStatus | null): string {
    switch (status) {
      case 'AVAILABLE':
        return 'AVAILABLE';
      case 'CAPTURER_RETIRED':
        return 'CAPTURER RETIRED';
      case 'NO_AVAILABLE_SOURCES':
        return 'NO AVAILABLE SOURCES';
      case null:
        return '-';
    }
  }

  sourcePlanStatusClass(status: QuoteMonitorSourcePlanStatus | null): string {
    if (!status) {
      return 'source-plan-status--missing';
    }

    return `source-plan-status--${status.toLowerCase().split('_').join('-')}`;
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

  highlightedLiveQuoteInstrumentCodeSegments(instrumentCode: string): HighlightSegment[] {
    return this.highlightedInstrumentCodeSegments(instrumentCode, this.liveQuoteFilterValue);
  }

  private highlightedInstrumentCodeSegments(
    instrumentCode: string,
    filterValue: string
  ): HighlightSegment[] {
    const query = filterValue.trim().toUpperCase();
    if (query.length === 0) {
      return [{ text: instrumentCode, match: false }];
    }

    const segments: HighlightSegment[] = [];
    const normalizedInstrumentCode = instrumentCode.toUpperCase();
    let cursor = 0;
    let matchIndex = normalizedInstrumentCode.indexOf(query, cursor);

    while (matchIndex >= 0) {
      if (matchIndex > cursor) {
        segments.push({
          text: instrumentCode.slice(cursor, matchIndex),
          match: false
        });
      }

      const matchEnd = matchIndex + query.length;
      segments.push({
        text: instrumentCode.slice(matchIndex, matchEnd),
        match: true
      });

      cursor = matchEnd;
      matchIndex = normalizedInstrumentCode.indexOf(query, cursor);
    }

    if (cursor < instrumentCode.length) {
      segments.push({
        text: instrumentCode.slice(cursor),
        match: false
      });
    }

    return segments;
  }

  trackRowByInstrumentCode(_index: number, row: QuoteMonitorInstrumentRow): string {
    return row.instrumentCode;
  }

  private loadInitialState(): void {
    if (this.locked) {
      return;
    }

    this.loading = true;
    const subscription = forkJoin({
      runtimeStatus: this.runtimeService.status(),
      options: this.instrumentSelectionService.getOptions(),
      selectedInstruments: this.instrumentSelectionService.getSelectedInstruments(),
      liveQuotes: this.liveQuoteService.getSnapshot()
    }).subscribe({
      next: state => {
        this.candidateInstrumentCodeSet = new Set(
          state.options.map(option => option.instrumentCode)
        );
        this.selectedInstrumentCodes = new Set(
          state.selectedInstruments.map(instrument => instrument.instrumentCode)
        );
        this.sourcePlanStatusesByInstrumentCode = new Map(
          state.selectedInstruments.map(instrument => [
            instrument.instrumentCode,
            instrument.sourcePlanStatus
          ])
        );
        this.applyRuntimeStatus(state.runtimeStatus, false);
        this.applyQuoteSnapshot(state.liveQuotes, false);
        this.syncRows();
        this.refreshQuoteStream();
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.snack.open(this.resolveErrorMessage(err, 'Load quote monitor failed'), 'Close');
      }
    });

    this.subscriptions.add(subscription);
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

  private replaceSelection(nextSelectedInstrumentCodes: Set<string>, successMessage: string): void {
    this.saving = true;

    const subscription = this.instrumentSelectionService
      .replaceSelectedInstrumentCodes(Array.from(nextSelectedInstrumentCodes))
      .subscribe({
        next: () => {
          this.selectedInstrumentCodes = nextSelectedInstrumentCodes;
          this.sourcePlanStatusesByInstrumentCode = new Map(
            Array.from(this.sourcePlanStatusesByInstrumentCode)
              .filter(([instrumentCode]) => nextSelectedInstrumentCodes.has(instrumentCode))
          );
          this.syncRows();
          this.saving = false;
          this.snack.open(successMessage, 'OK', { duration: 2500 });
        },
        error: err => {
          this.saving = false;
          this.snack.open(this.resolveErrorMessage(err, 'Save quote monitor instruments failed'), 'Close');
        }
      });

    this.subscriptions.add(subscription);
  }

  private applyRuntimeStatus(status: LiveQuoteMonitorRuntimeStatus, sync = true): void {
    this.runtimeStatus = status.status;
    this.lastTickAt = status.lastTickAt;
    this.monitoredInstrumentCodes = new Set(status.monitoredInstrumentCodes);

    if (sync) {
      this.syncRows();
    }
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

  private applyQuoteSnapshot(updates: LiveQuoteUpdate[], sync = true): void {
    this.liveQuotesByInstrumentCode.clear();
    for (const update of updates) {
      this.liveQuotesByInstrumentCode.set(update.instrumentCode, update);
    }

    if (sync) {
      this.syncRows();
    }
  }

  private applyQuoteUpdate(update: LiveQuoteUpdate): void {
    this.liveQuotesByInstrumentCode.set(update.instrumentCode, update);
    this.lastTickAt = update.receivedAt;
    this.syncRows();
  }

  private syncRows(): void {
    const selectedInstrumentCodes = Array.from(this.selectedInstrumentCodes)
      .filter(instrumentCode => this.matchesLiveQuoteFilter(instrumentCode))
      .sort((left, right) => left.localeCompare(right));

    this.rows = selectedInstrumentCodes.map(instrumentCode => this.toRow(instrumentCode));
  }

  private toRow(instrumentCode: string): QuoteMonitorInstrumentRow {
    const quote = this.liveQuotesByInstrumentCode.get(instrumentCode);
    const candidateAvailable = this.candidateInstrumentCodeSet.has(instrumentCode);
    const monitored = this.monitoredInstrumentCodes.has(instrumentCode);

    return {
      instrumentCode,
      lastPrice: quote?.lastPrice ?? null,
      sourceCode: quote?.sourceCode ?? null,
      sourcePlanStatus: this.sourcePlanStatusesByInstrumentCode.get(instrumentCode) ?? null,
      sourceTickTime: quote?.sourceTickTime ?? null,
      receivedAt: quote?.receivedAt ?? null,
      status: this.resolveRowStatus(quote, candidateAvailable, monitored),
      candidateAvailable,
      monitored
    };
  }

  private resolveRowStatus(
    quote: LiveQuoteUpdate | undefined,
    candidateAvailable: boolean,
    monitored: boolean
  ): QuoteMonitorRowStatus {
    if (!this.running) {
      return candidateAvailable ? 'STOPPED' : 'NOT_AVAILABLE';
    }

    if (!monitored) {
      return 'NOT_MONITORED';
    }

    return quote ? 'LIVE' : 'WAITING_FOR_QUOTE';
  }

  private matchesLiveQuoteFilter(instrumentCode: string): boolean {
    const normalizedFilter = this.liveQuoteFilterValue.trim().toUpperCase();
    return normalizedFilter.length === 0 || instrumentCode.toUpperCase().includes(normalizedFilter);
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
