package com.mistakemuseum.mistake_museum.model;

import java.util.Map;

public record ProfileResponse(
        int totalMistakes,
        Map<String, Integer> countsByLabel,
        String topLabel,
        String topTopic,
        String sentence,
        Map<String, Integer> weeklyTrend) {
}