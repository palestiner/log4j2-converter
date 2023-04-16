package com.inmar.api.json.converter;

import com.inmar.api.json.domain.PluginJsonPath;
import com.inmar.api.json.masker.JsonMasker;
import com.inmar.api.json.masker.Masker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "JsonMaskingConverter", category = "Core", elementType = "rewritePolicy", printObject = true)
public class JsonMaskingConverter implements RewritePolicy {

    private final Masker masker;

    public JsonMaskingConverter(Masker masker) {
        this.masker = masker;
    }

    @PluginFactory
    public static JsonMaskingConverter createPolicy(@PluginElement("JsonPaths") final PluginJsonPath[] jsonPaths) {
        return new JsonMaskingConverter(new JsonMasker(jsonPaths));
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        Message outputMessage = convertMessage(event.getMessage());
        return new Log4jLogEvent.Builder(event).setMessage(outputMessage).build();
    }

    private Message convertMessage(Message message) {
        return masker.handleMessage(message);
    }

}
