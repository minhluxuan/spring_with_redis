package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Student;
import com.example.demo.services.StudentService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public void addStudent(@RequestBody Student student) throws ExecutionException, InterruptedException {
        studentService.save(student);
    }

    @GetMapping
    public Map<String, Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudentById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return studentService.findById(id);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable String id, @RequestBody Student student) throws ExecutionException, InterruptedException, IllegalAccessException {
        Optional<Student> existingStudent = studentService.findById(id);
        if (existingStudent != null) {
            student.setId(id);
            studentService.update(id, student);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentService.delete(id);
    }
}