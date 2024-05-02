package com.yupi.springbootinit.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import org.springframework.stereotype.Service;

/**
 * 图片服务
 *
 * @author <a href="https://github.com/aiqiku">程序员艾琪苦</a>
 * 
 */

public interface PictureService {

 Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
