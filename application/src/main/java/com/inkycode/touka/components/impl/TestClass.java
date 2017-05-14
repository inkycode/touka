package com.inkycode.touka.components.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inkycode.touka.components.FactoryInterface;
import com.inkycode.touka.components.InjectedInterface;
import com.inkycode.touka.components.TestInterface;
import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;

public class TestClass implements TestInterface {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("property")
    private String stringProperty;

    @Inject
    @Source("property")
    private List<String> multipleStringProperty;

    @Inject
    @Source("component")
    private InjectedInterface injectedInstance;

    @Inject
    @Source("component")
    private Map<String, FactoryInterface> factory;

    @Activate
    public void activate() {
        this.log.info("stringProperty = " + this.stringProperty);
        this.log.info("multipleStringProperty = " + this.multipleStringProperty);
        this.log.info("injectedInstance = " + this.injectedInstance.getName());

        for (FactoryInterface factoryInstance : factory.values()) {
            this.log.info("Something = {}", factoryInstance);
        }
    }

}
