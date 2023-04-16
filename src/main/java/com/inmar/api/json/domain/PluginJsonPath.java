package com.inmar.api.json.domain;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "JsonPath", category = Node.CATEGORY, printObject = true)
public class PluginJsonPath {

    private final String value;

    public PluginJsonPath(String value) {
        this.value = value;
    }

    @PluginFactory
    public static PluginJsonPath getInstance(
            @PluginValue("value")
            @Required(message = "Value is required for JsonPath")
            final String value
    ) {
        return new PluginJsonPath(value);
    }

    public String value() {
        return value;
    }

}
