package com.digubird;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableHystrix
@EnableCaching
@EnableScheduling
public class IndexGatherStoreApplication {
    public static void main(String[] args) {
        int eurekaServerPort = 8761;
        int port = 8763;
        int redisPort = 6379;

        if (null != args) {
            for (String arg : args) {
                if (arg.startsWith("port=")) {
                    String strPort = StrUtil.subAfter(arg, "port=", true);
                    if (NumberUtil.isNumber(strPort)) {
                        port = Convert.toInt(strPort);
                    }
                }
            }
        }
        if (NetUtil.isUsableLocalPort(redisPort)) {
            System.err.printf("端口%d未使用，Redis服务可能未开启%n,进程已终止", redisPort);
            System.exit(1);
        }

        if (!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了，无法启动%n", port);
            System.exit(1);
        }

        if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.printf("端口%d 未启用，eureka 服务器未启动%n", eurekaServerPort);
            System.exit(1);
        }

        new SpringApplicationBuilder(IndexGatherStoreApplication.class).properties("server.port=" + port).run(args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
