package com.grepp.moodlink.app.model.llm;

public class EuclideanDistance {
    public static float compute(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("벡터 길이가 일치하지 않습니다.");
        }

        float sum = 0.0f;
        for (int i = 0; i < vectorA.length; i++) {
            float diff = vectorA[i] - vectorB[i];
            sum += diff * diff;
        }
        return (float) Math.sqrt(sum);
    }
}
