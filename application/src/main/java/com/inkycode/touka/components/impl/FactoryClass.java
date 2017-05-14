package com.inkycode.touka.components.impl;

import com.inkycode.touka.components.FactoryInterface;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;

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

    public String toString() {
        return String.format("\nMy name is: %s.\nMy Age is: %.0f", this.getName(), this.getAge());
    }

}