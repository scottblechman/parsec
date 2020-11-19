package dev.scottblechman.parsec.state;

import java.util.HashMap;
import java.util.Map;

public class ScoreService {

    private final HashMap<Integer, Integer> scores;

    public ScoreService() {
        scores = new HashMap<>();
    }

    public Map<Integer, Integer> getScores() {
        return scores;
    }

    public int getTotal() {
        int total = 0;
        for(int value : scores.values()) {
            total += value;
        }
        return total;
    }

    public void writeScore(int level, int score) {
        scores.put(level, score);
    }
}
