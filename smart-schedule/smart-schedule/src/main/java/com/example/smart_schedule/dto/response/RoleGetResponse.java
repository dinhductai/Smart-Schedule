package com.example.smart_schedule.dto.response;

import com.example.smart_schedule.enumeration.RoleName;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleGetResponse {
    private RoleName roleName;
    private String description;
    private Timestamp createTime;
    private List<PermissionResponse> permissionName;



}