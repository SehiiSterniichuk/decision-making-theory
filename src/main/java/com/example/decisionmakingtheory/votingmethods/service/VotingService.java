package com.example.decisionmakingtheory.votingmethods.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.votingmethods.domain.ElectionData;
import com.example.decisionmakingtheory.votingmethods.domain.VotingResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class VotingService {
    private final Config config;
    private final ElectionDataFabric fabric;

    public List<VotingResult> defaultScenario(String fileName) {
        ElectionData data = fabric.readElectionDataFromFile(config.getPathToInputFolder() + File.separator + fileName);
        return List.of(simpson(data));
    }


    record Simpson(char x, int points) {
    }

    record SimpsonData(char c, List<Simpson> data) {
    }

    private VotingResult simpson(ElectionData data) {
        char[] candidates = data.rating()[0].clone();
        Arrays.sort(candidates);
        List<SimpsonData> simpsons = new ArrayList<>(candidates.length);
        for (var candidate : candidates) {
            simpsons.add(calculateSimpson(candidate, candidates, data));
        }
        var winner = simpsons.get(0);
        int maxScore = Integer.MIN_VALUE;
        for (var s : simpsons) {
            var score = s.data().stream().mapToInt(Simpson::points).min().orElse(-1);
            if (score > maxScore) {
                maxScore = score;
                winner = s;
            }
        }
        return new VotingResult("Simpson", winner.c(), "");
    }

    private SimpsonData calculateSimpson(char candidate, char[] candidates, ElectionData data) {
        ArrayList<Simpson> simpsons = new ArrayList<>(candidates.length - 1);
        for (char x : candidates) {
            if (x == candidate) continue;
            for (int i = 0; i < data.votes().length; i++) {
                int sum = 0;
                for (var x1 : data.rating()[i]) {
                    if (x1 == x) {
                        break;
                    } else if (x1 == candidate) {
                        sum += data.votes()[i];
                        break;
                    }
                }
                simpsons.add(new Simpson(x, sum));
            }
        }
        return new SimpsonData(candidate, simpsons);
    }
}
