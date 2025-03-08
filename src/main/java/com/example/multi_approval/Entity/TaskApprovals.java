package com.example.multi_approval.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "task_approvals")
public class TaskApprovals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int taskId;
    
    private int approverId;
    
    private LocalDateTime approvedOn;

    private String status;
}
