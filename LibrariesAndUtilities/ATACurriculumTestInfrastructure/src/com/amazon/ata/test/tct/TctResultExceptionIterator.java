package com.amazon.ata.test.tct;

import java.util.Iterator;

/**
 * An iterator that throws a given exception for any method requested. This is used by testng's DataProvider. If our
 * data provider throws an Exception the test will be skipped. Instead we wrap our exception in this iterator and throw
 * it once our test has received the data from the DataProvider.
 */
public class TctResultExceptionIterator implements Iterator<Object[]> {
    private Throwable throwable;

    public TctResultExceptionIterator(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public boolean hasNext() {
        throw new RuntimeException(throwable);
    }

    @Override
    public Object[] next() {
        throw new RuntimeException(throwable);
    }

    @Override
    public void remove() {
        throw new RuntimeException(throwable);
    }
}
