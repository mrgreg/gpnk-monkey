package com.gpnk.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for loading TypeSafe configs in ConfigUtil class.
 */
public class ConfigUtilTest {

    @Test
    void testLoad() {
        Config config = ConfigUtil.load();
        assertThat(config.getString("env")).isEqualTo("dev");
        assertThat(config.getString("flower")).isEqualTo("daisy");
        assertThat(config.getString("herb")).isEqualTo("oregano");
        assertThat(config.getString("veg")).isEqualTo("tomato");
        assertThat(config.getString("berry")).isEqualTo("blackberry");
    }

    @Test
    void testLoadStagingEnv() {
        Config config = ConfigUtil.load("staging-reference");
        assertThat(config.getString("env")).isEqualTo("staging");
        assertThat(config.getString("tree")).isEqualTo("apple");
        assertThat(config.getString("flower")).isEqualTo("pansy");
        assertThat(config.getString("herb")).isEqualTo("oregano");
        assertThat(config.getString("veg")).isEqualTo("tomato");
        assertThat(config.getString("berry")).isEqualTo("blackberry");
    }

    @Test
    void testLoadWithConfig() {
        Map<String, String> envMap = new HashMap<>();
        envMap.put("env", "staging");
        Config envConfig = ConfigFactory.parseMap(envMap);
        Config rawConfig = ConfigFactory.load().withValue("env", envConfig.getValue("env"));
        Config config = ConfigUtil.load(rawConfig);

        assertThat(config.getString("env")).isEqualTo("staging");
        assertThat(config.getString("flower")).isEqualTo("pansy");
        assertThat(config.getString("herb")).isEqualTo("oregano");
        assertThat(config.getString("veg")).isEqualTo("tomato");
        assertThat(config.getString("berry")).isEqualTo("blackberry");
    }

}
