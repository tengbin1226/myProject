package com.tengbin.project;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单元测试继承该类即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootApiProjectSeedApplication.class)
// 事务
@Transactional
// 回滚
@Rollback
class Tester {

    @Test
    void contextLoads() {
    }

}
