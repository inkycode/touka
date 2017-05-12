package com.inkycode.silverzemni.components.impl;

import com.inkycode.silverzemni.components.InjectedInterface;
import com.inkycode.silverzemni.core.bootstrap.annotations.Inject;
import com.inkycode.silverzemni.core.bootstrap.annotations.Source;

public class InjectedClass implements InjectedInterface {

    @Inject
    @Source("property")
    private String name;
    
    public String getName() {
        return this.name;
    }

}
