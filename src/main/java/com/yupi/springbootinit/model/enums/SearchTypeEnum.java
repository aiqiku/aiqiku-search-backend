package com.yupi.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传业务类型枚举
 *
 * @author <a href="https://github.com/aiqiku">程序员艾琪苦</a>
 * 
 */
public enum SearchTypeEnum {

    POST("帖子", "post"),
    USER("用户", "user"),
    PICTURE("图片", "picture");
    private final String text;

    private final String type;

    SearchTypeEnum(String text, String type) {
        this.text = text;
        this.type = type;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.type).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SearchTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SearchTypeEnum anEnum : SearchTypeEnum.values()) {
            if (anEnum.type.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
