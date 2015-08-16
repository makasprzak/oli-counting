package com.makasprzak.matrix;

import java.util.List;
import java.util.Map;

import static com.makasprzak.matrix.Statistic.Builder.statistic;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class EquationManager {
    private final Map<Equation, Long> statistics;
    private final List<Equation> equationList;

    private EquationManager(Map<Equation, Long> statistics, List<Equation> equationList) {
        this.statistics = statistics;
        this.equationList = equationList;
    }

    public static EquationManager create(List<Equation> equationList) {
        return new EquationManager(
                equationList
                        .stream()
                        .collect(toMap(equation -> equation, any -> Long.valueOf(0))),
                equationList);
    }

    public void remove(int index) {
        this.equationList.remove(index);
    }

    public Equation get(int index) {
        return equationList.get(index);
    }

    public void addTimeSpent(Equation equation, long time) {
        this.statistics.put(equation, statistics.get(equation) + time);
    }

    public List<Statistic> getStatistics() {
        return statistics.entrySet()
                .stream()
                .map(entry -> statistic()
                        .withEquation(entry.getKey())
                        .withTimeTaken(entry.getValue()).build())
                .collect(toList());
    }

    public boolean isEmpty() {
        return equationList.isEmpty();
    }

    public int size() {
        return equationList.size();
    }
}
