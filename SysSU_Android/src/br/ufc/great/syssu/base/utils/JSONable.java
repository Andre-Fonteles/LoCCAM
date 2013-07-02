package br.ufc.great.syssu.base.utils;

public interface JSONable<T> {
    T getObject();
    String getJSON();
}
