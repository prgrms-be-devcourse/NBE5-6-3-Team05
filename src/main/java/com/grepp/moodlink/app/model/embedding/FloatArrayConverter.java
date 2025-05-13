package com.grepp.moodlink.app.model.embedding;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.*;

@Converter
public class FloatArrayConverter implements AttributeConverter<float[], String> {
    @Override
    public String convertToDatabaseColumn(float[] floats) {
        if (floats == null) return null;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < floats.length; i++) {
            sb.append(floats[i]);
            if (i < floats.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public float[] convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) return new float[0];
        String[] parts = s.replace("[", "").replace("]", "").split(",\\s*");
        List<Float> resultList = new ArrayList<>();
        for (String part : parts) {
            if (part == null || part.trim().isEmpty()) continue; // 빈 문자열 무시
            resultList.add(Float.parseFloat(part.trim()));
        }
        float[] result = new float[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
    }
}


