package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

public class RoomModel {

    private String roomName;
    private List<Locker> lockers = new ArrayList<>();

    public List<Locker> getLockers() {
        return lockers;
    }

    public void setLockers(List<Locker> lockers) {
        this.lockers = lockers;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
