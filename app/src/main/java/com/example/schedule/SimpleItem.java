package com.example.schedule;

import java.io.Serializable;

/**
 * Структура для представления информации об элементе справочника
 * @author Abrashov
 */
public class SimpleItem implements Serializable {

    public int id;
    public String name;

    @Override
    public String toString() {
        return name;
    }

    public SimpleItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
