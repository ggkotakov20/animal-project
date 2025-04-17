package com.backendcore.backendcore.v1.customer.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_perpetual_tokens")
public class ValidToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = true)
    private Integer userId;

    @Column(name = "token", nullable = false, length = 255)
    private String token;

    @Column(name = "ip", nullable = false, length = 255)
    private String ip;

    @Column(name = "device_name", nullable = true, length = 255)
    private String name;

    @Column(name = "device_model", nullable = true, length = 255)
    private String model;

    @Column(name = "date", nullable = false, length = 255)
    private Timestamp date;

    public ValidToken(Integer userId) {
        setUserId(userId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
