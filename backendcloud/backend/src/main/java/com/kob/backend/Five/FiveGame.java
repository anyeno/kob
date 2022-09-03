package com.kob.backend.Five;


public class FiveGame {
    public Integer aId;
    public Integer bId;
    public int [][] g = new int[15][15];
    public int steps = 0;
    public FiveGame(Integer aId, Integer bId){
        this.aId = aId;
        this.bId = bId;
    }
}
