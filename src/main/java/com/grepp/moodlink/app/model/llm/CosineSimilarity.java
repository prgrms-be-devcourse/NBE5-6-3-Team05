package com.grepp.moodlink.app.model.llm;

public class CosineSimilarity {
    public static float compute(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("벡터 길이가 일치하지 않습니다.");
        }

        float dotProduct = 0.0f;
        float magnitudeA = 0.0f;
        float magnitudeB = 0.0f;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            magnitudeA += vectorA[i] * vectorA[i];
            magnitudeB += vectorB[i] * vectorB[i];
        }

        magnitudeA = (float) Math.sqrt(magnitudeA);
        magnitudeB = (float) Math.sqrt(magnitudeB);

        if (magnitudeA == 0 || magnitudeB == 0) {
            return 0.0f;
        }

        return dotProduct / (magnitudeA * magnitudeB);
    }
}
