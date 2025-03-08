package com.example.multi_approval.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskRequestDto {
    private String loginId;
    private String description;
    private List<String> approverLoginIds;
}
