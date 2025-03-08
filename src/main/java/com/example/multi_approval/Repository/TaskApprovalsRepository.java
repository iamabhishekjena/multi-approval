package com.example.multi_approval.Repository;

import com.example.multi_approval.Entity.TaskApprovals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskApprovalsRepository extends JpaRepository<TaskApprovals,Integer> {

    @Query("SELECT t FROM task_approvals t WHERE t.taskId = :taskId ORDER BY t.approvedOn DESC")
    List<TaskApprovals> findAllApprovalRecordsDesc(@Param("taskId") int taskId);

    Optional<TaskApprovals> findByTaskIdAndApproverId(int taskId, int userId);

    List<TaskApprovals> findByTaskId(int taskId);

    @Query("SELECT COUNT(a) FROM task_approvals a WHERE a.taskId = :taskId AND a.status = 'PENDING'")
    int countPendingApprovals(int taskId);
}
