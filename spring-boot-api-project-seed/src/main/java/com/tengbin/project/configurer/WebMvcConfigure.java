package com.tengbin.project.configurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.tengbin.project.core.Result;
import com.tengbin.project.core.ResultCode;
import com.tengbin.project.core.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring MVC 配置
 *
 * @author TengBin
 */
@Configuration
@PropertySource("classpath:application.yml")
public class WebMvcConfigure implements WebMvcConfigurer {

    /**
     * 获取当前类日志并打印
     */
    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigure.class);

    /**
     * 当前激活的配置文件
     */
    @Value("${spring.profiles.active:#{dev}}")
    private String active;

    /**
     * 配置自定义消息转换器(使用阿里FastJson作为 Json消息转换器)
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 实例化convert转换消息的对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        // 添加fastjson配置消息. 如: 格式化返回json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);

        // 处理中文乱码问题(不然出现中文乱码)
        List<MediaType> mediaTypes = Lists.newArrayList();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;

        converters.add(converter);
    }

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /* 接口签名认证,该签名认证比较简单. 实际项目开发中可以使用Json Web Token(jwt) 或更好的方式替代 */
        if (!"dev".equals(active)) {
            registry.addInterceptor(new HandlerInterceptor() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    // 验证签名
                    boolean pass = validateSign(request);
                    if (pass) {
                        return true;
                    } else {
                        logger.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));

                        Result result = new Result();
                        result.setCode(ResultCode.UNAUTHORIZED).setMessage("签名认证失败");
                        responseResult(response, result);
                        return false;
                    }
                }
            });
        }
    }

    /**
     * 静态资源处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 配置视图解析器
     *
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }

    /**
     * 页面跳转
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // 重定向到指定url
        RedirectViewControllerRegistration redirectViewControllerRegistration = registry.addRedirectViewController("/urlPath", "/redirectUrl");

    }

    /**
     * 统一异常处理
     *
     * @param resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                // 实例化返回结果对象
                Result result = new Result();

                //业务失败的异常，如“账号或密码错误”
                if (e instanceof ServiceException) {
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                    logger.info(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error(message, e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }
        });
    }

    /**
     * 解决跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许指定的pathPattern可以进行跨域请求
        CorsRegistration corsRegistration = registry.addMapping("/pathPattern");

        // 设置允许哪些可以进行跨域访问，设置为"*"表示允许所有
        // 默认设置为允许所有
        corsRegistration.allowedOrigins("http://domain1.com", "http://domain2.com");

        // 设置允许的跨域请求动作，设置为"*"表示允许所有
        // 默认设置为允许简单动作，包括GET POST HEAD
        corsRegistration.allowedMethods("GET", "POST");

        // 设置允许的请求头，默认设置为允许所有，即"*"
        corsRegistration.allowedHeaders("Cache-Control", "Content-Language");

        // 设置response的头结构，不支持"*"
        corsRegistration.exposedHeaders("Cache-Control", "Content-Language");

        // 设置浏览器是否需要发送认证信息
        corsRegistration.allowCredentials(true);

        // 设置客户端保存pre-flight request缓存的时间
        // pre-flight request 预检请求
        corsRegistration.maxAge(1);
    }


    /**
     * 请求结果
     *
     * @param response
     * @param result
     */
    private void responseResult(HttpServletResponse response, Result result) {
        // 设置编码
        response.setCharacterEncoding("UTF-8");
        // 设置请求头
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        // 设置状态码
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        // 获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        String requestSign = request.getParameter("sign");
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }

        List<String> keys = new ArrayList<>(request.getParameterMap().keySet());

        // 排除sign参数
        keys.remove("sign");

        // 排序
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();

        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//拼接字符串
        }

        String linkString = sb.toString();

        // 去除最后一个'&'
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);

        // 密钥，自己修改
        String secret = "Potato";

        // 混合密钥md5
        String sign = DigestUtils.md5Hex(linkString + secret);

        // 比较
        return StringUtils.equals(sign, requestSign);
    }


    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        // 获取请求中的ip地址
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }

}
