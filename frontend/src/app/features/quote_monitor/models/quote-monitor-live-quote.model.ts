export interface QuoteMonitorLiveQuoteDto {
  instrumentCode: string;
  lastPrice: string;
  sourceCode: string;
  sourceTimestamp: string;
  receivedAt: string;
}

export interface QuoteMonitorLiveQuoteListResponseDto {
  quotes: QuoteMonitorLiveQuoteDto[];
}

export interface LiveQuoteUpdate {
  instrumentCode: string;
  lastPrice: string;
  sourceCode: string | null;
  sourceTimestamp: string | null;
  receivedAt: string;
}

export interface LiveQuoteRow {
  instrumentCode: string;
  lastPrice: string | null;
  sourceCode: string | null;
  sourceTimestamp: string | null;
  receivedAt: string | null;
  status: string;
}
