package dev.chenjr.attendance.service.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.utils.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "分页排序", description = "不要忘记转义！")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageSort {

    public static final String ASC_STRING = "asc";
    private static final boolean ASC = true;
    @Schema(description = "当前页，从1开始", defaultValue = "1")
    long curPage;
    @Schema(description = "页面大小", defaultValue = "10")
    long pageSize;
    @Schema(description = "排序，用逗号分割 ", example = "key1:desc")
    List<String> orderBy;
    @Schema(description = "返回属性，用逗号分割", example = "key1")
    List<String> returns;
    @Schema(description = "筛选器(完全相等)，用逗号分割, 记得转义！key1=kw1,key2=kw2", example = "key1=kw1")
    List<String> filters;

    @Schema(description = "搜索(部分相等)，记得转义！", example = "keyword")
    String search;

    @Schema(hidden = true)
    public <E> Page<E> getPage() {

        if (pageSize == 0) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        return new Page<>(curPage, pageSize);
    }

    @Schema(hidden = true)
    public List<Pair<String, Boolean>> getOrderList() {
        if (this.orderBy == null || this.orderBy.size() == 0) {
            return new ArrayList<>();
        }
        String[] split;
        Pair<String, Boolean> kvPair;
        String field;
        List<Pair<String, Boolean>> pairList = new ArrayList<>(orderBy.size());

        for (String kv : orderBy) {
            if (kv.length() == 0) {
                continue;
            }
            split = kv.split(":");
            field = StringUtil.toUnderlineCase(split[0]);
            if (split.length == 2) {
                kvPair = Pair.of(field, ASC_STRING.equalsIgnoreCase(split[1]));
            } else {
                kvPair = Pair.of(field, ASC);
            }
            pairList.add(kvPair);
        }
        return pairList;
    }

    @Schema(hidden = true)
    public List<Pair<String, String>> getFilterList() {
        if (this.filters == null || this.filters.size() == 0) {
            return new ArrayList<>();
        }
        List<Pair<String, String>> pairList = new ArrayList<>(filters.size());
        Pair<String, String> kvPair;
        String[] split;
        String field;
        for (String kv : filters) {
            split = kv.split("=");
            if (split.length == 1) {
                continue;
            }
            field = StringUtil.toUnderlineCase(split[0]);
            kvPair = Pair.of(field, split[1]);
            pairList.add(kvPair);
        }

        return pairList;
    }

    @Schema(hidden = true)
    public <E> QueryWrapper<E> buildQueryWrapper(QueryWrapper<E> qw, String... searchFields) {

        // 排序
        for (Pair<String, Boolean> order : this.getOrderList()) {
            qw = qw.orderBy(true, order.getSecond(), order.getFirst());
        }
        // 筛选
        for (Pair<String, String> filter : this.getFilterList()) {
            qw = qw.eq(filter.getFirst(), filter.getSecond());
        }
        // 指定返回数据域
        if (returns != null) {
            String[] fields = returns.toArray(new String[0]);
            qw = qw.select(fields);
        }
        // 查找
        for (String searchField : searchFields) {
            if (StringUtil.notEmpty(search) && StringUtil.notEmpty(searchField)) {
                qw = qw.like(StringUtil.toUnderlineCase(searchField), search);
            }
        }
        return qw;

    }
}
