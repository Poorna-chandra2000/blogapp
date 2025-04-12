package com.poorna.blogapp.Repositories;

import com.poorna.blogapp.Entities.BlogContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogContentRepo extends JpaRepository<BlogContents,Long> {
}
