
package com.mistakemuseum.mistake_museum.model;

public record DiagnosisResult(
        Label label,
        String reason,
        String topic,
        double confidence,
        String errorStep
) {
}
