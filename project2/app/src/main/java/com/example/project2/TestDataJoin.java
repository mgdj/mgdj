package com.example.project2;

import java.util.ArrayList;
import java.util.List;

public class TestDataJoin {
    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("阿根廷夺冠", "524.6w"));
        result.add(new TestData("意大利夺冠", "433.6w"));
        result.add(new TestData("中国世界杯夺冠", "357.8w"));
        result.add(new TestData("中国人最难忘的奥运瞬间", "333.6w"));
        result.add(new TestData("港大不再承认学生会在校内的角色", "285.6w"));
        result.add(new TestData("字节跳动牛逼", "183.2w"));
        result.add(new TestData("浙江大学牛逼", "139.4w"));
        result.add(new TestData("zju-bytedancer牛逼", "75.6w"));
        result.add(new TestData("英格兰罚丢三粒点球", "55w"));
        result.add(new TestData("字节跳动yyds", "43w"));
        return result;
    }
}
