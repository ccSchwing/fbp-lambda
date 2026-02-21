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

public class GetPickSheet {
    public APIGatewayProxyResponseEvent functionName(APIGatewayProxyRequestEvent request)
            throws JsonMappingException, Exception {
        String tableName= System.getenv("YOUR_TABLE_NAME_ENV_VARIABLE");
        String queryParam= request.getQueryStringParameters().get("YOUR_QUERY_PARAM_KEY");
        DynamoDbClient dynamoDB = DynamoDbClient.builder().build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient
                .builder()
                .dynamoDbClient(dynamoDB)
                .build();
        DynamoDbTable<FBPUserBean> table = enhancedClient.table(tableName, TableSchema.fromBean(FBPUserBean.class));
        String email = request.getQueryStringParameters().get(queryParam);
        FBPUserBean fbpUser=table.getItem(Key.builder().partitionValue(email).build());
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                    .withHeaders(Map.of(
                        "Access-Control-Allow-Origin", "https://my-fbp.com",
                        "Content-Type", "application/json"))
                        .withBody(new ObjectMapper().writeValueAsString(fbpUser));
    }
}
