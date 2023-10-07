package com.example.repository;

import com.example.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // Удаляет файл по имени
    void deleteByFilename(String filename);

    // Находит файл по имени
    FileEntity findByFilename(String filename);

    // Находит все файлы, принадлежащие пользователю с заданным идентификатором
    List<FileEntity> findAllByUserId(Long userId);
}
