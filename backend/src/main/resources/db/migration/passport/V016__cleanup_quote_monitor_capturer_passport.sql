DELETE FROM capturer_passport
WHERE capturer_code = 'LIVE_QUOTE_MONITOR';

UPDATE capturer_passport
SET description = 'Shows live quotes for selected instruments'
WHERE capturer_code = 'QUOTE_MONITOR';
