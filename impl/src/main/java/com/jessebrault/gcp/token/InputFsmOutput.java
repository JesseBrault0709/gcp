package com.jessebrault.gcp.token;

final class InputFsmOutput implements FsmOutput {

    private final CharSequence input;

    public InputFsmOutput(CharSequence input) {
        this.input = input;
    }

    @Override
    public CharSequence entire() {
        return this.input;
    }

    @Override
    public CharSequence part(int index) {
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return "InputFsmOutput(" + this.input + ")";
    }
}
