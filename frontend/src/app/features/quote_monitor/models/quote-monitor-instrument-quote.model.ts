export interface QuoteMonitorInstrumentQuoteDto {
  instrumentCode: string;
  lastPrice: string;
  sourceCode: string;
  sourceTickTime: string;
  receivedAt: string;
}

export interface QuoteMonitorInstrumentQuoteListResponseDto {
  quotes: QuoteMonitorInstrumentQuoteDto[];
}

export interface QuoteMonitorInstrumentQuote {
  instrumentCode: string;
  lastPrice: string;
  sourceCode: string | null;
  sourceTickTime: string | null;
  receivedAt: string;
}

export interface InstrumentQuoteRow {
  instrumentCode: string;
  lastPrice: string | null;
  sourceCode: string | null;
  sourceTickTime: string | null;
  receivedAt: string | null;
  status: string;
}
