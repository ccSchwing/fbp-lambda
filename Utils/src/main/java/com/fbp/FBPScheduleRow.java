package com.fbp;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FBPScheduleRow {
    private double week;
    private String gameId;
    private String awayTeam;
    private String homeTeam;
    private double homeScore;
    private double awayScore;
    private String date;
    private double spread;
    private String finalWithSpread;
    private String underdog;
    private String winner;

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

    @DynamoDbAttribute("HomeScore")
    public double getHomeScore() { return homeScore; }
    public void setHomeScore(double homeScore) { this.homeScore = homeScore; }

    @DynamoDbAttribute("AwayScore")
    public double getAwayScore() { return awayScore; }
    public void setAwayScore(double awayScore) { this.awayScore = awayScore; }

    @DynamoDbAttribute("Date")
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @DynamoDbAttribute("FinalWithSpread")
    public String getFinalWithSpread() { return finalWithSpread; }
    public void setFinalWithSpread(String finalWithSpread) { this.finalWithSpread = finalWithSpread; }

    @DynamoDbAttribute("Spread")
    public double getSpread() { return spread; }
    public void setSpread(double spread) { this.spread = spread; }

    @DynamoDbAttribute("Underdog")
    public String getUnderdog() { return underdog; }
    public void setUnderdog(String underdog) { this.underdog = underdog; }

    @DynamoDbAttribute("Winner")
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}
