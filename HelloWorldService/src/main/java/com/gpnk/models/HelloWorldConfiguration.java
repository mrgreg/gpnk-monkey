package com.gpnk.models;

import io.dropwizard.Configuration;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class HelloWorldConfiguration extends Configuration {

    @Getter
    @NotEmpty
    private List<String> modules;

}
