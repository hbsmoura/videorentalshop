package com.hbsmoura.videorentalshop.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
