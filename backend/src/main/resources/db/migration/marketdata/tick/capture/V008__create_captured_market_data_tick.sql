-- captured_market_data_tick: captured-level market data ticks
CREATE TABLE captured_market_data_tick
(
    id                      BIGSERIAL PRIMARY KEY,

    collection_process_code VARCHAR(128) NOT NULL,
    instrument_code         VARCHAR(128) NOT NULL,
    source_code             VARCHAR(50)  NOT NULL,

    source_tick_type        VARCHAR(32)  NOT NULL,
    source_instrument_code  VARCHAR(128) NOT NULL,
    source_timestamp        TIMESTAMPTZ  NOT NULL,

    received_timestamp      TIMESTAMPTZ  NOT NULL,
    stored_timestamp        TIMESTAMPTZ  NOT NULL DEFAULT now(),

    last_price              NUMERIC(38, 18),
    bid_price               NUMERIC(38, 18),
    ask_price               NUMERIC(38, 18),

    CONSTRAINT chk_captured_market_data_tick_type
        CHECK (
            (
                source_tick_type = 'LAST_PRICE'
                    AND last_price IS NOT NULL
                    AND bid_price IS NULL
                    AND ask_price IS NULL
            )
                OR
            (
                source_tick_type = 'TOP_OF_BOOK_QUOTE'
                    AND last_price IS NULL
                    AND bid_price IS NOT NULL
                    AND ask_price IS NOT NULL
            )
        ),

    CONSTRAINT chk_captured_market_data_tick_positive_prices
        CHECK (
            (last_price IS NULL OR last_price > 0)
                AND (bid_price IS NULL OR bid_price > 0)
                AND (ask_price IS NULL OR ask_price > 0)
        ),

    CONSTRAINT chk_captured_market_data_tick_bid_ask
        CHECK (
            bid_price IS NULL
                OR ask_price IS NULL
                OR bid_price <= ask_price
        )
);
