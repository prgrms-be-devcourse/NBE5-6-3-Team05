package com.grepp.moodlink.app.model.llm;

public class PearsonCorrelation {
    public static float compute(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("벡터 길이가 일치하지 않습니다.");
        }

        float sumA = 0.0f, sumB = 0.0f;
        float sumASq = 0.0f, sumBSq = 0.0f;
        float sumAB = 0.0f;
        int n = vectorA.length;

        for (int i = 0; i < n; i++) {
            sumA += vectorA[i];
            sumB += vectorB[i];
            sumASq += vectorA[i] * vectorA[i];
            sumBSq += vectorB[i] * vectorB[i];
            sumAB += vectorA[i] * vectorB[i];
        }

        float numerator = sumAB - (sumA * sumB) / n;
        float denominator = (float) Math.sqrt(
                (sumASq - (sumA * sumA) / n) *
                        (sumBSq - (sumB * sumB) / n)
        );

        return (denominator == 0) ? 0 : numerator / denominator;
    }
}
