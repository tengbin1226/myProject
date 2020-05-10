package pers.tengbin.mytomcat.servlet;

import pers.tengbin.mytomcat.handle.MyRequest;
import pers.tengbin.mytomcat.handle.MyResponse;

/**
 * @ClassName:MyServlet
 * @Description: 提供Servlet.
 * Tomcat是满足Servlet规范的容器，那么自然Tomcat需要提供API,如Servlet常见的doGet/doPost/service方法。
 * @Author Mr.T
 * @date 2020/5/7 14:04
 */
public abstract class AbstractMyServlet {

    /**
     * get方法
     *
     * @param myRequest  请求
     * @param myResponse 响应
     */
    public abstract void doGet(MyRequest myRequest, MyResponse myResponse);

    /**
     * post方法
     *
     * @param myRequest  请求
     * @param myResponse 响应
     */
    public abstract void doPost(MyRequest myRequest, MyResponse myResponse);


    public void service(MyRequest myRequest, MyResponse myResponse) {
        String postStr = "POST";

        String getStr = "GET";

        if (postStr.equalsIgnoreCase(myRequest.getMethod())) {
            doPost(myRequest, myResponse);
        } else if (getStr.equalsIgnoreCase(myRequest.getMethod())) {
            doGet(myRequest, myResponse);
        }else {
            System.out.println("未获取到请求方法");
        }
    }
}
