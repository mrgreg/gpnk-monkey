package com.gpnk.common;

/**
 * Allows an application to register custom startup logic that is specific to the application
 */
public interface ApplicationStartupHook {

    /**
     * Runs as the last step of application startup
     */
    void onStartup();
}
