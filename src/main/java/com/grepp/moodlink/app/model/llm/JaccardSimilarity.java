package com.grepp.moodlink.app.model.llm;

import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarity {
    public static float compute(Set<Integer> setA, Set<Integer> setB) {
        Set<Integer> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<Integer> union = new HashSet<>(setA);
        union.addAll(setB);

        return (float) intersection.size() / union.size();
    }
}
