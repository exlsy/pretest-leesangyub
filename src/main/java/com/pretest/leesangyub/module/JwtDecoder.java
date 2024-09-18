package com.pretest.leesangyub.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtDecoder {


    private final ObjectMapper objectMapper;

    public JwtDecoder() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Map<String, Object> decode(String jwtToken) throws JsonProcessingException {
        Map<String,Object> decMap = new HashMap<>();
        String[] chunks = jwtToken.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        // String header = new String(decoder.decode(chunks[0]), StandardCharsets.UTF_8);
        String payload = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8);

        // ObjectMapper mapper = new ObjectMapper();
        decMap = objectMapper.readValue(payload, Map.class);

        return decMap;
    }

}