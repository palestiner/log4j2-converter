package com.inmar.api.json.domain;

import com.inmar.api.json.masker.JsonSensitiveFieldsMasker.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonSupportedTypePatternMask {

    private final Map<String, Pair<String, String>> supportedFieldTypesPatternMap;
    private final List<JsonField> properties;

    private static final String MAIN_MASK = "\"$1\": %s";
    private static ConcurrentMap<String, Pair<Pattern, String>> fieldNamePatternMap;

    public JsonSupportedTypePatternMask(
            Map<String, Pair<String, String>> supportedFieldTypesPatternMap,
            List<JsonField> properties
    ) {
        this.supportedFieldTypesPatternMap = supportedFieldTypesPatternMap;
        this.properties = properties;
        if (supportedFieldTypesPatternMap == null) {
            supportedFieldTypesPatternMap = new HashMap<>();
        }
        if (supportedFieldTypesPatternMap.isEmpty()) {

            supportedFieldTypesPatternMap.put(
                    JsonSupportedType.STRING.value,
                    new Pair<>("\"(%s)\"\\s*:\\s*\".*\"", MAIN_MASK)
            );
            supportedFieldTypesPatternMap.put(
                    JsonSupportedType.ARRAY.value,
                    new Pair<>("\"(%s)\"\\s*:\\s*\\[[\\w\\W\\r\\n]*\\]", MAIN_MASK)
            );
            supportedFieldTypesPatternMap.put(
                    JsonSupportedType.OBJECT.value,
                    new Pair<>("\"(%s)\"\\s*:\\s*\\{[\\w\\W\\r\\n]*\\},", MAIN_MASK + ',')
            );
        }
        fieldNamePatternMap = setupFieldNamePatternMap();
    }

    private ConcurrentMap<String, Pair<Pattern, String>> setupFieldNamePatternMap() {
        return properties.stream()
                .collect(Collectors.toMap(
                        JsonField::name,
                        jsonField -> {
                            Pair<String, String> pair = supportedFieldTypesPatternMap.get(jsonField.type());
                            return new Pair<>(
                                    Pattern.compile(String.format(pair.pattern(), jsonField.name())),
                                    String.format(pair.mask(), jsonField.mask())
                            );
                        },
                        (pair, pair2) -> pair2,
                        ConcurrentHashMap::new
                ));
    }

    public Collection<Pair<Pattern, String>> values() {
        return fieldNamePatternMap.values();
    }

}
