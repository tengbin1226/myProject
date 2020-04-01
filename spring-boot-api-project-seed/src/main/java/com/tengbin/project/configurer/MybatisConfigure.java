package com.tengbin.project.configurer;

import com.github.pagehelper.PageHelper;
import com.tengbin.project.core.constant.ProjectConstant;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Mybatis & Mapper & PageHelper 配置
 *
 * @author TengBin
 */
@Configuration
public class MybatisConfigure {

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        // 实例化
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置属性
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(ProjectConstant.MODEL_PACKAGE);

        // 配置分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();

        // 分页尺寸为0时查询所有纪录不再执行分页
        properties.setProperty("pageSizeZero", "true");
        // 页码<=0 查询第一页，页码>=总页数查询最后一页
        properties.setProperty("reasonable", "true");
        // 支持通过 Mapper 接口参数来传递分页参数
        properties.setProperty("supportMethodsArguments", "true");

        pageHelper.setProperties(properties);

        // 添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});

        // 添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        // 实例化
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 设置属性
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setBasePackage(ProjectConstant.MAPPER_PACKAGE);

        // TODO 配置通用Mapper
        Properties properties = new Properties();
        // 设置mapper文件所在包
        properties.setProperty("mappers", ProjectConstant.MAPPER_INTERFACE_REFERENCE);
        // insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
        properties.setProperty("notEmpty", "false");
        // 设置数据库标识
        properties.setProperty("IDENTITY", "MYSQL");

        mapperScannerConfigurer.setProperties(properties);

        return mapperScannerConfigurer;
    }


}
