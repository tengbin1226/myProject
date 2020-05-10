package pers.tengbin.mytomcat.handle;

import lombok.Data;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName:MyResponse
 * @Description: 封装响应对象. 基于HTTP协议的格式进行输出写入。
 * @Author Mr.T
 * @date 2020/5/7 13:55
 */
@Data
public class MyResponse {

    private OutputStream outputStream;

    public MyResponse(OutputStream outputStream){
        this.outputStream=outputStream;
    }

    public void write(String content) throws IOException{
        // 初始化http响应请求
        StringBuffer httpResponse=new StringBuffer();

        // 拼接响应请求
        httpResponse.append("HTTP/1.1 200 OK\n").append("Content-Type: text/html\n").append("\r\n").append("<html<body>")
                .append(content).append("</body></html>");

        outputStream.write(httpResponse.toString().getBytes());

        // 关闭流
        outputStream.close();
    }

}
