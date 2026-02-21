package com.fbp;

import java.security.Key;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class LambdaGetMethod {
    public APIGatewayProxyResponseEvent functionName(APIGatewayProxyRequestEvent request)
            throws JsonMappingException, Exception {
        String tableName= System.getenv("FBPUsers");
        String queryParam= request.getQueryStringParameters().get("Week");
        DynamoDbClient dynamoDB = DynamoDbClient.builder().build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient
                .builder()
                .dynamoDbClient(dynamoDB)
                .build();
        DynamoDbTable<FBPPickSheet> table = enhancedClient.table(tableName, TableSchema.fromBean(FBPPickSheet.class));
        String week = request.getQueryStringParameters().get(queryParam);
        FBPPickSheet fbpUser=table.getItem(Key.builder().partitionValue(week).build());
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                    .withHeaders(Map.of(
                        "Access-Control-Allow-Origin", "https://my-fbp.com",
                        "Content-Type", "application/json"))
                        .withBody(new ObjectMapper().writeValueAsString(fbpUser));
    }
}
