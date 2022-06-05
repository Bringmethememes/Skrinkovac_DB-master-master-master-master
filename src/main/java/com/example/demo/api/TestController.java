package com.example.demo.api;

import com.example.demo.UserService.UserService;
import com.example.demo.domain.*;
import com.example.demo.payload.forms.AddStudentToLocker;
import com.example.demo.payload.forms.FindStudent;
import com.example.demo.payload.forms.LockerToRoomForm;
import com.example.demo.payload.forms.StudentDetails;
import com.example.demo.repo.LockerRepo;
import com.example.demo.repo.RoomRepo;
import com.example.demo.repo.StudentRepo;
import com.sipios.springsearch.anotation.SearchSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class TestController {

    private final UserService userService;
    private final StudentRepo studentRepo;
    private final LockerRepo lockerRepo;
    private final RoomRepo roomRepo;

    public TestController(UserService userService, StudentRepo studentRepo, LockerRepo lockerRepo,RoomRepo roomRepo) {
        this.userService = userService;
        this.studentRepo = studentRepo;
        this.lockerRepo = lockerRepo;
        this.roomRepo = roomRepo;
    }


    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    /*Rooms*/

    @GetMapping("/rooms")
    public HashMap<String, RoomModel>getRooms(){
        HashMap<String ,RoomModel> rooms = new HashMap<>();
        List<RoomModel> list = userService.getOnlyRooms();
        for (RoomModel room: list) {
            rooms.put(room.getRoomName(),room);
        }
        return rooms;
    }
    @GetMapping("/roomsAll")
    public HashMap<String, Room>getRoomsAll(){
        HashMap<String ,Room> rooms = new HashMap<>();
        List<Room> list = userService.getRooms();
        for (Room room: list) {
            rooms.put(room.getRoomName(),room);
        }
        return rooms;
    }

    @GetMapping("/findRoom")
    public ResponseEntity<List<Room>>searchForRoom(@SearchSpec Specification<Locker> specification){
        return new ResponseEntity(roomRepo.findAll(Specification.where(specification)), HttpStatus.OK);
    }

    @PostMapping("/room/lockers")
    public HashMap<Integer,Locker>getRoomLocker(@Valid @RequestBody String Name){
        HashMap<Integer,Locker> lockers = new HashMap<>();
        Room room = userService.findRoomByName(Name);
        for (Locker locker : room.getLockers()){
            lockers.put(locker.getNumber(),locker);
        }
        return lockers;
    }
    @PostMapping("/nextGrade")
    public void nextGrade(@RequestParam List<Integer> list){
        List<Student> students = userService.getStudents();
        for (Student student:students) {
            if (list.contains(student.getId())){

            }
            else {
                Integer grade = student.getGrade();
                grade++;
                student.setGrade(grade);
                userService.updateStudent(student.getId(), student);
            }
        }


    }

    @PostMapping("/add/room")
    public void saveRoom(@RequestBody Room room){
        List<Locker> list = room.getLockers();
        userService.saveRoom(room);
        for (Locker locker: list) {
            userService.saveLocker(locker);
            userService.addLockerToRoom(room.getRoomName(),locker.getNumber());
            if (locker.getStudent() == null){
                userService.saveStudent(null);
            }
            else {
                Student student = locker.getStudent();
                userService.saveStudent(student);
            }
        }

    }

    @PutMapping("/edit/room/{id}")
    public ResponseEntity<Room>updateRoom(@PathVariable(value = "id") Long roomId, @Valid @RequestBody Room roomDetails) throws ResourceNotFoundException {
        return new ResponseEntity(userService.updateRoom(roomId,roomDetails), HttpStatus.OK);
    }

    @DeleteMapping("/delete/room/{id}")
    public void deleteRoom(@PathVariable(value = "id") Long roomId){
        userService.deleteRoom(roomId);
    }

    /*Lockers*/

    @PostMapping("/get/student")
    public ResponseEntity<Student> requestStudent(@RequestBody FindStudent findStudent){
        Room room = userService.findRoomByName(findStudent.getRoomName());
        List<Locker> list = room.getLockers();
        Locker mainLocker = new Locker();
        for (Locker locker:list){
            if (locker.getNumber() == findStudent.getNumber()){
                mainLocker = locker;
            }
            else {

            }
        }
        if (mainLocker.getStudent() == null){
            return ResponseEntity.ok().body(new Student());
        }
        else {
            return ResponseEntity.ok().body(mainLocker.getStudent());
        }
    }
    @PostMapping("/add/studentToLocker")
    public void addStudentToLocker(@RequestBody AddStudentToLocker form){
        userService.addStudentToLocker(form.getStudentName(), form.getNumber(), form.getRoomName());

    }

    @GetMapping("/lockers")
    public HashMap<Integer, Locker>getLockers(){
        HashMap<Integer ,Locker> lockers = new HashMap<>();
        List<Locker> list= userService.getLockers();
        for (Locker locker: list) {
            lockers.put(locker.getNumber(),locker);
        }
        return lockers;
    }
    @GetMapping("/lockersByRoom/{Hala}")
    public HashMap<Integer,Locker>getRoomLockers(@PathVariable(value = "Hala")String name) throws ResourceNotFoundException{
        HashMap<Integer ,Locker> lockers = new HashMap<>();
            List<Locker> list = userService.getLockersByRoom(name);
            for (Locker locker: list) {
                lockers.put(locker.getNumber(),locker);
            }
            return lockers;

    }

    @GetMapping("/findLocker")
    public ResponseEntity<List<Locker>>searchForLocker(@SearchSpec Specification<Locker> specification){
        return new ResponseEntity<>(lockerRepo.findAll(Specification.where(specification)), HttpStatus.OK);
    }

    @PostMapping("/add/locker")
    public ResponseEntity<Locker>saveLocker(@RequestBody Locker locker){
        return ResponseEntity.ok().body(userService.saveLocker(locker));
    }
    @PostMapping("/add/lockerToRoom")
    public void lockerToRoom(@RequestBody LockerToRoomForm form){
        userService.addLockerToRoom(form.getName(), form.getNumber());
    }

    @PutMapping("/edit/locker/{id}")
    public ResponseEntity<Student>updateLocker(@PathVariable(value = "id") Long lockerId, @Valid @RequestBody Locker lockerDetail) throws ResourceNotFoundException {
        return new ResponseEntity(userService.updateLocker(lockerId,lockerDetail),HttpStatus.OK);
    }

    @DeleteMapping("/delete/locker/{id}")
    public void deleteLocker(@PathVariable(value = "id") Long lockerId){
        userService.deleteLocker(lockerId);
    }

    /*Students*/
    @GetMapping("/students")
    public ResponseEntity<List<Student>>getStudents(){
        return ResponseEntity.ok().body(userService.getStudents());
    }

    @GetMapping("/findStudent")
    public ResponseEntity<List<Student>>searchForZiak(@SearchSpec Specification<Student> specification){
        return new ResponseEntity<>(studentRepo.findAll(Specification.where(specification)), HttpStatus.OK);
    }

    @PostMapping("/add/student")
    public ResponseEntity<Student>saveStudent(@RequestBody Student student){
        return ResponseEntity.ok().body(userService.saveStudent(student));
    }
    @GetMapping("/print/pdf")

    public ResponseEntity<List<RoomLockerModel>> printPdf(){
        List<Room> list= userService.getRooms();
        List<LockerModel> lockerModels = new ArrayList<>();
        List<RoomLockerModel> roomLockerModels = new ArrayList<>();
        LockerModel lockerModel = new LockerModel();
        Room room = new Room();
        List<Locker> lockers = new ArrayList<>();
        RoomLockerModel roomLockerModel = new RoomLockerModel();

        for (int i = 0; i < list.size(); i++) {
            Room roomCurent = list.get(i);
            lockers = roomCurent.getLockers();
            roomLockerModel = new RoomLockerModel();

            for (Locker locker: lockers) {
                lockerModel = new LockerModel();
                lockerModel.setStudent(locker.getStudent());
                lockerModel.setId(locker.getId());
                lockerModel.setNumber(locker.getNumber());
                lockerModels.add(lockerModel);
            }
            roomLockerModel.setLockers(lockerModels);
            roomLockerModels.add(roomLockerModel);

        }
        return ResponseEntity.ok().body(roomLockerModels);
    }
    @PutMapping("/edit/student")
    public void updateStudent(@Valid @RequestBody StudentDetails form) throws ResourceNotFoundException {
        Locker locker = userService.getLockerByNumber(form.getNumber());
        Student student = locker.getStudent();
        Student studentNew = new Student(null, form.getStudentName(), form.getClassroom(), form.getGrade());
        if (locker.getStudent() == null || locker.getStudent().getStudentName() == null){
            userService.saveStudent(studentNew);
            userService.addStudentToLocker(form.getStudentName(), form.getNumber(), form.getRoomName());
        }
        else {
            student.setStudentName(form.getStudentName());
            student.setClassroom(form.getClassroom());
            student.setGrade(form.getGrade());
            userService.getLockerByNumber(form.getNumber()).setStudent(student);
        }
    }

    @DeleteMapping("/delete/studentFromLocker")
    public void deleteStudentFromLocker(@RequestBody Integer integer){
        userService.removeStudentFromLocker(integer);

    }

    @DeleteMapping("/delete/student/{id}")
    public void deleteStudent(@PathVariable(value = "id") Long studentId){
        userService.deleteStudent(studentId);
    }
}
