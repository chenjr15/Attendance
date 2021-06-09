package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputBindThirdPartyDTO {
    @NotBlank
    private String openid;
    @NotBlank
    private String accessToken;
    @NotBlank
    private String type;
    @Schema(hidden = true)
    private Long uid;

}
