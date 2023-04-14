# Log4j2 Converter

## Description
This application helps to add a simple json masking rewriter for any appender

##
Usage

1. Using Java 11 exec ```mvn clean install```
2. Add dependency to project
```xml
<dependency>
    <groupId>com.inmar</groupId>
    <artifactId>log4j2-converter</artifactId>
    <version>1.0.0</version>
</dependency>
```
3. Add ```com.inmar.api.json``` package to ```Configuration``` node in ```log4j2.xml```
4. Add ```Rewriter```
5. Add ```JsonMaskingConverter``` to ```Rewriter```
6. ```JsonMaskingConverter``` must contain at least one ```JsonField```
7. ```JsonField``` must contain ```type```, ```name``` and ```mask``` attributes
7. Set this ```Rewriter``` to ```Root``` like appender

### Full config example

```xml
<?xml version="1.0" encoding="utf-8"?>
<Configuration packages="com.inmar.api.json">
    <Appenders>
        <Console name="STDOUT">
            <PatternLayout pattern="[%-5level] %c{1.} %m%n"/>
        </Console>
        <Rewrite name="REWRITE">
            <AppenderRef ref="STDOUT"/>
            <JsonMaskingConverter>
                <JsonField type="string"
                           name="field1"
                           mask="&quot;**********&quot;"/>
                <JsonField type="object"
                           name="field2"
                           mask="{**********}"/>
            </JsonMaskingConverter>
        </Rewrite>
    </Appenders>

    <Loggers>
        <AsyncRoot level="INFO">
            <AppenderRef ref="REWRITE"/>
        </AsyncRoot>
    </Loggers>

</Configuration>
```
