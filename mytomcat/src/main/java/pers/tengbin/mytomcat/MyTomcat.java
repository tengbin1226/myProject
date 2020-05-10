package pers.tengbin.mytomcat;

import pers.tengbin.mytomcat.config.ServletMappingConfig;
import pers.tengbin.mytomcat.handle.MyRequest;
import pers.tengbin.mytomcat.handle.MyResponse;
import pers.tengbin.mytomcat.mapping.ServletMapping;
import pers.tengbin.mytomcat.servlet.AbstractMyServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:MyTomcat
 * @Description: 启动类
 * Tomcat的处理流程：
 * 1. 把URL对应处理的Servlet关系形成，
 * 2. 解析HTTP协议，封装请求/响应对象，
 * 3.利用反射实例化具体的Servlet进行处理即可
 * @Author Mr.T
 * @date 2020/5/7 14:13
 */
public class MyTomcat {

    /**
     * 端口号
     */
    private int port=9090;

    private Map<String,String> urlServletMap=new HashMap<>();

    public MyTomcat(int port) {
        this.port = port;
    }

    /**
     *  启动
     */
    public void start(){
        // 初始化URL与对应处理的servlet的关系
        initServletMpping();

        // 初始化服务套接字对象
        ServerSocket serverSocket=null;

        try {
            // 实例化服务套接字对象
            serverSocket=new ServerSocket(port);

            // 测试代码
            System.out.println("mytomcat已启动.....");

            while (true){
                // 获取套接字对象
                Socket socket=serverSocket.accept();
                // 通过套接字对象获取输入/输出流对象
                InputStream inputStream=socket.getInputStream();

                OutputStream outputStream=socket.getOutputStream();

                // 获取请求与响应
                MyRequest myRequest=new MyRequest(inputStream);
                MyResponse myResponse=new MyResponse(outputStream);

                // 分发请求
                dispatch(myRequest,myResponse);

                // 关闭套接字
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化servlet映射
     */
    public void initServletMpping(){

        if (ServletMappingConfig.servletMappings.size()>0 ){
            for (ServletMapping servletMapping : ServletMappingConfig.servletMappings) {
                urlServletMap.put(servletMapping.getUrl(),servletMapping.getClas());
            }
        }else {
            System.out.println("映射集合为空!");
        }
    }

    /**
     *  调度
     * @param myRequest
     * @param myResponse
     */
    private void dispatch(MyRequest myRequest, MyResponse myResponse){

        // 获取映射类
        String clas=urlServletMap.get(myRequest.getUrl());


        // TODO 反射
        try {
            Class<AbstractMyServlet> abstractMyServletClass= (Class<AbstractMyServlet>) Class.forName(clas);

            AbstractMyServlet abstractMyServlet=abstractMyServletClass.newInstance();

            abstractMyServlet.service(myRequest,myResponse);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyTomcat(9090).start();
    }

}
