package com.inmar.api.json.masker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmar.api.json.domain.PluginJsonPath;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonMasker implements Masker {

    private static final Configuration JSON_PATH_CONFIG = Configuration.builder()
            .options(Option.AS_PATH_LIST, Option.SUPPRESS_EXCEPTIONS)
            .build();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    private final Set<JsonPath> jsonPaths;

    public JsonMasker(PluginJsonPath[] jsonPaths) {
        this.jsonPaths = Stream.of(jsonPaths)
                .map(PluginJsonPath::value)
                .map(JsonPath::compile)
                .filter(JsonPath::isDefinite)
                .collect(Collectors.toSet());
    }

    @Override
    public Message handleMessage(Message message) {
        String json = message.getFormattedMessage();
        DocumentContext ctx = JsonPath.parse(json, JSON_PATH_CONFIG);
        if (ctx.json() instanceof String) {
            return newFormattedMessage(message, json);
        }
        for (JsonPath jsonPath : jsonPaths) {
            ctx.set(jsonPath, "*************");
        }
        String result = ctx.jsonString();
        try {
            result = MAPPER.writer()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(MAPPER.readValue(result, new TypeReference<Map<String, Object>>() {}));
        } catch (JsonProcessingException e) {
            LOGGER.info("Can't write result pretty");
        }
        return newFormattedMessage(message, result);
    }

    private static FormattedMessage newFormattedMessage(Message message, String json) {
        return new FormattedMessage(json, message.getParameters(), message.getThrowable());
    }

}
