package com.example.hrms.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String phoneNumber;
    private String address;
}
