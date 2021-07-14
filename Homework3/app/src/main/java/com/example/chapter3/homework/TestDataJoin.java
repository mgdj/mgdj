package com.example.chapter3.homework;

import java.util.ArrayList;
import java.util.List;

public class TestDataJoin {
    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("阿菲:", "哈哈哈", "22：12"));
        result.add(new TestData("阿万:", "带个宵夜", "22：11"));
        result.add(new TestData("阿颜:", "basketball?","22：00"));
        result.add(new TestData("阿包:", "吃饭？","21：59"));
        result.add(new TestData("阿豪:", "笑死","21：55"));
        result.add(new TestData("字节跳动:", "谢谢老师", "21：45"));
        result.add(new TestData("浙江大学:", "谢谢学校","21：33"));
        result.add(new TestData("zju-bytedancer:", "谢谢助教","21：30"));
        result.add(new TestData("阿荣:", "你好！","21：22"));
        result.add(new TestData("阿珑:", "来game","21：20"));
        return result;
    }
}
