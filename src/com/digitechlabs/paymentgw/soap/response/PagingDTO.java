package com.digitechlabs.paymentgw.soap.response;

public class PagingDTO {
    private int page;
    private long total;
    private int per_page;

    public PagingDTO(int page, long total, int per_page) {
        this.page = page;
        this.total = total;
        this.per_page = per_page;
    }

    public int getPage() {
        return page;
    }

    public PagingDTO setPage(int page) {
        this.page = page;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PagingDTO setTotal(long total) {
        this.total = total;
        return this;
    }

    public int getPer_page() {
        return per_page;
    }

    public PagingDTO setPer_page(int per_page) {
        this.per_page = per_page;
        return this;
    }
}
