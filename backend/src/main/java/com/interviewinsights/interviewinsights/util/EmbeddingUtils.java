package com.interviewinsights.interviewinsights.util;

import java.util.ArrayList;
import java.util.List;

public class EmbeddingUtils {

    public static List<Float> parseEmbedding(String embedding) {

        embedding = embedding.replace("[", "")
                .replace("]", "");

        String[] values = embedding.split(",");

        List<Float> vector = new ArrayList<>();

        for (String value : values) {
            vector.add(Float.parseFloat(value.trim()));
        }

        return vector;
    }

    public static double cosineSimilarity(
            List<Float> vector1,
            List<Float> vector2) {

        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (int i = 0; i < vector1.size(); i++) {

            dotProduct += vector1.get(i) * vector2.get(i);

            magnitude1 += Math.pow(vector1.get(i), 2);

            magnitude2 += Math.pow(vector2.get(i), 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        return dotProduct / (magnitude1 * magnitude2);
    }

}