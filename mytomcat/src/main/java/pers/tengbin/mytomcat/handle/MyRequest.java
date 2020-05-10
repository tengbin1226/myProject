package pers.tengbin.mytomcat.handle;

import com.sun.xml.internal.bind.v2.TODO;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName:MyRequest
 * @Description: 封装请求对象. 通过输入流，对HTTP协议进行解析，拿到了HTTP请求头的方法以及URL
 * @Author Mr.T
 * @date 2020/5/7 13:51
 */
@Data
public class MyRequest {

    /**
     * url
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    public MyRequest(InputStream inputStream) throws IOException {
        /** TODO 对HTTP协议进行解析 */

        // 初始化http请求
        String httpRequest = "";

        // 创建字节数组
        byte[] httpRequestBytes = new byte[2048];

        // 初始化http请求长度
        int length = 0;

        // 判断http请求长度
        if ((length = inputStream.read(httpRequestBytes)) > 0) {
            httpRequest = new String(httpRequestBytes, 0, length);
        }

        // 分割请求头
        String httpHead = httpRequest.split("\n")[0];

        // 分割url
        url = httpHead.split("\\s")[1];

        //分割请求方法
        method = httpHead.split("\\s")[0];

        // 测试代码
        System.out.println(this);

    }

}
