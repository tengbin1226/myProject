package pers.tengbin.mytomcat.mapping;

import lombok.Data;

/**
 * @ClassName:ServletMapping
 * @Description: servlet配置
 * @Author Mr.T
 * @date 2020/5/7 14:06
 */
@Data
public class ServletMapping {

    /**
     * servlet名称
     */
    private String servletName;

    /**
     * 请求url
     */
    private String url;

    /**
     * 类
     */
    private String clas;


    /**
     * 有参构造
     *
     * @param servletName
     * @param url
     * @param clas
     */
    public ServletMapping(String servletName, String url, String clas) {
        this.servletName = servletName;
        this.url = url;
        this.clas = clas;
    }
}
