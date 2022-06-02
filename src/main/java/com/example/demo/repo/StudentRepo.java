package com.example.demo.repo;

import com.example.demo.domain.Locker;
import com.example.demo.domain.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface StudentRepo extends JpaRepository<Student,Long>,JpaSpecificationExecutor<Student> {
    Student findByStudentName(String studentName);
    Student findByClassroom(String classroom);
    Student findByGrade(String grade);
    Optional<Student> findById(Long id);
    Student findAllById (Long id);
}
