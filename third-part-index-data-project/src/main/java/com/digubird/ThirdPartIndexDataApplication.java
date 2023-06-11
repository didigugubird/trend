package com.digubird;

import cn.hutool.core.net.NetUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ThirdPartIndexDataApplication {
    public static void main(String[] args) {
        int eurekaServerPort = 8761;

        if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.printf("端口%d 未启用，eureka 服务器未启动%n", eurekaServerPort);
            System.exit(1);
        }
        if (!NetUtil.isUsableLocalPort(8762)) {
            System.err.printf("8762端口被占用");
            System.exit(1);
        }

        new SpringApplicationBuilder(ThirdPartIndexDataApplication.class).run(args);

    }

}