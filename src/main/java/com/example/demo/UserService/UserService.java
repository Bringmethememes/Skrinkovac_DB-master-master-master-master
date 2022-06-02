package com.example.demo.UserService;

import com.example.demo.domain.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Locker saveLocker(Locker locker);
    Locker getLockerByNumber(Integer number);
    Locker getLockerById(Long id);
    List<Locker>getLockers();
    List<Locker>getLockersByRoom(String name);

    Locker updateLocker(Long id, Locker locker);

    Room saveRoom(Room room);

    List<RoomModel> getOnlyRooms();

    List<Room>getRooms();

    void deleteLocker(Long id);

    Role addRole(Role role);

    void addLockerToRoom(String roomName, Integer number);

    Student findStudentByName(String Name);
    Student findStudentById(Long id);
    List<Student>getStudents();
    Student saveStudent(Student student);

    Room updateRoom(Long id, Room room);
    Room findRoomByName(String name);

    Student updateStudent(Long id, Student student);
    Student findStudentByLocker(Integer number);

    void deleteRoom(Long id);

    void deleteStudent(Long id);

    void addStudentToLocker(String student,Integer number,String room);
    void removeStudentFromLocker(Integer number);
}
