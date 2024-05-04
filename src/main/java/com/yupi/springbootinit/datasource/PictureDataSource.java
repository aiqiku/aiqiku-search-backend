package com.yupi.springbootinit.datasource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aiqiku
 * @create 2024/5/2 11:42
 */
@Service
public class PictureDataSource implements DataSource<Picture>  {

    @Resource
    private PictureService pictureService;
    @Override
    public Page<Picture> doSearch(String searchText, int current, int pageSize) {
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
        return picturePage;
    }
}