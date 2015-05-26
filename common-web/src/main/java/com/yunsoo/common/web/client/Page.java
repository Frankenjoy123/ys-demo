package com.yunsoo.common.web.client;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/25
 * Descriptions:
 */
public class Page<T> {

    private T content;
    private int page;
    private int total;

    public Page(T content, int page, int total) {
        this.content = content;
        this.page = page;
        this.total = total;
    }

    public T getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

}
