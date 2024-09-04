package com.example.application.service;

import com.example.application.data.ClassEntity;
import com.example.application.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<ClassEntity> findAllClasses() {
        return classRepository.findAll();
    }

    public Optional<ClassEntity> findClassById(UUID id) {
        return classRepository.findById(id);
    }

    public ClassEntity saveClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public void deleteClass(UUID id) {
        classRepository.deleteById(id);
    }

    @Transactional
    public ClassEntity findById(UUID id) {
        return classRepository.findById(id).orElse(null);
    }
}
