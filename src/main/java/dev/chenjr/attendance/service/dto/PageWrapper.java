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
    @Schema(description = "当前分页元素数量")
    private long curItemCount;
    @Schema(description = "当前分页下标, 从1开始")
    private long current;
    @Schema(description = "页面大小")
    private long size;
    @Schema(description = "所有的数据项数量")
    private long total;
    @Schema(description = "页面数")
    private long pageCount;


    public static <E> PageWrapper<E> fromIPage(IPage<?> iPage) {
        int curCount = 0;
        if (iPage.getRecords() != null) {
            curCount = iPage.getRecords().size();
        }
        return new PageWrapper<>(
                null,
                curCount,
                iPage.getCurrent(),
                iPage.getSize(),
                iPage.getTotal(),
                iPage.getPages()
        );
    }

    public static <E> PageWrapper<E> fromList(IPage<?> iPage, List<E> list) {

        PageWrapper<E> wrapper = PageWrapper.fromIPage(iPage);
        if (list != null) {
            wrapper.setCurItemCount(list.size());
        }
        wrapper.setContent(list);
        return wrapper;
    }
}
