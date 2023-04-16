package com.inmar.api.json.masker;

import com.inmar.api.json.domain.JsonSupportedTypePatternMask;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonSensitiveFieldsMasker implements Masker {

    private final JsonSupportedTypePatternMask jsonSupportedTypePatternMask;

    public JsonSensitiveFieldsMasker(JsonSupportedTypePatternMask jsonSupportedTypePatternMask) {
        this.jsonSupportedTypePatternMask = jsonSupportedTypePatternMask;
    }

    @Override
    public Message handleMessage(Message message) {
        String result = message.getFormattedMessage();
        for (Pair<Pattern, String> pair : jsonSupportedTypePatternMask.values()) {
            result = handleMessage(result, pair);
        }
        return new FormattedMessage(result, message.getParameters(), message.getThrowable());
    }

    private String handleMessage(String message, Pair<Pattern, String> pair) {
        Matcher matcher = pair.pattern.matcher(message);
        if (matcher.find()) {
            return matcher.replaceAll(pair.mask);
        }
        return message;
    }

    public static class Pair<C1, C2> {

        private final C1 pattern;
        private final C2 mask;

        public Pair(C1 pattern, C2 mask) {
            this.pattern = pattern;
            this.mask = mask;
        }

        @Override
        public String toString() {
            return String.format("Pair\\{pattern=%s, mask=%s}", pattern, mask);
        }

        public C1 pattern() {
            return pattern;
        }

        public C2 mask() {
            return mask;
        }

    }

}
