package com.fbp;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOrderLambda {
    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent request)
        throws JsonMappingException, JsonProcessingException {
        try{ 
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(request.getBody(), Order.class);
        String responseMessage = String.format("Order created: ID=%d, Item=%s, Quantity=%d", order.id, order.itemName, order.quantity);
        return new APIGatewayProxyResponseEvent().withStatusCode(200)
            .withHeaders(Map.of(
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                "Access-Control-Allow-Methods", "POST,OPTIONS"
            ))
            .withBody(responseMessage);
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
