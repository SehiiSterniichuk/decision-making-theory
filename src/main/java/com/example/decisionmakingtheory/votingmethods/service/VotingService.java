package com.example.decisionmakingtheory.votingmethods.service;

import com.example.decisionmakingtheory.config.Config;
import com.example.decisionmakingtheory.votingmethods.domain.ElectionData;
import com.example.decisionmakingtheory.votingmethods.domain.VotingResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@AllArgsConstructor
public class VotingService {
    private final Config config;
    private final ElectionDataFabric fabric;

    public List<VotingResult> defaultScenario(String fileName) {
        ElectionData data = fabric.readElectionDataFromFile(config.getPathToInputFolder() + File.separator + fileName);
        return List.of(absoluteMajority(data));
    }


    private VotingResult absoluteMajority(ElectionData data) {
        return null;
    }
}
