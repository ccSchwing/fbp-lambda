package com.fbp;

import java.util.HashMap;
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
            System.out.println("=== Environment Variables ===");
            System.getenv().forEach((key, value) -> 
            System.out.println(key + " = " + value)
            );
            System.out.println("=============================");
            System.out.println("=============================");
            // ...existing code...
String body = request.getBody();
Boolean b64 = request.getIsBase64Encoded();
System.out.println("isBase64Encoded=" + b64);
System.out.println("body length=" + (body == null ? 0 : body.length()));

if (Boolean.TRUE.equals(b64) && body != null) {
    body = new String(java.util.Base64.getDecoder().decode(body), java.nio.charset.StandardCharsets.UTF_8);
}

System.out.println("body preview=" + (body == null ? "null" : body.substring(0, Math.min(body.length(), 300))));

ObjectMapper objectMapper = new ObjectMapper();
FBPUser fbpUser = objectMapper.readValue(body, FBPUser.class);
System.out.println("parsed user=" + fbpUser);
// ...existing code...
        // ObjectMapper objectMapper = new ObjectMapper();
        // FBPUser fbpUser = objectMapper.readValue(request.getBody(), FBPUser.class);
        System.out.println("FBPUser to add: " + fbpUser.toString());

        DynamoDbClient dynamoDB = DynamoDbClient.builder().build();

        String tableName = System.getenv("FBPUsersTableName");
        System.out.println("FBPUser Table Name: " + tableName);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("email", AttributeValue.builder().s(fbpUser.getEmail()).build());
        item.put("firstName", AttributeValue.builder().s(fbpUser.getFirstName()).build());
        item.put("lastName", AttributeValue.builder().s(fbpUser.getLastName()).build());
        item.put("displayName", AttributeValue.builder().s(fbpUser.getDisplayName()).build());
        item.put("isAdmin", AttributeValue.builder().bool(fbpUser.getIsAdmin()).build());
        item.put("emailPickSheet", AttributeValue.builder().bool(fbpUser.getEmailPickSheet()).build());
        item.put("emailReminders", AttributeValue.builder().bool(fbpUser.getEmailReminders()).build());
        item.put("emailGridSheet", AttributeValue.builder().bool(fbpUser.getEmailGridSheet()).build());
        item.put("defaultAlgorithm", AttributeValue.builder().s(fbpUser.getDefaultAlgorithm()).build());
        item.put("isAccountLocked", AttributeValue.builder().bool(fbpUser.getIsAccountLocked()).build());
        item.put("isPaidUser", AttributeValue.builder().bool(fbpUser.getIsPaidUser()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        dynamoDB.putItem(putItemRequest);
        System.out.println("Table Name from ENV: " + tableName);
        return new APIGatewayProxyResponseEvent().withStatusCode(200)
            .withHeaders(Map.of(
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                "Access-Control-Allow-Methods", "POST,OPTIONS"
            ))
            .withBody("User added successfully");
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
