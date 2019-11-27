package com.gpnk.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Utility class to load HOCON TypeSafe configs. Loads environment-specific values
 * for the current environment. Environment is defined by a property called {@code env} which defaults to "dev".
 * The value for {@code env} can be specified via:
 * - TypeSafe config file. Note that the included {@code reference.conf} file sets the {@code env}
 *   to {@code dev}.
 * - Passed as a VM option to Java, for example '-Denv=staging'.
 * - Defined as an environment variable ENV on the current system, for example 'export ENV=staging'.
 *   This option is enabled via environment variable override for {@code env} in reference.conf file.
 *
 *   <p>ConfigUtil.load() method loads environment-specific values from application.conf with fallback to reference.conf.
 *
 *   <p>ConfigUtil.load(String configName) loads environment-specific values from {@code configName} with fallback to
 *   application.conf with fallback to reference.conf.
 *
 *   <p>ConfigUtil supports an {@code override} block which overrides values in any environment.
 *
 *   <p>For example:
 *   {
 *     flower: daisy
 *     herb: basil
 *     veg: tomato
 *     berry: blackberry
 *     dev {
 *     }
 *     staging {
 *       flower: pansy
 *       herb: dill
 *     }
 *     overrides {
 *       herb: oregano
 *       berry: ${?BERRY}
 *     }
 *   }
 *
 *   <p>For the above config:
 *   In dev:
 *      flower = daisy
 *      herb = oregano
 *      veg = tomato
 *      berry = value of the BERRY environment variable if it is set, "blackberry" otherwise
 *   In staging:
 *      flower = pansy
 *      herb = oregano
 *      veg = tomato
 *      berry = value of the BERRY environment variable if it is set, "blackberry" otherwise
 */
public final class ConfigUtil {

    public static final String ENV = "env";
    public static final String OVERRIDES = "overrides";

    /**
     * Making constructor private to prevent other classes from instantiating this "utility" class.
     */
    private ConfigUtil() {
    }

    /**
     * @param configName The name of the config file to load first, for example, helloworld-reference.conf.
     * @return Config with environment-specific values and overrides from {@code configName} with fallback to
     *      application.conf with fallback to reference.conf.
     */
    public static Config load(String configName) {
        Config fullConfig = ConfigFactory.load(configName).withFallback(ConfigFactory.load());
        return load(fullConfig);
    }

    /**
     * @return Config with environment-specific values and overrides from application.conf with fallback to
     *      reference.conf.
     */
    public static Config load() {
        return load(ConfigFactory.load());
    }

    /**
     * @param fullConfig Full configuration object.
     * @return Config with with environment-specific values and overrides from {@code fullConfig}.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public static Config load(Config fullConfig) {
        Config currentConfig = fullConfig;
        // which environment are we in?
        String currentEnv = fullConfig.getString(ENV);
        // use values for our current environment where specified
        if (fullConfig.hasPath(currentEnv)) {
            currentConfig = fullConfig.getConfig(currentEnv).withFallback(fullConfig);
        }

        // use specified overrides
        if (fullConfig.hasPath(OVERRIDES)) {
            currentConfig = fullConfig.getConfig(OVERRIDES).withFallback(currentConfig);
        }

        return currentConfig;
    }

}
