package com.fbp;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FBPGridSheetRow {
    private double week;
    private String gameId;
    private String awayTeam;
    private String homeTeam;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("Week")
    public double getWeek() { return week; }
    public void setWeek(double week) { this.week = week; }

    @DynamoDbSortKey
    @DynamoDbAttribute("GameId")
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    @DynamoDbAttribute("Away")
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    
    @DynamoDbAttribute("Home")
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    @Override
    public String toString() {
        return "FBPGridSheetRow{" +
                "week=" + week +
                ", gameId='" + gameId + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                
      '}';
    }
}