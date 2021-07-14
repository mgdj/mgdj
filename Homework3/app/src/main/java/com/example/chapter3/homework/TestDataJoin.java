package com.example.chapter3.homework;

import java.util.ArrayList;
import java.util.List;

public class TestDataJoin {
    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("阿菲", "22:12"));
        result.add(new TestData("阿万", "22：11"));
        result.add(new TestData("阿颜", "22：00"));
        result.add(new TestData("阿包", "21：59"));
        result.add(new TestData("阿豪", "21：55"));
        result.add(new TestData("字节跳动", "21：45"));
        result.add(new TestData("浙江大学", "21：33"));
        result.add(new TestData("zju-bytedancer", "21：30"));
        result.add(new TestData("阿荣", "21：22"));
        result.add(new TestData("阿珑", "21：20"));
        return result;
    }
}
