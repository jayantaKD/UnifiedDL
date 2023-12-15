package org.infobeyondtech.unifieddl.core;

import java.util.concurrent.Callable;

public class test implements Callable<String> {
    @Override
    public String call() throws Exception {

        Thread.sleep(2000);
        System.out.print("testing");
        Thread.sleep(2000);
        System.out.print("testing1");

        return "test";
    }
}
