package cn.buptteam.test;

import com.hankcs.hanlp.HanLP;

/**
 * Created by bitholic on 16/9/8.
 */
public class SegmentTest {
    public static void main(String[] args) {
//        System.out.println(HanLP.segment("高速公路上肇事逃逸要承担什么样的后果？"));
//        System.out.println(HanLP.segment("交通肇事逃逸是一种特别恶劣的交通违法行为，要承担更为严厉的法律后果。"));
//
//        System.out.println(HanLP.extractKeyword("高速公路上肇事逃逸要承担什么样的后果？", 10));
        System.out.println(HanLP.parseDependency("我的天!"));
    }


}
