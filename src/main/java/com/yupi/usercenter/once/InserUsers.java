package com.yupi.usercenter.once;

import com.yupi.usercenter.mapper.UserMapper;
import com.yupi.usercenter.model.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import javax.annotation.Resource;

@Component
public class InserUsers {

    @Resource
    private UserMapper userMapper;

    /**
     *  批量插入用户
     */
//    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void doInserUsers() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
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
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
