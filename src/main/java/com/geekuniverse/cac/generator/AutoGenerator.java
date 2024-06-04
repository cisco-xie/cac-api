package com.geekuniverse.cac.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.TypeConverts;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutoGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://47.114.220.201:3306/cac", "nwtadmin", "nwtsoft2020")
            .typeConvert(new MySqlTypeConvert(){
                @Override
                public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                    System.out.println("转换类型：" + fieldType);
                    // tinyint转换成Integer
                    if (fieldType.toLowerCase().contains("tinyint")) {
                        return DbColumnType.INTEGER;
                    }
                    return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
                }
            });


    /**
     * 执行
     */
    public static void main(String[] args) throws SQLException {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author("谢诗宏") // 设置作者
                            .enableSwagger() // 不开启 swagger 模式
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 指定输出目录
                            .disableOpenDir();   //禁止打开输出目录，默认打开
                })
                .packageConfig(builder -> {
                    builder.parent("com.geekuniverse") // 设置父包名
                            .moduleName("cac") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    //实体类相关配置
                    builder.entityBuilder()
                            //.enableTableFieldAnnotation() // 开启实体类字段注解
                            .enableLombok() // 开启lombok
                            .enableChainModel() // 开启链式模式
                            .enableFileOverride(); // 覆盖已生成文件
                })
                .strategyConfig(builder -> {
                    //mapper相关配置
                    builder.mapperBuilder()
                            .enableBaseResultMap() // 开启BaseResultMap
                            .enableBaseColumnList() // 开启BaseColumnList
                            .enableFileOverride(); // 开启覆盖已生成文件
                })
                .strategyConfig(builder -> {
                    //service相关配置
                    builder.serviceBuilder()
                            .enableFileOverride(); // 开启覆盖已生成文件
                })
                .strategyConfig(builder -> {
                    //controller相关配置
                    builder.controllerBuilder()
                            .enableRestStyle()  //开启生成@RestController控制器
                            //.enableHyphenStyle() // 开启驼峰转连字符
                            .enableFileOverride(); // 覆盖已生成文件
                })
                .strategyConfig((scanner, builder) -> {
                    builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all"))) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
