package com.example.decisionmakingtheory.votingmethods.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.votingmethods.domain.ElectionData;
import com.example.decisionmakingtheory.votingmethods.domain.VotingResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VotingService {
    private final Config config;
    private final ElectionDataFabric fabric;

    public List<VotingResult> defaultScenario(String fileName) {
        ElectionData data = fabric.readElectionDataFromFile(config.getPathToInputFolder() + File.separator + fileName);
        VotingResult simpson = simpson(data);
        VotingResult majority = absoluteMajority(data);
        return List.of(simpson, majority);
    }

    private VotingResult absoluteMajority(ElectionData data) {
        Map<Character, Integer> counter = new HashMap<>(data.votes().length);
        for (int i = 0; i < data.rating()[0].length; i++) {
            counter.merge(data.rating()[0][i], data.votes()[i], Integer::sum);
        }
        if (counter.size() < 3) {
            Set<Map.Entry<Character, Integer>> entries = counter.entrySet();
            return entries.stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(winner -> new VotingResult("Absolute majority", winner.getKey(), null))
                    .orElse(null);
        }
        Set<Character> nextTour = counter.entrySet().stream()
                .sorted((x, y) -> y.getValue() - x.getValue())
                .limit(2).map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return absoluteMajority(reduceData(data, nextTour));
    }

    private ElectionData reduceData(ElectionData data, Set<Character> characters) {
        char[][] rating = new char[2][data.rating()[0].length];
        for (int vote = 0; vote < data.votes().length; vote++) {
            int index = 0;
            for (int j = 0; j < data.rating().length; j++) {
                if (index == 2) break;
                char c = data.rating()[j][vote];
                if (characters.contains(c)) {
                    rating[index++][vote] = c;
                }
            }
        }
        return new ElectionData(data.votes(), rating);
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
        return new VotingResult("Simpson", winner.c(), null);
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
