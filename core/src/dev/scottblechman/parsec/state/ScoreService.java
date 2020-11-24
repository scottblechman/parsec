package dev.scottblechman.parsec.state;

import java.util.ArrayList;
import java.util.List;

public class ScoreService {

    private final ArrayList<Integer> scores;

    // Tracks whether to append a new score or increment the current score
    private int currentLevel;

    public ScoreService() {
        scores = new ArrayList<>();
    }

    public List<Integer> getScores() {
        return scores;
    }

    public int getTotal() {
        int total = 0;
        for(int value : scores) {
            total += value;
        }
        return total;
    }

    public void writeScore(int level, int score) {
        if(currentLevel == level) {
            scores.set(scores.size() - 1, scores.get(scores.size() - 1) + 1);
        } else {
            scores.add(score);
        }
    }
}
