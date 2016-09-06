package cn.buptteam.calSimilarityByVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luyao-Li on 2016/9/5.
 */
public class CalVector {
    public double getSimilarity(ArrayList<String> list1, ArrayList<String> list2) {
        double result=0;
        CalVector calVector = new CalVector();
        HashMap<String, Integer> map1 = calVector.transToMap(list1);
        HashMap<String, Integer> map2 = calVector.transToMap(list2);

        if (list1.size() + list2.size() == 0) {
            return -1;
        }
        if (list1.size() * list2.size() == 0) {
            return 0;
        }

        int sum = 0;
        int mod1 = 0, mod2 = 0;
        for (Map.Entry<String, Integer> entry : map1.entrySet()) {
            String key = entry.getKey();
            if (map2.containsKey(key)) {
                sum += map1.get(key) * map2.get(key);
            }
            mod1 += entry.getValue() * entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : map2.entrySet()){
            mod2 += entry.getValue() * entry.getValue();
        }
        result = sum / (Math.sqrt(mod1) * Math.sqrt(mod2));
        return result;
    }

    public  HashMap<String, Integer> transToMap(ArrayList<String> list) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String s : list) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) + 1);
            } else {
                map.put(s, 1);
            }
        }
        return map;
    }

    private static ArrayList<String> trans(String s) {//-d
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++) {
            list.add(s.charAt(i) + "");
            System.out.print(s.charAt(i)+" ");
        }
        return list;
    }

    public static void main(String[] args) {//-test
        String s1 ="闯红灯交通安全";
        String s2 = "压黄线人身安全";
        String s3 = "撞车财产安全";

        //System.out.println(getSimilarity(trans(s1), trans(s2)));
       // System.out.println(getSimilarity(trans(s2), trans(s3)));
        //System.out.println(getSimilarity(trans(s3), trans(s1)));
    }
}
