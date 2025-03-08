package com.example.multi_approval.Entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity(name = "USERS")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String name;

    private String emailId;

    private String loginId;

    private String loginStatus="N";
}
