package com.poorna.blogapp.Repositories;

import com.poorna.blogapp.Entities.FileShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileShareRepo extends JpaRepository<FileShare,Long> {

    List<FileShare> findByReciever(String email);
}
