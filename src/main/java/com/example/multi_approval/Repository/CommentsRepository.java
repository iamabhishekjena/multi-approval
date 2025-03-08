package com.example.multi_approval.Repository;

import com.example.multi_approval.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Integer> {

    List<Comments> findByTaskId(int taskId);
}
