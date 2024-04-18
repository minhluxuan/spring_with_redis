package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.StudentRepository;

import com.example.demo.models.Student;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class StudentService {
    private static final String HASH_KEY = "STUDENT";

    @Autowired
    private final StudentRepository repository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, String, Student> hashOperations;

    public StudentService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.repository = new StudentRepository();
    }

    public void save(Student student) throws ExecutionException, InterruptedException {
        // hashOperations.put(HASH_KEY, student.getId(), student);
        repository.createStudent(student);
    }

    public Map<String, Student> findAll() {
        return hashOperations.entries(HASH_KEY);
    }

    public Optional<Student> findById(String id) throws ExecutionException, InterruptedException {
        if (hashOperations.hasKey(HASH_KEY, id)) {
            return Optional.ofNullable(hashOperations.get(HASH_KEY, id));
        } else {
            Optional<Student> studentOptional = repository.findById(id);
            studentOptional.ifPresent(student -> hashOperations.put(HASH_KEY, student.getId(), student));
            return studentOptional;
        }
    }

    public void update(String id, Student student) throws ExecutionException, InterruptedException, IllegalAccessException {
        String documentId = repository.getDocumentIdByStudentId(id);
        System.out.println(documentId);
        repository.updateStudent(documentId, student);
        hashOperations.put(HASH_KEY, student.getId(), student);
    }

    public void delete(String id) {
        hashOperations.delete(HASH_KEY, id);
    }
}
