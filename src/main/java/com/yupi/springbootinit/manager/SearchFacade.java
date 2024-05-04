package com.yupi.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SeachQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author aiqiku
 * @create 2024/5/4 17:15
 * 搜索门面
 * 使用了门面模式
 */
@Component
@Slf4j
public class SearchFacade {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;
    @Resource
    private PictureService pictureService;

    public  SearchVO searchAll(@RequestBody SeachQueryRequest seachQueryRequest, HttpServletRequest request) {
        // 获取搜索类型
        String type = seachQueryRequest.getType();
        SearchTypeEnum enumByValue = SearchTypeEnum.getEnumByValue(type);

        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR, "搜索类型不能为空");
        String searchText = seachQueryRequest.getSearchText();
        int current = seachQueryRequest.getCurrent();
        int pageSize = seachQueryRequest.getPageSize();
        // 根据搜索类型进行不同的搜索操作
        if (enumByValue == null) {
            //搜索出所有的内容

            // 查询帖子
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                return postVOPage;
            });
            // 查询用户
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);

                Page<UserVO> userVOByPage = userService.listUserVOByPage(userQueryRequest);
                return userVOByPage;
            });

            // 查询图片
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
                return picturePage;
            });
            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setPostVOList(postVOPage.getRecords());
                searchVO.setUserVOList(userVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("聚合查询异常");
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            // 实现帖子搜索逻辑
            SearchVO searchVO = new SearchVO();
            switch (enumByValue) {
                case POST -> {
                    // 查询帖子
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostVOList(postVOPage.getRecords());
                }
                case USER -> {
                    // 查询用户
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOByPage = userService.listUserVOByPage(userQueryRequest);
                    searchVO.setUserVOList(userVOByPage.getRecords());
                }
                case PICTURE -> {
                    // 查询图片
                    String searchText1 = seachQueryRequest.getSearchText();
                    if (searchText1 == null) {
                        searchText1 = "小黑子";
                    }
                    Page<Picture> picturePage = pictureService.searchPicture(searchText1, current, pageSize);
                    searchVO.setPictureList(picturePage.getRecords());
                }
            }
            return searchVO;
        }
    }
}
