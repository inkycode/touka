package com.inkycode.silverzemni;

import java.util.List;

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

    @Activate
    public void activate() {
        System.out.println("stringProperty = " + this.stringProperty);
        System.out.println("multipleStringProperty = " + this.multipleStringProperty);
        System.out.println("injectedInstance = " + this.injectedInstance.getName());
    }

}
