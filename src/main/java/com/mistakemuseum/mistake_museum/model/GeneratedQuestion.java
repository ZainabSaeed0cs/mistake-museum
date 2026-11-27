
package com.mistakemuseum.mistake_museum.model;

public class GeneratedQuestion {

    private final int number;
    private final String subjectAndTopic;
    private final String questionText;
    private final String answer;
    private final String explanation;

    public GeneratedQuestion(int number, String subjectAndTopic,
                             String questionText, String answer,
                             String explanation) {
        this.number = number;
        this.subjectAndTopic = subjectAndTopic;
        this.questionText = questionText;
        this.answer = answer;
        this.explanation = explanation;
    }

    public int getNumber() {
        return number;
    }

    public String getSubjectAndTopic() {
        return subjectAndTopic;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }
}
