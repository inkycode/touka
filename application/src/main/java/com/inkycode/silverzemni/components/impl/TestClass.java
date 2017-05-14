package com.inkycode.silverzemni.components.impl;

import java.util.List;
import java.util.Map;

import com.inkycode.silverzemni.components.FactoryInterface;
import com.inkycode.silverzemni.components.InjectedInterface;
import com.inkycode.silverzemni.components.TestInterface;
import com.inkycode.silverzemni.core.bootstrap.annotations.Activate;
import com.inkycode.silverzemni.core.bootstrap.annotations.Inject;
import com.inkycode.silverzemni.core.bootstrap.annotations.Source;

public class TestClass implements TestInterface {

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
        System.out.println("stringProperty = " + this.stringProperty);
        System.out.println("multipleStringProperty = " + this.multipleStringProperty);
        System.out.println("injectedInstance = " + this.injectedInstance.getName());
        System.out.println("factory = " + this.factory);
    }

}
