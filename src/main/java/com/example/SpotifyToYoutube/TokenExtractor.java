package com.example.SpotifyToYoutube;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {

    public String extractToken(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            // Assuming your JSON structure is like {"token": "your_token_value"}
            JsonNode tokenNode = jsonNode.get("access_token");

            if (tokenNode != null) {
                String tokenValue = tokenNode.asText();
                System.out.println("Token: " + tokenValue);
                return tokenValue;
            } else {
                System.err.println("Token not found in JSON response");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
