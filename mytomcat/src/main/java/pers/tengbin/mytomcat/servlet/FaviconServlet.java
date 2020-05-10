package pers.tengbin.mytomcat.servlet;

import pers.tengbin.mytomcat.handle.MyRequest;
import pers.tengbin.mytomcat.handle.MyResponse;

import java.io.IOException;

/**
 * @ClassName:FaviconServlet
 * @Description: 网站图标映射
 * @Author Mr.T
 * @date 2020/5/7 17:11
 */
public class FaviconServlet extends AbstractMyServlet {

    @Override
    public void doGet(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("GET favicon.ico....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("POST favicon.ico....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
