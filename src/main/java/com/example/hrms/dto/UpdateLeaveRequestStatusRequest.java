package com.example.hrms.dto;

import com.example.hrms.entity.LeaveRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLeaveRequestStatusRequest {
    @NotNull
    private LeaveRequestStatus status;
}
