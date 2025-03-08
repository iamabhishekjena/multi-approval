package com.example.multi_approval.Controller;

import com.example.multi_approval.DTO.TaskRequestDto;
import com.example.multi_approval.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody TaskRequestDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveTask(@RequestParam int taskId, @RequestParam String loginId) {
        return taskService.approveTask(taskId, loginId);
    }

    @PostMapping("/addComment")
    public ResponseEntity<String> addComment(@RequestParam int taskId, @RequestParam String loginId, @RequestParam String comment) {
        return taskService.addComment(taskId, loginId, comment);
    }
}
