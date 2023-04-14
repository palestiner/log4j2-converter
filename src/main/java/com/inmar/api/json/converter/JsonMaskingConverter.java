package com.inmar.api.json.converter;

import com.inmar.api.json.domain.JsonField;
import com.inmar.api.json.domain.JsonSupportedType;
import com.inmar.api.json.domain.JsonSupportedTypePatternMask;
import com.inmar.api.json.masker.JsonSensitiveFieldsMasker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Plugin(name = "JsonMaskingConverter", category = "Core", elementType = "rewritePolicy", printObject = true)
public class JsonMaskingConverter implements RewritePolicy {

    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private final JsonSensitiveFieldsMasker jsonSensitiveFieldsMasker;

    public JsonMaskingConverter(JsonSensitiveFieldsMasker jsonSensitiveFieldsMasker) {
        this.jsonSensitiveFieldsMasker = jsonSensitiveFieldsMasker;
    }

    @PluginFactory
    public static JsonMaskingConverter createPolicy(
            @PluginElement("JsonFields") @Required final JsonField[] jsonFields
    ) {
        List<JsonField> filteredFields = Stream.of(jsonFields)
                .filter(jsonField -> {
                    boolean contains = JsonSupportedType.ALL_VALUES.contains(jsonField.type());
                    if (!contains) LOGGER.info("{} field type don't supported", jsonField.type());
                    return contains;
                })
                .collect(Collectors.toList());
        return new JsonMaskingConverter(
                new JsonSensitiveFieldsMasker(
                        new JsonSupportedTypePatternMask(
                                new HashMap<>(),
                                filteredFields
                        )));
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        Message outputMessage = convertMessage(event.getMessage());
        return new Log4jLogEvent.Builder(event).setMessage(outputMessage).build();
    }

    private Message convertMessage(Message message) {
        String handledMessage = jsonSensitiveFieldsMasker.handleMessage(message.getFormattedMessage());
        return new FormattedMessage(handledMessage, message.getParameters(), message.getThrowable());
    }

}
