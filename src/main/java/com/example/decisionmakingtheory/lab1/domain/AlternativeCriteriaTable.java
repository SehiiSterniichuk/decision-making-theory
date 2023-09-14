package com.example.decisionmakingtheory.lab1.domain;


public record AlternativeCriteriaTable(int[][] alternatives) {
    @Override
    public String toString() {
        return "AlternativeCriteriaTable{" +
                "alternatives=" + getMatrix() +
                '}';
    }

    public int getLength(){
        return alternatives.length;
    }

    private String getMatrix() {
        var builder = new StringBuilder(alternatives.length * alternatives[0].length);
        builder.append("[");
        for (var array : alternatives) {
            builder.append("{");
            for (var i : array){
                builder.append(i).append(",");
            }
            builder.append("},");
        }
        builder.append("]");
        return builder.toString();
    }
}
