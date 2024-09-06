package com.example.application.service;

import com.example.application.data.ClassEntity;
import com.example.application.data.User;
import com.example.application.repository.ClassRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }
    @Transactional
    public List<ClassEntity> findAllClasses() {
        return classRepository.findAll();
    }

    public Optional<ClassEntity> findClassById(Long id) {
        return classRepository.findById(id);
    }

    @Transactional
    public ClassEntity saveClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    @Transactional
    public ClassEntity findById(Long id) {
        return classRepository.findById(id).orElse(null);
    }

    public Set<ClassEntity> findClassesByAttendees(User user) {
        return classRepository.findClassesByAttendees(user);
    }

    @Transactional(readOnly = true)
    public ClassEntity getClassWithAttendees(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        // Inicializar la colección de attendees
        Hibernate.initialize(classEntity.getAttendees());
        return classEntity;
    }


}
