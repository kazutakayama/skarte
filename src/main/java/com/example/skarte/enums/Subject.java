package com.example.skarte.enums;

public enum Subject {
    KOKUGO(1, "国語"), SYAKAI(2, "社会"), SUGAKU(3, "数学"), RIKA(4, "理科"), ONGAKU(5, "音楽"), BIJUTSU(6, "美術"), HOKENTAIIKU(7, "保健体育"),
    GIJUTSUKATEI(8, "技術家庭"), EIGO(9, "英語");

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