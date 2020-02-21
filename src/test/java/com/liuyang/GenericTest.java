package com.liuyang;

import com.liuyang.business.pojo.vo.UserVo;
import com.liuyang.business.pojo.vo.UserVoVo;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class GenericTest {

    @Test
    public void test1() {
        Map<String, ? extends UserVo> map = this.getMap();
        UserVo a = map.get("a");
    }

    private Map<String, ? extends UserVo> getMap() {
        return Collections.singletonMap("a", new UserVoVo());
    }
}
