export type QuoteMonitorInstrumentRuntimeStatus =
  | 'STOPPED'
  | 'WAITING_FOR_QUOTE'
  | 'LIVE'
  | 'RUNTIME_INSTRUMENT_NOT_FOUND'
  | 'RUNTIME_SOURCE_PLAN_NOT_FOUND'
  | 'RUNTIME_SOURCE_NOT_FOUND'
  | 'HANDLER_NOT_FOUND'
  | 'INSTRUMENT_NOT_SUPPORTED_BY_HANDLER'
  | 'STREAM_START_FAILED'
  | 'STREAM_FAILED'
  | 'UNSUPPORTED_SOURCE_TICK_TYPE';

export interface QuoteMonitorInstrumentRuntimeStateDto {
  instrumentCode: string;
  sourceCode: string | null;
  status: QuoteMonitorInstrumentRuntimeStatus;
  detail: string | null;
}

export interface QuoteMonitorRuntimeStatusResponseDto {
  status: string;
  monitoredInstrumentCodes: string[];
  lastTickAt: string | null;
  instrumentStates: QuoteMonitorInstrumentRuntimeStateDto[];
}

export interface QuoteMonitorInstrumentRuntimeState {
  instrumentCode: string;
  sourceCode: string | null;
  status: QuoteMonitorInstrumentRuntimeStatus;
  detail: string | null;
}

export interface QuoteMonitorRuntimeStatus {
  status: string;
  monitoredInstrumentCodes: string[];
  lastTickAt: string | null;
  instrumentStates: QuoteMonitorInstrumentRuntimeState[];
}
