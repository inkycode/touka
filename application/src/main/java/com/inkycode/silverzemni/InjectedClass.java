package com.inkycode.silverzemni;

import com.inkycode.silverzemni.core.bootstrap.annotations.Activate;
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
