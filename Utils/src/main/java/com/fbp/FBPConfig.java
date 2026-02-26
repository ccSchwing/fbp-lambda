package com.fbp;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class FBPConfig {
    private String week;

    public FBPConfig() {}

    @DynamoDbPartitionKey
    @DynamoDbAttribute("Week")  // Map to the actual DynamoDB attribute name
    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return "FBPConfig{week='" + week + "'}";
    }
}

