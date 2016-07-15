package com.yunsoo.common.web.client;

import com.yunsoo.common.web.util.PageableUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/25
 * Descriptions:
 */
public class Page<T> implements Iterable<T>, Serializable {

    private List<T> content;
    private Integer page; //page index, start from 0
    private Integer total; //total pages
    private Integer count; //total elements count


    public Page(List<T> content, Integer page, Integer total) {
        Assert.notNull(content, "content must not be null");
        this.content = content;
        this.page = page;
        this.total = total;
    }

    public Page(List<T> content, Integer page, Integer total, Integer count) {
        Assert.notNull(content, "content must not be null");
        this.content = content;
        this.page = page;
        this.total = total;
        this.count = count;
    }

    public List<T> getContent() {
        return content;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getCount() {
        return count;
    }

    public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
        List<R> contentR = content.stream().map(mapper).collect(Collectors.toList());
        return new Page<>(contentR, page, total, count);
    }

    public String toContentRange() {
        return PageableUtils.formatPages(page, total, count);
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }


    public static <T> Page<T> empty() {
        return new Page<>(new ArrayList<>(0), 0, 0, 0);
    }

}
