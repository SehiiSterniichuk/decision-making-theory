package com.example.decisionmakingtheory.votingmethods.domain;

import java.util.List;

public record Response(String source, ElectionData data, List<VotingResult> results) {
}
