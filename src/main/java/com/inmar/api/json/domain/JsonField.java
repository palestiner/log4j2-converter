package com.inmar.api.json.domain;

import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "JsonField", category = Node.CATEGORY, printObject = true)
public class JsonField {

    private final String name;
    private final String type;
    private final String mask;

    public JsonField(String name, String type, String mask) {

        this.name = name;
        this.type = type;
        this.mask = mask;
    }

    @PluginFactory
    public static JsonField getInstance(
            @PluginAttribute("name") @Required final String name,
            @PluginAttribute("type") @Required final String type,
            @PluginAttribute("mask") @Required final String mask
    ) {
        return new JsonField(name, type, mask);
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public String mask() {
        return mask;
    }

}
