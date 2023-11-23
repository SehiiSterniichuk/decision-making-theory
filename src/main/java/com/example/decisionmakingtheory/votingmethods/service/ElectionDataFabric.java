package com.example.decisionmakingtheory.votingmethods.service;

import com.example.decisionmakingtheory.votingmethods.domain.ElectionData;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ElectionDataFabric {
    public ElectionData readElectionDataFromFile(String fileName) {
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            var votes = Arrays.stream(reader.readLine().split(","))
                    .mapToInt(Integer::parseInt).toArray();
            String line;
            List<char[]> buffer = new ArrayList<>(4);
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                char[] data = new char[split.length];
                IntStream.range(0, split.length).forEach(i -> data[i] = split[i].charAt(0));
                buffer.add(data);
            }
            return new ElectionData(votes, buffer.stream().toArray(char[][]::new));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
