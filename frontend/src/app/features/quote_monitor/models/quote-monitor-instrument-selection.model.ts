export type QuoteMonitorSourcePlanStatus =
  | 'AVAILABLE'
  | 'CAPTURER_RETIRED'
  | 'NO_AVAILABLE_SOURCES';

export interface QuoteMonitorInstrumentDto {
  instrumentCode: string;
  sourcePlanStatus: QuoteMonitorSourcePlanStatus;
}

export interface QuoteMonitorInstrumentOptionDto {
  instrumentCode: string;
  selected: boolean;
}

export interface QuoteMonitorInstrumentOptionsResponseDto {
  options: QuoteMonitorInstrumentOptionDto[];
}

export interface QuoteMonitorInstrumentSelectionResponseDto {
  instruments: QuoteMonitorInstrumentDto[];
}

export interface ReplaceQuoteMonitorInstrumentSelectionDto {
  instrumentCodes: string[];
}

export interface QuoteMonitorInstrumentOption {
  instrumentCode: string;
  selected: boolean;
}

export interface QuoteMonitorSelectedInstrument {
  instrumentCode: string;
  sourcePlanStatus: QuoteMonitorSourcePlanStatus;
}
