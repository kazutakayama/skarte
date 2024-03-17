package com.example.skarte.enums;

public enum Chikoku {
    NULL(0, ""), CHIKOKU(1, "遅刻");

    private int code;
    private String name;

    private Chikoku(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}