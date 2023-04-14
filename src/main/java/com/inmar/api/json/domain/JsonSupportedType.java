package com.inmar.api.json.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum JsonSupportedType {
    STRING("string"),
    ARRAY("array"),
    OBJECT("object");

    public final String value;

    JsonSupportedType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static final Set<String> ALL_VALUES = Arrays.stream(JsonSupportedType.values())
            .map(JsonSupportedType::getValue)
            .collect(Collectors.toSet());
}
