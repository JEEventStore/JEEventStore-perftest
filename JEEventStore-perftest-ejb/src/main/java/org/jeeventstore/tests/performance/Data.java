package org.jeeventstore.tests.performance;

import java.io.Serializable;

public class Data implements Serializable {

    public Data() { }
    public Data(String data) {
        this.data = data;
    }
    public String data;
    
}
