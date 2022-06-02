package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

public class RoomLockerModel {
    private String roomName;
    private List<LockerModel> lockers = new ArrayList<>();

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<LockerModel> getLockers() {
        return lockers;
    }

    public void setLockers(List<LockerModel> lockers) {
        this.lockers = lockers;
    }
}
