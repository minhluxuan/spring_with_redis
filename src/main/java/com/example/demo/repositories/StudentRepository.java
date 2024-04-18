package com.example.demo.repositories;

import com.example.demo.models.Student;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class StudentRepository extends GenericRepositoryImpl {
    public void createStudent(Student student) throws ExecutionException, InterruptedException {
        String documentId = UUID.randomUUID().toString();
        createDocument("Students", documentId, student);
    }

    public Optional<Student> findByUsername(String username) throws ExecutionException, InterruptedException {
        List<Student> students = getDocumentsByAttribute("Students", "username", username, Student.class);
        if (students.isEmpty()) {
            return Optional.empty();
        }
    
        return Optional.of(students.get(0));
    }

    public Optional<Student> findById(String id) throws ExecutionException, InterruptedException {
        List<Student> students = getDocumentsByAttribute("Students", "id", id, Student.class);
        if (students.isEmpty()) {
            return Optional.empty();
        }
    
        return Optional.of(students.get(0));
    }

    public void updateStudent(String documentId, Student student) throws IllegalAccessException, ExecutionException, InterruptedException {
        updateDocument("Students", documentId, student);
    }
}