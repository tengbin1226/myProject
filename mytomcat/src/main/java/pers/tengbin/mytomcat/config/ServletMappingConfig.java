package pers.tengbin.mytomcat.config;

import pers.tengbin.mytomcat.mapping.ServletMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:ServletMappingConfig
 * @Description: servlet配置
 * 在web.xml中通过<servlet></servlet>和<servlet-mapping></servlet-mapping>来进行指定哪个URL交给哪个servlet进行处理
 * @Author Mr.T
 * @date 2020/5/7 14:12
 */
public class ServletMappingConfig {


    public static List<ServletMapping> servletMappings=new ArrayList<>();

    static {
        servletMappings.add(new ServletMapping("findGirl","/girl","pers.tengbin.mytomcat.servlet.FindGirlServlet"));
        servletMappings.add(new ServletMapping("helloWorld","/hello","pers.tengbin.mytomcat.servlet.HelloWorldServlet"));
        servletMappings.add(new ServletMapping("faviconServlet","/favicon.ico","pers.tengbin.mytomcat.servlet.FaviconServlet"));
    }

}
