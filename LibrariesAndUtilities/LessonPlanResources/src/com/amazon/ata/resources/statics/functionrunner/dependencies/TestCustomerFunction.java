package com.amazon.ata.resources.statics.functionrunner.dependencies;

import java.util.Random;

public class TestCustomerFunction implements CustomerFunction {
    @Override
    public void run() {
        System.out.println("Running customer function!");
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            System.out.println("Hm. we were interrupted while sleeping--er, computing something--on the job.");
        }
    }
}
