export interface LiveQuoteMonitorRuntimeStatusResponseDto {
  status: string;
  monitoredInstrumentCodes: string[];
  lastTickAt: string | null;
}

export interface LiveQuoteMonitorRuntimeStatus {
  status: string;
  monitoredInstrumentCodes: string[];
  lastTickAt: string | null;
}
