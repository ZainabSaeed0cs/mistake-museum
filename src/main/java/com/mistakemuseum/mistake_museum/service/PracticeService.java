
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.PracticeQuestion;
import org.springframework.stereotype.Service;

import com.mistakemuseum.mistake_museum.model.GeneratedQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PracticeService {

    public List<PracticeQuestion> generate(Label topLabel) {

        return switch (topLabel) {

            case MISREAD_QUESTION -> List.of(

                new PracticeQuestion(
                    "Convert 3 km to metres.",
                    "3000 m",
                    topLabel
                ),

                new PracticeQuestion(
                    "Convert 750 cm to metres.",
                    "7.5 m",
                    topLabel
                ),

                new PracticeQuestion(
                    "A car travels 150 km in 3 hours. Find its average speed.",
                    "50 km/h",
                    topLabel
                ),

                new PracticeQuestion(
                    "Convert 2.5 hours into minutes.",
                    "150 minutes",
                    topLabel
                ),

                new PracticeQuestion(
                    "Find the perimeter of a rectangle with length 8 cm and width 5 cm.",
                    "26 cm",
                    topLabel
                )
            );

            case CARELESS_SLIP -> List.of(

                new PracticeQuestion(
                    "Calculate 48 + 37.",
                    "85",
                    topLabel
                ),

                new PracticeQuestion(
                    "Calculate 15 x 6.",
                    "90",
                    topLabel
                ),

                new PracticeQuestion(
                    "A body has mass 4 kg and acceleration 3 m/s^2. Calculate the force.",
                    "12 N",
                    topLabel
                ),

                new PracticeQuestion(
                    "Calculate 144 / 12.",
                    "12",
                    topLabel
                ),

                new PracticeQuestion(
                    "A resistor has a voltage of 24 V and resistance of 6 ohms. Find the current.",
                    "4 A",
                    topLabel
                )
            );

            case CONCEPT_GAP -> List.of(

                new PracticeQuestion(
                    "What is the derivative of x^3 with respect to x?",
                    "3x^2",
                    topLabel
                ),

                new PracticeQuestion(
                    "What is the SI unit of energy?",
                    "Joule (J)",
                    topLabel
                ),

                new PracticeQuestion(
                    "What is the value of cos(0 degrees)?",
                    "1",
                    topLabel
                ),

                new PracticeQuestion(
                    "What happens to the acceleration of an object moving with constant velocity in a straight line?",
                    "Its acceleration is zero (0 m/s^2).",
                    topLabel
                ),

                new PracticeQuestion(
                    "What is the slope of a vertical straight line?",
                    "Undefined",
                    topLabel
                )
            );

            case FORMULA_MISUSE -> List.of(

                new PracticeQuestion(
                    "Using A = l x w, calculate the area of a rectangle with length 9 cm and width 4 cm.",
                    "36 cm^2",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using F = ma, calculate the force when mass is 5 kg and acceleration is 2 m/s^2.",
                    "10 N",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using v = u + at, find final velocity when u = 4 m/s, a = 3 m/s^2 and t = 2 s.",
                    "10 m/s",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using KE = 1/2 mv^2, calculate kinetic energy for a 4 kg object moving at 2 m/s.",
                    "8 J",
                    topLabel
                ),

                new PracticeQuestion(
                    "Using P = W/t, calculate power when 120 J of work is done in 6 seconds.",
                    "20 W",
                    topLabel
                )
            );

            case INCOMPLETE_ANSWER -> List.of(

                new PracticeQuestion(
                    "Solve 3x + 5 = 20. Give the value of x.",
                    "x = 5",
                    topLabel
                ),

                new PracticeQuestion(
                    "A car travels 180 metres in 30 seconds. Find its speed, including the unit.",
                    "6 m/s",
                    topLabel
                ),

                new PracticeQuestion(
                    "Find the circumference of a circle with radius 7 cm. Use pi = 22/7.",
                    "44 cm",
                    topLabel
                ),

                new PracticeQuestion(
                    "A force of 30 N acts on a body of mass 6 kg. Find its acceleration, including the unit.",
                    "5 m/s^2",
                    topLabel
                ),

                new PracticeQuestion(
                    "Solve x^2 = 64 for the positive value of x.",
                    "x = 8",
                    topLabel
                )
            );
        };
    }
    
  
    // ===== Part 2: Gemini question generation =====

    private static final Logger log =
            LoggerFactory.getLogger(PracticeService.class);

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/";

    private static final int MAX_ATTEMPTS = 3;

    private final RestClient client = buildClient();

    private static RestClient buildClient() {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(120));

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }

    public List<GeneratedQuestion> generateQuestions(
            String subject,
            String topic,
            String difficulty,
            int count,
            List<String> weakTopics,
            List<String> previousQuestions) {

        String level = cleanDifficulty(difficulty);
        int total = count == 10 ? 10 : 5;

        List<String> weak = weakTopics == null
                ? new ArrayList<>()
                : weakTopics;

        boolean noSubject = subject == null || subject.isBlank();
        boolean noTopic = topic == null || topic.isBlank();

        if (noSubject && noTopic && weak.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please analyze a mistake first, or choose a subject or topic.");
        }

        String focus;

        if (!noTopic) {
            focus = "Subject: "
                    + (noSubject ? "choose the matching subject" : subject)
                    + ". Topic: " + topic + ".";
        } else if (!noSubject) {
            focus = "Subject: " + subject + ". "
                    + (weak.isEmpty()
                    ? "Choose suitable topics for this subject."
                    : "Choose topics from this list: "
                    + String.join(", ", weak) + ".");
        } else {
            focus = "Choose from the student's weak topics: "
                    + String.join(", ", weak) + ".";
        }

        String avoidText = previousQuestions == null
                || previousQuestions.isEmpty()
                ? ""
                : "Do NOT repeat or closely copy these earlier questions:\n- "
                + String.join("\n- ", previousQuestions);

        String key = System.getenv("GEMINI_API_KEY");

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "The AI service is not set up yet. Please try again later.");
        }

        String model = System.getenv("GEMINI_MODEL");

        if (model == null || model.isBlank()) {
            model = "gemini-flash-latest";
        }

        String url = BASE_URL + model + ":generateContent";

        String prompt = """
                You are an educational practice-question generator.

                Generate exactly %d original practice questions.

                %s

                Difficulty: %s

                Create clear, accurate questions suitable for a student.
                Include a correct answer and a short explanation for each.
                Match the selected difficulty.
                Avoid repeating earlier questions.

                %s

                Return ONLY valid JSON in this exact structure:
                {
                  "questions": [
                    {
                      "subjectAndTopic": "Mathematics - Algebra",
                      "questionText": "Question text",
                      "answer": "Correct answer",
                      "explanation": "Short explanation"
                    }
                  ]
                }
                """.formatted(total, focus, level, avoidText);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                ),
                "generationConfig", Map.of(
                        "temperature", 0.4,
                        "responseMimeType", "application/json"
                )
        );

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                String response = client.post()                
        .uri(url)
        .header("x-goog-api-key", key)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
         .body(requestBody)
        .retrieve()
        .body(String.class);

                if (response == null || response.isBlank()) {
                    throw new IllegalStateException(
                            "The AI service returned an empty response.");
                }

                Map<String, Object> responseMap =
                        JsonParserFactory.getJsonParser().parseMap(response);

                List<?> candidates =
                        (List<?>) responseMap.get("candidates");

                if (candidates == null || candidates.isEmpty()) {
                    throw new IllegalStateException(
                            "The AI could not generate questions. Please try again.");
                }

                Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
                Map<?, ?> content = (Map<?, ?>) candidate.get("content");
                List<?> parts = (List<?>) content.get("parts");

                StringBuilder generatedText = new StringBuilder();

                for (Object partObject : parts) {
                    Map<?, ?> part = (Map<?, ?>) partObject;
                    Object text = part.get("text");

                    if (text instanceof String value) {
                        generatedText.append(value);
                    }
                }
            log.info("Gemini practice response text: {}", generatedText);
                Map<String, Object> generated =
                        JsonParserFactory.getJsonParser()
                                .parseMap(generatedText.toString());

                List<?> questionList =
                        (List<?>) generated.get("questions");

                if (questionList == null) {
                    throw new IllegalStateException(
                            "The AI response did not contain questions.");
                }

                List<GeneratedQuestion> result = new ArrayList<>();
                Set<String> seen = new HashSet<>();

                if (previousQuestions != null) {
                    for (String previous : previousQuestions) {
                        if (previous != null) {
                            seen.add(previous.trim().toLowerCase());
                        }
                    }
                }

                for (Object itemObject : questionList) {
                    if (!(itemObject instanceof Map<?, ?> item)) {
                        continue;
                    }

                    String questionText =
                            readText(item, "questionText");
                    String answer =
                            readText(item, "answer");
                    String explanation =
                            readText(item, "explanation");

                    if (questionText.isBlank()
                            || answer.isBlank()
                            || explanation.isBlank()) {
                        continue;
                    }

                    if (!seen.add(questionText.toLowerCase())) {
                        continue;
                    }

                    String subjectAndTopic =
                            readText(item, "subjectAndTopic");

                    if (subjectAndTopic.isBlank()) {
                        subjectAndTopic =
                                (noSubject ? "Practice" : subject)
                                + (noTopic ? "" : " - " + topic);
                    }

                    result.add(new GeneratedQuestion(
                            result.size() + 1,
                            subjectAndTopic,
                            questionText,
                            answer,
                            explanation
                    ));

                    if (result.size() == total) {
                        break;
                    }
                }

                if (result.size() != total) {
                    throw new IllegalStateException(
                            "The AI generated fewer valid questions than requested. Please try again.");
                }

                return result;

            } catch (RestClientResponseException ex) {
               log.error("Gemini returned HTTP status {}. Response body: {}",
        ex.getStatusCode(),
        ex.getResponseBodyAsString());

                if (ex.getStatusCode().is4xxClientError()
                        || attempt == MAX_ATTEMPTS) {
                    throw new IllegalStateException(
                            "The AI service could not generate questions. Please try again later.");
                }

            } catch (ResourceAccessException ex) {
                log.warn("Gemini connection attempt {} failed.",
                        attempt);

                if (attempt == MAX_ATTEMPTS) {
                    throw new IllegalStateException(
                            "Could not connect to the AI service. Please try again later.");
                }

            } catch (IllegalStateException ex) {
                throw ex;

            } catch (Exception ex) {
                log.error("Could not process Gemini response.", ex);

                throw new IllegalStateException(
                        "The AI returned an invalid response. Please try again.");
            }
        }

        throw new IllegalStateException(
                "Could not generate practice questions. Please try again.");
    }

    private String cleanDifficulty(String difficulty) {
        if (difficulty == null) {
            return "Medium";
        }

        return switch (difficulty.trim().toLowerCase()) {
            case "easy" -> "Easy";
            case "hard" -> "Hard";
            default -> "Medium";
        };
    }

    private String readText(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value instanceof String text ? text.trim() : "";
    }
}
