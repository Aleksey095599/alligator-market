-- Миграция создания таблицы инструментов FX OUTRIGHT
CREATE TABLE public.fx_outright (
    id BIGINT PRIMARY KEY,
    base_currency VARCHAR(3) NOT NULL,
    quote_currency VARCHAR(3) NOT NULL,
    value_date_code VARCHAR(4) NOT NULL,
    quote_decimal INTEGER NOT NULL,
    CONSTRAINT fk_fx_outright_instrument FOREIGN KEY (id) REFERENCES public.instrument (id),
    CONSTRAINT fk_fx_outright_base FOREIGN KEY (base_currency) REFERENCES public.currency (code),
    CONSTRAINT fk_fx_outright_quote FOREIGN KEY (quote_currency) REFERENCES public.currency (code)
);
