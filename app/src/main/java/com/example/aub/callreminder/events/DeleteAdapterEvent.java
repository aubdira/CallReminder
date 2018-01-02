package com.example.aub.callreminder.events;

/**
 * Created by aub on 1/1/18.
 * Project: CallReminder
 */

public class DeleteAdapterEvent {

    private int id;
    private int size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
