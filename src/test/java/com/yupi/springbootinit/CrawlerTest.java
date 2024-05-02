package com.yupi.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.*;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aiqiku
 * @create 2024/5/2 9:56
 */
@SpringBootTest
public class CrawlerTest {
    @Resource
    private PostService postService;

    @Test
    void testPicture() throws IOException {
        int current = 1;
        String url = "https://cn.bing.com/images/search?q=小黑子&form=HDRSC2&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements select = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : select) {
            //取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            System.out.println("murl = " + murl);
            //取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            System.out.println("title = " + title);
            Picture picture = new Picture();
            if (!murl.isEmpty()){
                picture.setUrl(murl);
            }
            if (!title.isEmpty()){
                picture.setTitle(title);
            }
            pictures.add(picture);
        }
//        Elements newsHeadlines = doc.select("#mp-itn b a");
//        for (Element headline : newsHeadlines) {
//
//        }
    }
    @Test
    void test() {
        for (int i = 1; i < 10; i++) {
            //1.获取数据
            String json = "{\"current\":" + i + ",\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"tags\":[],\"reviewStatus\":1}";
            String url = "https://api.code-nav.cn/api/post/search/page/vo";
            String result2 = HttpRequest.post(url)
                    .body(json)
                    .execute().body();
            //2.json转成对象
            Map<String, Object> map = JSONUtil.toBean(result2, Map.class);
            JSONObject data = (JSONObject) map.get("data");
            JSONArray records = (JSONArray) data.get("records");
            List<Post> postList = new ArrayList<>();
            for (Object record : records) {
                JSONObject temp = (JSONObject) record;
                String content = temp.getStr("content");
                if (content.length() > 3000){
                    continue;
                }

                Post post = new Post();
                post.setTitle(temp.getStr("title"));

                post.setContent(temp.getStr("content"));
                JSONArray tagsArray = (JSONArray) temp.get("tags");
                List<String> list = tagsArray.toList(String.class);
                post.setTags(JSONUtil.toJsonStr(list));
                post.setThumbNum(temp.getInt("thumbNum"));
                post.setFavourNum(temp.getInt("favourNum"));
                post.setUserId(1785185275765420033L);
                postList.add(post);
            }
            boolean b = postService.saveBatch(postList);
        }

    }
}
