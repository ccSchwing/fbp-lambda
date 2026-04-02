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
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class AddFBPUser {
    public APIGatewayProxyResponseEvent addFBPUser(APIGatewayProxyRequestEvent request)
            throws JsonMappingException, JsonProcessingException {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        // Add CORS headers to ALL responses
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        headers.put("Access-Control-Allow-Headers",
                "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        response.setHeaders(headers);

        // Handle OPTIONS preflight request
        if ("OPTIONS".equals(request.getHttpMethod())) {
            response.setStatusCode(200);
            response.setBody("");
            return response;
        }
        String body = request.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        FBPUser fbpUser = objectMapper.readValue(body, FBPUser.class);
        System.out.println("parsed user=" + fbpUser);
        System.out.println("FBPUser to add: " + fbpUser.toString());
        try {
            System.out.println("=== Environment Variables ===");
            System.getenv().forEach((key, value) -> System.out.println(key + " = " + value));
            System.out.println("=============================");
            System.out.println("=============================");
            // ...existing code...
            Boolean b64 = request.getIsBase64Encoded();
            System.out.println("isBase64Encoded=" + b64);
            System.out.println("body length=" + (body == null ? 0 : body.length()));

            if (Boolean.TRUE.equals(b64) && body != null) {
                body = new String(java.util.Base64.getDecoder().decode(body), java.nio.charset.StandardCharsets.UTF_8);
            }

            System.out.println(
                    "body preview=" + (body == null ? "null" : body.substring(0, Math.min(body.length(), 300))));

            DynamoDbClient dynamoDB = DynamoDbClient.builder().build();

            String tableName = System.getenv("FBPUsersTableName");
            System.out.println("FBPUser Table Name: " + tableName);

            // Build a partial update: only include fields that are present in the payload.
            // isAdmin, isAccountLocked, and isPaidUser are omitted from profile-update
            // requests, so they will never be overwritten by a non-admin caller.
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("email", AttributeValue.builder().s(fbpUser.getEmail()).build());

            Map<String, AttributeValue> exprValues = new HashMap<>();
            Map<String, String> exprNames = new HashMap<>();
            StringBuilder updateExpr = new StringBuilder("SET ");
            boolean first = true;

            // Helper lambda approach via inline method to keep code DRY
            if (fbpUser.getFirstName() != null) {
                updateExpr.append(first ? "" : ", ").append("#firstName = :firstName");
                exprNames.put("#firstName", "firstName");
                exprValues.put(":firstName", AttributeValue.builder().s(fbpUser.getFirstName()).build());
                first = false;
            }
            if (fbpUser.getLastName() != null) {
                updateExpr.append(first ? "" : ", ").append("#lastName = :lastName");
                exprNames.put("#lastName", "lastName");
                exprValues.put(":lastName", AttributeValue.builder().s(fbpUser.getLastName()).build());
                first = false;
            }
            if (fbpUser.getDisplayName() != null) {
                updateExpr.append(first ? "" : ", ").append("#displayName = :displayName");
                exprNames.put("#displayName", "displayName");
                exprValues.put(":displayName", AttributeValue.builder().s(fbpUser.getDisplayName()).build());
                first = false;
            }
            if (fbpUser.getDefaultAlgorithm() != null) {
                updateExpr.append(first ? "" : ", ").append("#defaultAlgorithm = :defaultAlgorithm");
                exprNames.put("#defaultAlgorithm", "defaultAlgorithm");
                exprValues.put(":defaultAlgorithm", AttributeValue.builder().s(fbpUser.getDefaultAlgorithm()).build());
                first = false;
            }
            if (fbpUser.getEmailPickSheet() != null) {
                updateExpr.append(first ? "" : ", ").append("#emailPickSheet = :emailPickSheet");
                exprNames.put("#emailPickSheet", "emailPickSheet");
                exprValues.put(":emailPickSheet", AttributeValue.builder().bool(fbpUser.getEmailPickSheet()).build());
                first = false;
            }
            if (fbpUser.getEmailReminders() != null) {
                updateExpr.append(first ? "" : ", ").append("#emailReminders = :emailReminders");
                exprNames.put("#emailReminders", "emailReminders");
                exprValues.put(":emailReminders", AttributeValue.builder().bool(fbpUser.getEmailReminders()).build());
                first = false;
            }
            if (fbpUser.getEmailGridSheet() != null) {
                updateExpr.append(first ? "" : ", ").append("#emailGridSheet = :emailGridSheet");
                exprNames.put("#emailGridSheet", "emailGridSheet");
                exprValues.put(":emailGridSheet", AttributeValue.builder().bool(fbpUser.getEmailGridSheet()).build());
                first = false;
            }
            // Admin-only fields — only written when explicitly provided (e.g., during
            // new-user creation by an admin)
            if (fbpUser.getIsAdmin() != null) {
                updateExpr.append(first ? "" : ", ").append("#isAdmin = :isAdmin");
                exprNames.put("#isAdmin", "isAdmin");
                exprValues.put(":isAdmin", AttributeValue.builder().bool(fbpUser.getIsAdmin()).build());
                first = false;
            }
            if (fbpUser.getIsAccountLocked() != null) {
                updateExpr.append(first ? "" : ", ").append("#isAccountLocked = :isAccountLocked");
                exprNames.put("#isAccountLocked", "isAccountLocked");
                exprValues.put(":isAccountLocked", AttributeValue.builder().bool(fbpUser.getIsAccountLocked()).build());
                first = false;
            }
            if (fbpUser.getIsPaidUser() != null) {
                updateExpr.append(first ? "" : ", ").append("#isPaidUser = :isPaidUser");
                exprNames.put("#isPaidUser", "isPaidUser");
                exprValues.put(":isPaidUser", AttributeValue.builder().bool(fbpUser.getIsPaidUser()).build());
                first = false;
            }

            if (first) {
                logAction(fbpUser.getEmail(), "Add/Update User", "No updatable fields provided for user: " + fbpUser.getEmail() + ". Request body may be missing or malformed.",
                        "WARN");
                return new APIGatewayProxyResponseEvent().withStatusCode(400)
                        .withHeaders(Map.of(
                                "Access-Control-Allow-Origin", "*",
                                "Access-Control-Allow-Headers",
                                "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                                "Access-Control-Allow-Methods", "POST,OPTIONS"))
                        .withBody("No updatable fields provided");
            }

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .updateExpression(updateExpr.toString())
                    .expressionAttributeNames(exprNames)
                    .expressionAttributeValues(exprValues)
                    .build();
            dynamoDB.updateItem(updateItemRequest);
            logAction(fbpUser.getEmail(), "Add/Update User", "User " + fbpUser.getDisplayName() + " new/updated config:" + body,
                    "INFO");
            return new APIGatewayProxyResponseEvent().withStatusCode(200)
                    .withHeaders(Map.of(
                            "Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers",
                            "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                            "Access-Control-Allow-Methods", "POST,OPTIONS"))
                    .withBody("User added successfully");
        } catch (Exception e) {
            logAction(fbpUser.getEmail(), "Add/Update User",
                    "Error adding/updating user: " + fbpUser.getDisplayName() + ". Exception: " + e.getMessage(), "ERROR");
            return new APIGatewayProxyResponseEvent().withStatusCode(500)
                    .withHeaders(Map.of(
                            "Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers",
                            "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                            "Access-Control-Allow-Methods", "POST,OPTIONS"))
                    .withBody("Error processing order: " + e.getMessage());
        }
    }

    private void logAction(String email, String action, String details, String level) {
        FBPLogAction logAction = new FBPLogAction();
        logAction.setEmail(email);
        logAction.setAction(action);
        logAction.setDetails(details);
        logAction.setLevel(level);
        String week = FBPUtils.getCurrentWeek().toString();
        logAction.setWeek(week);
        FBPUtils.logAction(logAction);
    }
}
