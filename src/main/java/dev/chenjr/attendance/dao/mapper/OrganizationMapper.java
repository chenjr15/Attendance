package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Organization;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * <p>
 * 学校信息表，学校-> 院系->专业->班级 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface OrganizationMapper extends MyBaseMapper<Organization> {
    @Override
    @Select("SELECT 1 FROM organization WHERE id=#{id} limit 1 ")
    Optional<Boolean> exists(long id);
}
