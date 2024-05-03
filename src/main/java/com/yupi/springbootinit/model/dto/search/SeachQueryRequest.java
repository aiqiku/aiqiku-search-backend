package com.yupi.springbootinit.model.dto.search;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/aiqiku">程序员艾琪苦</a>
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SeachQueryRequest extends PageRequest implements Serializable {



    /**
     * 搜索词
     */
    private String searchText;



    private static final long serialVersionUID = 1L;
}