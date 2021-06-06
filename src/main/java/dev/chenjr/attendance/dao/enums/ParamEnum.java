package dev.chenjr.attendance.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ParamEnum implements IEnum<Integer> {
    DOUBLE(0),
    STRING(1);

    ParamEnum(int code) {
        this.code = code;
    }

    @JsonValue
    @EnumValue
    private final int code;

    @Override
    public Integer getValue() {
        return this.code;

    }
}
