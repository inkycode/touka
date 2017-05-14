package com.inkycode.touka.components.impl;

import com.inkycode.touka.components.InjectedInterface;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;

public class InjectedClass implements InjectedInterface {

    @Inject
    @Source("property")
    private String name;
    
    public String getName() {
        return this.name;
    }

}
