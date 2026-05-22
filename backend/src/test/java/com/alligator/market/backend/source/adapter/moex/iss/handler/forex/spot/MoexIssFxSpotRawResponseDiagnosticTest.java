package com.alligator.market.backend.source.adapter.moex.iss.handler.forex.spot;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Manual diagnostic test for inspecting the raw MOEX ISS FX spot payload.
 * Excluded from the default Maven test run by the dev tag.
 */
@Tag("dev")
@Tag("external")
class MoexIssFxSpotRawResponseDiagnosticTest {
    private static final JsonMapper JSON = JsonMapper.builder().build();

    private static final URI CNY_RUB_TOM_MARKETDATA_URI = URI.create(
            "https://iss.moex.com/iss/engines/currency/markets/selt/boards/CETS/securities/CNYRUB_TOM.json" +
                    "?iss.meta=on"
    );

    @Test
    void printRawCnyRubTomResponse() throws Exception {
        try (HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build()) {

            HttpRequest request = HttpRequest.newBuilder(CNY_RUB_TOM_MARKETDATA_URI)
                    .timeout(Duration.ofSeconds(30))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Alligator Market Diagnostic")
                    .GET()
                    .build();

            Instant requestStartedAt = Instant.now();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            Instant responseReceivedAt = Instant.now();

            String body = response.body();
            JsonNode root = JSON.readTree(body);

            printRequestSummary(response, requestStartedAt, responseReceivedAt);
            printMarketdataFirstRow(root);
            printFullJson(root);
        }
    }

    private static void printRequestSummary(
            HttpResponse<String> response,
            Instant requestStartedAt,
            Instant responseReceivedAt
    ) {
        System.out.println();
        System.out.println("========== MOEX ISS RAW RESPONSE DIAGNOSTIC ==========");
        System.out.println("Request URI: " + CNY_RUB_TOM_MARKETDATA_URI);
        System.out.println("Request started at UTC: " + requestStartedAt);
        System.out.println("Response received at UTC: " + responseReceivedAt);
        System.out.println("Elapsed: " + Duration.between(requestStartedAt, responseReceivedAt).toMillis() + " ms");
        System.out.println("HTTP status: " + response.statusCode());
        System.out.println("HTTP Date header: " + firstHeaderValue(response, "Date"));
        System.out.println("HTTP Content-Type header: " + firstHeaderValue(response, "Content-Type"));
    }

    private static void printMarketdataFirstRow(JsonNode root) {
        JsonNode marketdata = root.path("marketdata");
        JsonNode columnsNode = marketdata.path("columns");
        JsonNode dataNode = marketdata.path("data");

        System.out.println();
        System.out.println("========== MARKETDATA FIRST ROW ==========");

        if (!columnsNode.isArray()) {
            System.out.println("marketdata.columns is missing or is not an array");
            return;
        }
        if (!dataNode.isArray() || dataNode.isEmpty()) {
            System.out.println("marketdata.data is missing, is not an array, or has no rows");
            return;
        }

        ArrayNode columns = (ArrayNode) columnsNode;
        JsonNode row = dataNode.get(0);
        if (!row.isArray()) {
            System.out.println("marketdata.data[0] is not an array");
            return;
        }

        for (int i = 0; i < columns.size(); i++) {
            JsonNode columnNode = columns.get(i);
            String columnName = columnNode.isString() ? columnNode.stringValue() : columnNode.toString();
            JsonNode value = row.get(i);
            System.out.printf("%-24s = %s%n", columnName, formatCell(value));
        }

        System.out.println();
        System.out.println("Important candidate time fields:");
        printCandidateField(columns, row, "SYSTIME");
        printCandidateField(columns, row, "TIME");
        printCandidateField(columns, row, "UPDATETIME");
        printCandidateField(columns, row, "TRADETIME");
        printCandidateField(columns, row, "LASTCHANGE");
    }

    private static void printFullJson(JsonNode root) {
        System.out.println();
        System.out.println("========== FULL JSON ==========");
        System.out.println(JSON.writerWithDefaultPrettyPrinter().writeValueAsString(root));
    }

    private static void printCandidateField(ArrayNode columns, JsonNode row, String columnName) {
        int index = indexOfColumn(columns, columnName);
        if (index < 0) {
            System.out.printf("%-24s = <absent>%n", columnName);
            return;
        }

        System.out.printf("%-24s = %s%n", columnName, formatCell(row.get(index)));
    }

    private static int indexOfColumn(ArrayNode columns, String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            JsonNode columnNode = columns.get(i);
            if (columnNode.isString() && columnName.equalsIgnoreCase(columnNode.stringValue())) {
                return i;
            }
        }
        return -1;
    }

    private static String firstHeaderValue(HttpResponse<?> response, String headerName) {
        List<String> values = response.headers().allValues(headerName);
        if (values.isEmpty()) {
            return "<absent>";
        }
        return values.getFirst();
    }

    private static String formatCell(JsonNode value) {
        if (value == null || value.isNull()) {
            return "null";
        }
        if (value.isString()) {
            return "\"" + value.stringValue() + "\"";
        }
        return value.toString();
    }
}
