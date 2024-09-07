package com.example.application;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;

@IncludeEngines("cucumber")
@SelectClasspathResource("com/example/application/")
public class RunCucumberTests {

}
