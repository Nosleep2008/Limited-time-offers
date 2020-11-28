package com.limitedtimeoffers.springboot.enums;

public enum SeckillStatEnum {

    SUCCESS(1, "SUCCESS ORDER"),
    END(0, "OFFER END"),
    REPEAT_KILL(-1,"REPEAT ORDER"),
    INNER_ERROR(-2, "SYSTEM ERROR"),
    DATA_REWRITE(-3, "DATA REWRITE");

    private int state;
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum state : values()){
            if (state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
