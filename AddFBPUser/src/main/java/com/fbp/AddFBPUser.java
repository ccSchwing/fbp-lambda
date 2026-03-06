package com.fbp;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;



public class AddFBPUser {
    public APIGatewayProxyResponseEvent addFBPUser(APIGatewayProxyRequestEvent request)
        throws JsonMappingException, JsonProcessingException {
        try{ 
        ObjectMapper objectMapper = new ObjectMapper();
        FBPUserBean fbpUser = objectMapper.readValue(request.getBody(), FBPUserBean.class);
        DynamoDbClient dynamoDB = DynamoDbClient.builder().build();

        String tableName = System.getenv("FBPUserTableName");

         PutItemRequest putItemRequest =
            PutItemRequest.builder()
                .tableName(tableName)
                .item(java.util.Map.of(
                    "email", AttributeValue.builder().s(fbpUser.getEmail()).build(),
                    "firstName", AttributeValue.builder().s(fbpUser.getFirstName()).build(),
                    "lastName", AttributeValue.builder().s(fbpUser.getLastName()).build(),
                    "displayName", AttributeValue.builder().s(fbpUser.getDisplayName()).build()
                ))
                .build();
        dynamoDB.putItem(putItemRequest);
        System.out.println("Table Name from ENV: " + tableName);
        return new APIGatewayProxyResponseEvent().withStatusCode(200)
            .withHeaders(Map.of(
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                "Access-Control-Allow-Methods", "POST,OPTIONS"
            ))
            .withBody("Picks saved successfully");
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500)
                .withHeaders(Map.of(
                    "Access-Control-Allow-Origin", "*",
                    "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                    "Access-Control-Allow-Methods", "POST,OPTIONS"
                ))
                .withBody("Error processing order: " + e.getMessage());
        }
    }
}
