package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudentDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type = "string", description = "userid会返回字符串格式的uid以防止json数字精度丢失问题")
    private long uid;

    private String stuName;

    @Schema(description = "学号")
    private String stuId;
    @Schema(description = "经验值")
    private int experience;
}
