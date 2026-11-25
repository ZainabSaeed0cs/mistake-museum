package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.DiagnosisResult;
import com.mistakemuseum.mistake_museum.model.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Profile("!stub")
public class GeminiDiagnosisService implements DiagnosisService {

    private static final Logger log = LoggerFactory.getLogger(GeminiDiagnosisService.class);
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final int MAX_ATTEMPTS = 3;

    private final RestClient client;

    public GeminiDiagnosisService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(40));
        this.client = RestClient.builder().requestFactory(factory).build();
    }

    @Override
    public DiagnosisResult diagnose(String subject, String topic, String question,
                                    String studentAnswer, String correction,
                                    MultipartFile photo) {

        String key = System.getenv("GEMINI_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "The AI service is not set up yet. Please try again later.");
        }
        String model = System.getenv("GEMINI_MODEL");
        if (model == null || model.isBlank()) {
            model = "gemini-flash-latest";
        }

        String prompt = """
                You are a careful %s teacher. A student got a question wrong.
                Classify the PRIMARY cause of the mistake as exactly one of:
                CARELESS_SLIP (knew it, small slip), CONCEPT_GAP (does not understand the idea),
                FORMULA_MISUSE (wrong or misapplied formula), INCOMPLETE_ANSWER (stopped early or missed parts),
                MISREAD_QUESTION (misread or ignored information, units or what was asked).

                Question: %s
                Student's answer: %s
                Correct answer: %s
                Topic hint: %s

                Reply with JSON only, with these keys:
                "label" (one of the five labels above),
                "reason" (one or two simple sentences addressed to the student),
                "errorStep" (the step or line where it went wrong),
                "topic" (short topic name),
                "confidence" (a number from 0 to 1).
                """.formatted(nz(subject), nz(question), nz(studentAnswer), nz(correction), nz(topic));

        List<Object> parts = new ArrayList<>();
        parts.add(Map.of("text", prompt));

        if (photo != null && !photo.isEmpty()) {
            String type = photo.getContentType();
            if (type == null || !(type.equals("image/jpeg") || type.equals("image/png"))) {
                throw new IllegalArgumentException("Please upload a JPG or PNG photo.");
            }
            try {
                String data = Base64.getEncoder().encodeToString(photo.getBytes());
                parts.add(Map.of("inlineData", Map.of("mimeType", type, "data", data)));
            } catch (IOException e) {
                throw new IllegalStateException("We could not read that photo. Please try again.");
            }
        }

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", parts)),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "temperature", 0.2));

        String url = BASE_URL + model + ":generateContent";

        try {
            Map<?, ?> response = callWithRetry(url, key, body);

            String text = extractText(response);
            Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(text);

            Label label = Label.valueOf(String.valueOf(json.get("label")).trim().toUpperCase());
            String reason = String.valueOf(json.getOrDefault("reason", ""));
            String errorStep = String.valueOf(json.getOrDefault("errorStep", ""));
            String outTopic = json.get("topic") == null ? nz(topic) : String.valueOf(json.get("topic"));
            double confidence = json.get("confidence") instanceof Number n ? n.doubleValue() : 0.5;
            confidence = Math.max(0.0, Math.min(1.0, confidence));

            return new DiagnosisResult(label, reason, outTopic, confidence, errorStep);

        } catch (RestClientResponseException e) {
            int status = e.getStatusCode().value();
            log.error("Gemini call failed with HTTP {}: {}", status, e.getResponseBodyAsString());
            if (status == 400 || status == 401 || status == 403) {
                throw new IllegalStateException("The AI service key is missing or not accepted.");
            } else if (status == 404) {
                throw new IllegalStateException("The AI model name is wrong. Set GEMINI_MODEL.");
            } else if (status == 429 || status == 503) {
                throw new IllegalStateException("The AI is busy right now. Please try again in a minute.");
            }
            throw new IllegalStateException("The AI could not analyse this right now. Please try again.");
        } catch (ResourceAccessException e) {
            log.error("Gemini call timed out or could not connect: {}", e.getMessage());
            throw new IllegalStateException("The AI took too long to answer. Please try again.");
        } catch (RuntimeException e) {
            log.error("Gemini diagnosis failed", e);
            throw new IllegalStateException("The AI gave an answer we could not read. Please try again.");
        }
    }

    private Map<?, ?> callWithRetry(String url, String key, Map<String, Object> body) {
        for (int attempt = 1; ; attempt++) {
            try {
                return client.post()
                        .uri(url)
                        .header("x-goog-api-key", key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body)
                        .retrieve()
                        .body(Map.class);
            } catch (RestClientResponseException e) {
                int s = e.getStatusCode().value();
                boolean retryable = (s == 503 || s == 500 || s == 429) && attempt < MAX_ATTEMPTS;
                if (!retryable) {
                    throw e;
                }
                log.warn("Gemini returned HTTP {} (attempt {} of {}), retrying", s, attempt, MAX_ATTEMPTS);
                pause(1500L * attempt);
            } catch (ResourceAccessException e) {
                if (attempt >= MAX_ATTEMPTS) {
                    throw e;
                }
                log.warn("Gemini call failed (attempt {} of {}), retrying", attempt, MAX_ATTEMPTS);
                pause(1500L * attempt);
            }
        }
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static String extractText(Map<?, ?> response) {
        List<?> candidates = (List<?>) response.get("candidates");
        Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
        List<?> parts = (List<?>) content.get("parts");
        return String.valueOf(((Map<?, ?>) parts.get(0)).get("text"));
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }
}