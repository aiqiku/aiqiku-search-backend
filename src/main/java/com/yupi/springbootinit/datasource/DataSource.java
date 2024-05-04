package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author aiqiku
 * @create 2024/5/4 19:46
 */
public interface DataSource<T> {

    // 定义数据源的接口方法
    Page<T> doSearch(String searchText, int current, int pageSize);
}
