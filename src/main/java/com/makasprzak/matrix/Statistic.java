package com.makasprzak.matrix;

import com.google.common.base.MoreObjects;

public class Statistic {
    private Equation equation;
    private Long timeTaken;

    public Equation getEquation() {
        return equation;
    }

    public void setEquation(Equation equation) {
        this.equation = equation;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistic statistic = (Statistic) o;

        if (!equation.equals(statistic.equation)) return false;
        if (!timeTaken.equals(statistic.timeTaken)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = equation.hashCode();
        result = 31 * result + timeTaken.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("equation", equation)
                .add("timeTaken", timeTaken)
                .toString();
    }

    public static interface EquationStep {
        TimeTakenStep withEquation(Equation equation);
    }

    public static interface TimeTakenStep {
        BuildStep withTimeTaken(Long timeTaken);
    }

    public static interface BuildStep {
        Statistic build();
    }

    public static class Builder implements EquationStep, TimeTakenStep, BuildStep {
        private Equation equation;
        private Long timeTaken;

        private Builder() {
        }

        public static EquationStep statistic() {
            return new Builder();
        }

        @Override
        public TimeTakenStep withEquation(Equation equation) {
            this.equation = equation;
            return this;
        }

        @Override
        public BuildStep withTimeTaken(Long timeTaken) {
            this.timeTaken = timeTaken;
            return this;
        }

        @Override
        public Statistic build() {
            Statistic statistic = new Statistic();
            statistic.setEquation(this.equation);
            statistic.setTimeTaken(this.timeTaken);
            return statistic;
        }
    }
}
