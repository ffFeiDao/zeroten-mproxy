package com.zeroten.mproxy.tool;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Monitor;

import java.util.LinkedList;

public class ValueBox<V> {
    private final Monitor monitor = new Monitor();

    private final Monitor.Guard valuePresent = new Monitor.Guard(monitor) {
        @Override
        public boolean isSatisfied() {
            return !value.isEmpty();
        }
    };

    private final Monitor.Guard valueAbsent = new Monitor.Guard(monitor) {
        @Override
        public boolean isSatisfied() {
            return value.size() < 5;
        }
    };

    private LinkedList<V> value = Lists.newLinkedList();

    public V get() throws InterruptedException {
        monitor.enterWhen(valuePresent);
        try {
            return value.removeFirst();
        } finally {
            monitor.leave();
        }
    }

    public void add(V newValue) throws InterruptedException {
        monitor.enterWhen(valueAbsent);
        try {
            value.add(newValue);
        } finally {
            monitor.leave();
        }
    }

    public int size() {
        return value.size();
    }
}
