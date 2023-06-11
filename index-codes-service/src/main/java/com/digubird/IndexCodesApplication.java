package com.digubird;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@SpringBootApplication
@EnableCaching
public class IndexCodesApplication {
    public static void main(String[] args) {
        //8764为当前服务网关使用
        //端口，8765到8770预留给当前微服务
        int port = 0;
        int defaultPort = 8765;
        int redisPort = 6379;
        int eurekaServerPort = 8761;

        if (NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.out.printf("端口%d未被使用，eurekaServer未开启，进程启动失败", eurekaServerPort);
            System.exit(1);
        }
        if (NetUtil.isUsableLocalPort(redisPort)) {
            System.out.printf("端口%d未被使用，redis未开启，进程启动失败", redisPort);
            System.exit(1);
        }

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
        if (0 == port) {
            Future<Integer> future = ThreadUtil.execAsync(() -> {
                int p;
                System.out.printf("请于5秒钟内输入端口号, 推荐 8765-8770 ,超过5秒将默认使用 %d %n", defaultPort);
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String strPort = scanner.nextLine();
                    if (!NumberUtil.isInteger(strPort)) {
                        System.err.println("只能是数字");
                    } else {
                        p = Convert.toInt(strPort);
                        scanner.close();
                        break;
                    }
                }
                return p;
            });
            try {
                port = future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                port = defaultPort;
            }
        }

        if (!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("端口%d被占用了，无法启动%n", port);
            System.exit(1);
        }
        new SpringApplicationBuilder(IndexCodesApplication.class).properties("server.port=" + port).run(args);
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
