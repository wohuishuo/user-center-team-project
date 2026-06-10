package com.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/** 通用分页返回。 */
@Data
public class PageResult<T> implements Serializable {
    private List<T> records;
    private long total;
    private long current;
    private long pageSize;

    public PageResult(List<T> records, long total, long current, long pageSize) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.pageSize = pageSize;
    }
}
