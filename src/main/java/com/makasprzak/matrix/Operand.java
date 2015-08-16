package com.makasprzak.matrix;

public enum Operand {
    MULTIPLY("*") {
        @Override
        public int expected(int left, int right) {
            return left * right;
        }
    },DIVIDE(":") {
        @Override
        public int expected(int left, int right) {
            return left / right;
        }
    };
    private final String string;
    
    private Operand(String string) {
        this.string = string;
    }
    
    public abstract int expected(int left, int right);
    
    @Override
    public String toString() {
        return string;
    }

    
}
