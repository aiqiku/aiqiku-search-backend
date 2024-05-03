package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author aiqiku
 * @create 2024/5/2 11:24
 */
@Data
public class Picture implements Serializable {
    private String title;
    private String url;
    private static final long serialVersionUID = 1L;
}
