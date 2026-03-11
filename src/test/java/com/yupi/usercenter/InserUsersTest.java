package com.yupi.usercenter;

import com.yupi.usercenter.mapper.UserMapper;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class InserUsersTest {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;

    // 自定义线程池
    // CPU 密集型 : 分配的核心线程数 = CPU - 1
    // IO  密集型 : 分配的核心线程数可以大于 CPU 核数
    private ExecutorService executorService = new ThreadPoolExecutor(
            60,
            1000,
            10000,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10000));
    /**
     *  批量插入用户
     */
    @Test
    public void doInserUsers() {

        // mysql redis
        // Java 基础 集合 并发
        // Springboot
        // 消息队列
        // Linux

        final int INSERT_NUM = 1000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分十组
        int j = 0;
        // 提交异步任务，使用最终变量
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<User> userList = Collections.synchronizedList( new ArrayList<>() ) ;
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假用户" + i);
                user.setUserAccount("fakeuser" + i);
                user.setAvatarUrl("https://example.com/avatar" + i + ".jpg");
                user.setGender(i % 2);
                user.setUserPassword("12345678");
                user.setPhone("1380000000" + i);
                user.setEmail("fakeuser" + i + "@example.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("planet" + i);
                user.setTags("[]");
                userList.add(user);

                if (j % INSERT_NUM == 0) {
                    break;
                }
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("ThreadName"+Thread.currentThread().getName());
                userService.saveBatch(userList,20);
            },executorService); // 提交到自定义线程池

            futureList.add(future);
        }

        // 等待所有异步任务完成
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

//        // 20个为一个批次
//        userService.saveBatch(userList,20);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
