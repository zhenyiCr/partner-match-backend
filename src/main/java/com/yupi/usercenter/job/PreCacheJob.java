package com.yupi.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.usercenter.mapper.UserMapper;
import com.yupi.usercenter.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private List<Long> userIdList = Arrays.asList(1L);

    // 每天凌晨 0 点执行 预热用户
    @Scheduled(cron = "0 0 0 * * ?")
    public void preCacheRecommendUser() {
        RLock lock = redissonClient.getLock("zhenyi:preCacheJob:docache:lock");
        try {
            // 只有一个线程能获取到
            lock.tryLock(0, -1, TimeUnit.SECONDS);
            for (Long userId : userIdList) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                Page<User> userPage = userMapper.selectPage(new Page<>(1, 20), queryWrapper);
                String redisKey = String.format("user:recommend:%s", userId);
                ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

                // 缓存到 Redis 中，过期时间为 10 分钟
                try {
                    valueOperations.set(redisKey, userPage, 10, TimeUnit.MINUTES);
                } catch (Exception e) {
                    log.error("Redis 缓存推荐用户列表失败", e);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 只能释放当前线程的锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
