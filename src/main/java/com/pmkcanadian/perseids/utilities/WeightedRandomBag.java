package com.pmkcanadian.perseids.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomBag<T> {
    private class Entry {
        double accumulatedWeight;

        T object;

        private Entry() {}
    }

    public List<Entry> entries = new ArrayList<>();

    private double accumulatedWeight;

    private Random rand = new Random();

    public void addEntry(T object, double weight) {
        this.accumulatedWeight += weight;
        Entry e = new Entry();
        e.object = object;
        e.accumulatedWeight = this.accumulatedWeight;
        this.entries.add(e);
    }

    public T getRandom() {
        double r = this.rand.nextDouble() * this.accumulatedWeight;
        for (Entry entry : this.entries) {
            if (entry.accumulatedWeight >= r)
                return entry.object;
        }
        return null;
    }
}