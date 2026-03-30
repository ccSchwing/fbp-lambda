package com.fbp;

import java.util.Date;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class FBPLogAction {
    private String email;
    private Date date;
    private String week;
    private String action;
    private String details;
    private String level;

    @DynamoDbPartitionKey
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @DynamoDbAttribute("date")
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @DynamoDbAttribute("week")
    public String getWeek() { return week; }
    public void setWeek(String week) { this.week = week; }

    @DynamoDbAttribute("action")
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    @DynamoDbAttribute("details")
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    @DynamoDbAttribute("level")
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    @Override
    public String toString() {
        return "FBPLogAction{" +
                "email='" + email + '\'' +
                ", week='" + week + '\'' +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

}
