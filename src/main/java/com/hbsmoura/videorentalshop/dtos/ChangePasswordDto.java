package com.hbsmoura.videorentalshop.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
