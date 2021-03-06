package io.github.leberkleber.ljcm;

import io.github.leberkleber.ljcm.configuration.ConfigDummyImpl;
import io.github.leberkleber.ljcm.exception.NoResponsibleParserFoundException;
import io.github.leberkleber.ljcm.parser.ConfigurationParser;
import io.github.leberkleber.ljcm.parser.ConfigurationParserDummyImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ConfigurationProcessorTest {
    @Test
    public void process() throws Exception {
        Class parserResponsibility = String.class;
        Properties properties = new Properties();
        ConfigurationParser configurationParser = new ConfigurationParserDummyImpl(parserResponsibility);
        Map<Class, ConfigurationParser> configurationParserMap = new HashMap<>();

        properties.put("test.exists", "MyCustomValue");
        configurationParserMap.put(parserResponsibility, configurationParser);

        ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(properties, configurationParserMap);
        ConfigDummyImpl cfg = configurationProcessor.process(ConfigDummyImpl.class);

        assertNotNull(cfg);
        assertEquals(cfg.getTestExists(), "MyCustomValue");
        assertEquals(cfg.getTestWithDefault(), "defaultV");
        assertEquals(cfg.getTestWithoutAnnotation(), "default");
        assertEquals(cfg.getTestDoseNotExists(), null);
    }


    @Test(expected = NoResponsibleParserFoundException.class)
    public void processNoMappingParserFound() throws Exception {
        Class parserResponsibility = Enum.class;
        Properties properties = new Properties();
        ConfigurationParser configurationParser = new ConfigurationParserDummyImpl(parserResponsibility);
        Map<Class, ConfigurationParser> configurationParserMap = new HashMap<>();

        properties.put("test.exists", "MyCustomValue");
        configurationParserMap.put(parserResponsibility, configurationParser);



        ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(properties, configurationParserMap);
        ConfigDummyImpl cfg = configurationProcessor.process(ConfigDummyImpl.class);

        assertNotNull(cfg);
        assertEquals(cfg.getTestExists(), "MyCustomValue");
        assertEquals(cfg.getTestWithDefault(), "defaultV");
        assertEquals(cfg.getTestWithoutAnnotation(), "default");
        assertEquals(cfg.getTestDoseNotExists(), null);
    }


    @Test(expected = NullPointerException.class)
    public void constructWithConfigurationsNull() {
        new ConfigurationProcessor(null, new HashMap<>());
    }


    @Test(expected = NullPointerException.class)
    public void constructWithConfigurationParsersNull() {
        new ConfigurationProcessor(new Properties(), null);
    }
}
