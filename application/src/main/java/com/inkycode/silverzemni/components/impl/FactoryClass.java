package com.inkycode.silverzemni.components.impl;

import com.inkycode.silverzemni.components.FactoryInterface;
import com.inkycode.silverzemni.core.bootstrap.annotations.Inject;
import com.inkycode.silverzemni.core.bootstrap.annotations.Source;

public class FactoryClass implements FactoryInterface {

    @Inject
    @Source("property")
    private String name;

    @Inject
    @Source("property")
    private double age;

    public String getName() {
        return this.name;
    }

    public double getAge() {
        return this.age;
    }

}