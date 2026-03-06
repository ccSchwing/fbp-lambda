package com.fbp;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FBPGridSheet {
    private double week;
    private String gameId;
    private String awayTeam;
    private String homeTeam;
    private String date;
    private double spread;
    private double finalWithSpread;
    private String Underdog;

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

    @DynamoDbAttribute("Date")
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @DynamoDbAttribute("FinalWithSpread")
    public double getFinalWithSpread() { return finalWithSpread; }
    public void setFinalWithSpread(double finalWithSpread) { this.finalWithSpread = finalWithSpread; }

    @DynamoDbAttribute("Spread")
    public double getSpread() { return spread; }
    public void setSpread(double spread) { this.spread = spread; }

    @DynamoDbAttribute("Underdog")
    public String getUnderdog() { return Underdog; }
    public void setUnderdog(String Underdog) { this.Underdog = Underdog; }
}
