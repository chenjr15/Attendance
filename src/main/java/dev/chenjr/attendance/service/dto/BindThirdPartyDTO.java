package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindThirdPartyDTO {
    @NotBlank
    private String openid;
    @NotBlank
    private String accessToken;
    @NotBlank
    private String type;
    @Schema(hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

}
