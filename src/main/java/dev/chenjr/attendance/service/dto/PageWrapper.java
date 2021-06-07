package dev.chenjr.attendance.service.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页包装器，记录分页的信息
 *
 * @param <T> 内容类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageWrapper<T> {
    @Schema(description = "数据内容(数组)")
    private List<T> content;
    @Schema(description = "当前分页下表, 从1开始")
    private Long current;
    @Schema(description = "页面大小")
    private Long size;
    @Schema(description = "所有的数据项数量")
    private Long total;
    @Schema(description = "页面数")
    private Long pageCount;

    public static <E> PageWrapper<E> fromIPage(IPage<?> iPage) {
        return new PageWrapper<>(
                null,
                iPage.getCurrent(),
                iPage.getSize(),
                iPage.getTotal(),
                iPage.getPages()
        );
    }

}
