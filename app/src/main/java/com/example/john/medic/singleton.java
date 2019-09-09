package com.example.john.medic;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by john on 3/14/2018.
 */

class singleton implements Map<String,List<String>>{
    private static final singleton ourInstance = new singleton();

    static singleton getInstance() {
        return ourInstance;
    }

    private singleton() {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public List<String> get(Object o) {
        return null;
    }

    @Override
    public List<String> put(String s, List<String> strings) {
        return null;
    }

    @Override
    public List<String> remove(Object o) {
        return null;
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends List<String>> map) {

    }

    @Override
    public void clear() {

    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return null;
    }

    @NonNull
    @Override
    public Collection<List<String>> values() {
        return null;
    }

    @NonNull
    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return null;
    }
}
