package com.ztiany.adapter.recycler;

public interface HeaderAndFooterSize {

    default int getFooterSize() {
        return 0;
    }

    default int getHeaderSize() {
        return 0;
    }

}
