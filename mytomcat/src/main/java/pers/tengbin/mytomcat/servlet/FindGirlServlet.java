package pers.tengbin.mytomcat.servlet;

import pers.tengbin.mytomcat.handle.MyRequest;
import pers.tengbin.mytomcat.handle.MyResponse;

import java.io.IOException;

/**
 * @ClassName:FindGirlServlet
 * @Description: 具体的Servlet实现，只是为了后续的测试
 * @Author Mr.T
 * @date 2020/5/7 14:05
 */
public class FindGirlServlet extends AbstractMyServlet {

    @Override
    public void doGet(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("GET GIRL....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("POST GIRL....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
