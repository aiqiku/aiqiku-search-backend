package com.yupi.springbootinit.model.vo;

import com.yupi.springbootinit.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 *
 * @author <a href="https://github.com/aiqiku">程序员艾琪苦</a>
 * 
 */
@Data
public class SearchVO implements Serializable {

   private List<PostVO> postVOList;

   private List<UserVO> userVOList;

   private List<Picture> pictureList;

   private static final long serialVersionUID = 1L;
}
