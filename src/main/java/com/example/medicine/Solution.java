package com.example.medicine;

import java.util.*;


public class Solution {
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     *
     * @param strs string字符串一维数组
     * @return string字符串
     */
    public static void main(String[] args) {
        String[] strs = {"abca","abc","abca","abc","abcc"};
        System.out.println(new Solution().longestCommonPrefix(strs));
//        String str  = "flower";
//        String str1  = "flow";
//
//        String tem = "flow";
//        System.out.println(tem.indexOf(str));
//        System.out.println(tem.indexOf(str1));

    }
    public String longestCommonPrefix (String[] strs) {
        if (strs.length == 0) return "";
        String tmp = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(tmp) != 0) { //如果返回的索引位置不是0

                tmp = tmp.substring(0, tmp.length() - 1);

            }

        }
        return tmp;//一直在缩短tmp,直至缩短到公共前缀
    }
}