package com.example.demo.repo;

import com.example.demo.domain.Locker;
import com.example.demo.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Locker>, CrudRepository<Room,Long> {
    Room findByRoomName(String roomName);
}
