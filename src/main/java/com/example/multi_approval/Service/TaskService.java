package com.example.multi_approval.Service;

import com.example.multi_approval.DTO.TaskRequestDto;
import com.example.multi_approval.Entity.Comments;
import com.example.multi_approval.Entity.TaskApprovals;
import com.example.multi_approval.Entity.Tasks;
import com.example.multi_approval.Entity.Users;
import com.example.multi_approval.Repository.CommentsRepository;
import com.example.multi_approval.Repository.TaskApprovalsRepository;
import com.example.multi_approval.Repository.TaskRepository;
import com.example.multi_approval.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskApprovalsRepository taskApprovalsRepository;

    @Autowired
    UserService userService;

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    public static final int MAX_APPROVAL_LEVEL = 3;

    public ResponseEntity<String> createTask(TaskRequestDto taskDto) {
        int userId = userService.getUseridByLoginId(taskDto.getLoginId());
        if(userId==-1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        if (!userService.checkUserLoginStatus(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Logged In");
        }
        Tasks task = new Tasks();
        task.setDescription(taskDto.getDescription());
        task.setUserId(userId);
        taskRepository.save(task);


        for (String approverLoginId : taskDto.getApproverLoginIds()) {
            Users users = userService.getUserByLoginId(approverLoginId);
            if (users==null){
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
            TaskApprovals taskApproval = new TaskApprovals();
            taskApproval.setTaskId(task.getTaskId());
            taskApproval.setApproverId(users.getUserId());
            taskApproval.setStatus("PENDING");
            taskApprovalsRepository.save(taskApproval);

            emailService.sendTaskAssignmentEmail(users.getEmailId(), task.getTaskId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Task created and assigned successfully.");
    }



    public ResponseEntity<String> approveTask(int taskId,String loginId) {
        int userId = userService.getUseridByLoginId(loginId);
        if(userId==-1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        Optional<TaskApprovals> taskApprovalOpt = taskApprovalsRepository.findByTaskIdAndApproverId(taskId, userId);
        if (!taskApprovalOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not an approver for this task.");
        }
        if (taskApprovalOpt.get().getStatus().equals("APPROVED")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already approved by the user");
        }
        TaskApprovals taskApproval = taskApprovalOpt.get();
        taskApproval.setStatus("APPROVED");
        taskApproval.setApprovedOn(LocalDateTime.now());
        taskApprovalsRepository.save(taskApproval);

        int creatorId = taskRepository.findById(taskId).get().getUserId();
        String creatorEmail = userRepository.findById(creatorId).get().getEmailId();
        emailService.sendSingleApprovalEmail(creatorEmail,userId, taskId);

        if (taskApprovalsRepository.countPendingApprovals(taskId) == 0) {
            Optional<Tasks> tasks = taskRepository.findById(taskId);
            tasks.get().setStatus("APPROVED");
            taskRepository.save(tasks.get());
            notifyAllParties(taskId);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Task Approved Successfully.");

    }

    public ResponseEntity<String> addComment(int taskId, String loginId, String commentText) {
        int userId = userService.getUseridByLoginId(loginId);
        if(userId==-1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        if (!userService.checkUserLoginStatus(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Logged In");
        }
        Optional<TaskApprovals> taskApprovalOpt = taskApprovalsRepository.findByTaskIdAndApproverId(taskId, userId);
        if (!taskApprovalOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not an approver for this task.");
        }

        Comments comment = new Comments();
        comment.setTaskId(taskId);
        comment.setUserId(userId);
        comment.setComment(commentText);
        comment.setCreatedAt(LocalDateTime.now());

        commentsRepository.save(comment);
        return ResponseEntity.status(HttpStatus.OK).body("Comment added successfully.");
    }


    private void notifyAllParties(int taskId) {
        List<TaskApprovals> taskApprovals = taskApprovalsRepository.findByTaskId(taskId);
        List<String> emails = new ArrayList<>();

        for (TaskApprovals approval : taskApprovals) {
            String email = userRepository.findById(approval.getApproverId()).get().getEmailId();
            emails.add(email);
        }

        String creatorEmail = userRepository.findById(taskRepository.findById(taskId).get().getUserId()).get().getEmailId();
        emails.add(creatorEmail);

        emailService.sendFinalApprovalNotification(emails, taskId);
    }
}
