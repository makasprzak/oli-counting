package com.makasprzak.matrix;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Joiner.on;
import static java.util.Arrays.asList;

public class Equation {
    private int left;
    private int right;
    private Operand operand;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public Operand getOperand() {
        return operand;
    }

    public void setOperand(Operand operand) {
        this.operand = operand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Equation that = (Equation) o;

        return Objects.equal(this.left, that.left) &&
                Objects.equal(this.right, that.right) &&
                Objects.equal(this.operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left, right, operand);
    }

    public static interface LeftStep {
        RightStep withLeft(int left);
    }

    public static interface RightStep {
        OperandStep withRight(int right);
    }

    public static interface OperandStep {
        BuildStep withOperand(Operand operand);
    }

    public static interface BuildStep {
        Equation build();
    }

    
    public String print() {
        return on(" ").join(asList(Integer.toString(left), operand.toString(), Integer.toString(right),"= ?"));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("left", left)
                .add("right", right)
                .add("operand", operand)
                .toString();
    }

    public static class Builder implements LeftStep, RightStep, OperandStep, BuildStep {
        private int left;
        private int right;
        private Operand operand;

        private Builder() {
        }

        public static LeftStep equation() {
            return new Builder();
        }

        @Override
        public RightStep withLeft(int left) {
            this.left = left;
            return this;
        }

        @Override
        public OperandStep withRight(int right) {
            this.right = right;
            return this;
        }

        @Override
        public BuildStep withOperand(Operand operand) {
            this.operand = operand;
            return this;
        }

        @Override
        public Equation build() {
            Equation equation = new Equation();
            equation.setLeft(this.left);
            equation.setRight(this.right);
            equation.setOperand(this.operand);
            return equation;
        }
    }
    
    public int getExpected() {
        return operand.expected(left, right);
        
    }
}
