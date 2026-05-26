export interface QuoteMonitorRuntimeStatusResponseDto {
  status: string;
  monitoredInstrumentCodes: string[];
  lastTickAt: string | null;
}

export interface QuoteMonitorRuntimeStatus {
  status: string;
  monitoredInstrumentCodes: string[];
  lastTickAt: string | null;
}
