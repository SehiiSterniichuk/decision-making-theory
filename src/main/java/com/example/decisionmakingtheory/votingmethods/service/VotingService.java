package com.example.decisionmakingtheory.votingmethods.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.votingmethods.domain.ElectionData;
import com.example.decisionmakingtheory.votingmethods.domain.Response;
import com.example.decisionmakingtheory.votingmethods.domain.VotingResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class VotingService {
    private final Config config;
    private final ElectionDataFabric fabric;

    public Response defaultScenario(String fileName) {
        ElectionData data = fabric.readElectionDataFromFile(config.getPathToInputFolder() + File.separator + fileName);
        VotingResult simpson = simpson(data);
        VotingResult majority = absoluteMajority(data);
        VotingResult sequential = sequentialElimination(data);
        return new Response(fileName, data, List.of(majority, simpson, sequential));
    }

    private VotingResult sequentialElimination(ElectionData data) {
        char[] candidates = getCandidates(data);//відсортовані кандидати за алфавітом кодування ASCII
        char winner = candidates[0];
        for (var x : candidates) {// послідовно порівнюємо кандидатів з теперішнім переможцем
            if (winner == x) continue;
            var compareData = reduceData(data, Set.of(winner, x));//створюємо матрицю з двома кандидатами winner та x
            var counter = countScore(compareData);//обчислюємо хто набрав скільки балів
            winner = counter.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))//обираємо того в кого більше балів, того й записуємо у змінну winner
                    .map(Map.Entry::getKey).orElse('0');
        }
        return new VotingResult("Sequential Elimination", winner);
    }

    private VotingResult absoluteMajority(ElectionData data) {//метод абсолютної більшості у два тури
        Map<Character, Integer> counter = countScore(data);
        float half = Arrays.stream(data.votes()).sum() / 2f;
        Set<Map.Entry<Character, Integer>> entries = counter.entrySet();
        var firstTourWinner = entries.stream()
                .filter(e -> e.getValue() * 1.0 > half).findAny();//шукаємо кандидата який переміг у першому турі
        if(firstTourWinner.isPresent()) {//якщо такий є, то оголошуємо його переможцем
            return new VotingResult("Absolute majority", firstTourWinner.get().getKey());
        }
        //якщо жоден кандидат не набрав більш ніж половини голосів, то відбираємо 2 найсильніших у наступний тур
        Set<Character> nextTour = entries.stream()
                .sorted((x, y) -> y.getValue() - x.getValue())
                .limit(2).map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        //тоді рекурсивно передаємо у наступний тур двох найсильніших кандидатів
        return absoluteMajority(reduceData(data, nextTour));
    }

    private static Map<Character, Integer> countScore(ElectionData data) {
        //рахуємо скільки очок набрав кожен кандидат
        Map<Character, Integer> counter = new HashMap<>(data.votes().length);
        for (int i = 0; i < data.rating()[0].length; i++) {
            counter.merge(data.rating()[0][i], data.votes()[i], Integer::sum);
            //мердж в мапі створює нові дані за ключем, або модифікує значення що вже існує
            //у цьому випадку ми передаємо Integer::sum - ф-цію суми як модифікатор
        }
        return counter;
    }

    //ф-ція що залишає в матриці кандидатів наявних у переданій множині Set
    private ElectionData reduceData(ElectionData data, Set<Character> candidates) {
        char[][] rating = new char[candidates.size()][data.rating()[0].length];
        for (int vote = 0; vote < data.votes().length; vote++) {
            int index = 0;
            for (int j = 0; j < data.rating().length; j++) {
                if (index == candidates.size()) break;
                char c = data.rating()[j][vote];
                if (candidates.contains(c)) {
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
        char[] candidates = getCandidates(data);//Отримання всіх кандидатів у відсортованому вигляді
        List<SimpsonData> simpsons = new ArrayList<>(candidates.length);//список який зберігає всі S(a,x)
        for (var candidate : candidates) {
            simpsons.add(calculateSimpson(candidate, candidates, data));
        }
        var winner = simpsons.get(0);
        int maxScore = Integer.MIN_VALUE;
        for (var s : simpsons) {
            var score = s.data().stream().mapToInt(Simpson::points).min().orElse(-1);//обраховуємо S(a) = min(S(a,x))
            if (score > maxScore) {//зберігаємо кандидата з максимальними балами
                maxScore = score;
                winner = s;
            }
        }
        return new VotingResult("Simpson", winner.c());
    }

    private static char[] getCandidates(ElectionData data) {
        char[] candidates = new char[data.rating().length];
        IntStream.range(0, data.rating().length).forEach(i -> candidates[i] = data.rating()[i][0]);
        Arrays.sort(candidates);
        return candidates;
    }

    //метод обраховує S(a,x), де а - 'candidate' параметр
    private SimpsonData calculateSimpson(char candidate, char[] candidates, ElectionData data) {
        Map<Character, Integer> counter = new HashMap<>(candidates.length - 1);
        //counter - лічильник де ключ кандидат "x", значення - скільки голосів candidate був кращим за x
        for (char x : candidates) {//обраховуємо число виборців для яких 'candidate' краще за 'x'
            if (x == candidate) continue;
            for (int vote = 0; vote < data.votes().length; vote++) {
                for (int j = 0; j < data.rating().length; j++) {
                    char x1 = data.rating()[j][vote];
                    if (x1 == x) {//якщо ми зустріли x раніше за candidate - не зараховуємо нічого в суму
                        break;
                    } else if (x1 == candidate) {
                        //якщо ми зустріли candidate раніше за x - додаємо голоси в лічильник голоси
                        counter.merge(x, data.votes()[vote], Integer::sum);
                        break;
                    }
                }
            }
        }
        var simpsons = counter.entrySet().stream()
                .map(e -> new Simpson(e.getKey(), e.getValue())).toList();
        return new SimpsonData(candidate, simpsons);
    }
}
