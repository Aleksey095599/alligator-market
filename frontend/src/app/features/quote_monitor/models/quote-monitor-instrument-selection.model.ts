export interface QuoteMonitorInstrumentDto {
  instrumentCode: string;
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
