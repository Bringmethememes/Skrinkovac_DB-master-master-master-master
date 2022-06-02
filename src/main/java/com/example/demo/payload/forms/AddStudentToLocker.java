package com.example.demo.payload.forms;

import lombok.Data;

@Data
public class AddStudentToLocker {
    private String roomName;
    private String studentName;
    private Integer number;
}
