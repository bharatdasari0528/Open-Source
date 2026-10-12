package com.example.auth.entity;
import jakarta.persistence.*;
@Entity @Table(name="users") public class User {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@Column(nullable=false) public String name;
@Column(nullable=false,unique=true,length=190) public String email;
@Column(nullable=false) public String password;
}
