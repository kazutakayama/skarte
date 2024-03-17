package com.example.skarte.enums;

public enum Subject {
    KOKUGO(1, "国語"), SUGAKU(2, "数学"), EIGO(3, "英語"), SYAKAI(4, "社会"), RIKA(5, "理科"), TAIIKU(6, "保健体育"), ONGAKU(7, "音楽"),
    BIJUTSU(8, "美術"), GIJUTSU_KATEI(9, "技術家庭");

    private int code;
    private String name;

    private Subject(int code, String name) {
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