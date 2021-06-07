package dev.chenjr.attendance.dao.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    private List<T> content;
    private Long current;
    private Long size;
    private Long total;
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
