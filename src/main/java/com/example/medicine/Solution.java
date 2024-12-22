package com.example.medicine;

import java.util.*;


  class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;
    public TreeNode(int val) {
      this.val = val;
    }
  }


public class Solution {

    public static void main(String[] args) {
        int[] preOrder = {1,2,4,7,3,5,6,8};
        int[] vinOrder = {4,7,2,1,5,3,8,6};
        TreeNode treeNode = new Solution().reConstructBinaryTree(preOrder, vinOrder);
    }
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     *
     * @param preOrder int整型一维数组
     * @param vinOrder int整型一维数组
     * @return TreeNode类
     */
    public TreeNode reConstructBinaryTree (int[] preOrder, int[] vinOrder) {
        // write code here
        TreeNode root = reConstructBinaryTree_1(preOrder, 0, preOrder.length - 1,
                                                vinOrder, 0, vinOrder.length - 1);
        return  root;
    }
    // 定义重构二叉树方法
    // 前序遍历{1,2,4,7,3,5,6,8} 和 中序遍历{4,7,2,1,5,3,8,6}
    public TreeNode reConstructBinaryTree_1(int[] pre, int startPre, int endPre,
                                            int[] in, int startIn, int endIn) {
        if (startPre > endPre || startIn > endIn) {
            return null;
        }
        // 创建根节点
        TreeNode root = new TreeNode(pre[startPre]);
        // 给根节点左右节点进行赋值
        for (int i = startIn; i <= endIn; i++) {
            if (in[i] == pre[startPre]) {
                // 给左节点赋值
                root.left = reConstructBinaryTree_1(pre, startPre + 1, startPre + i - startIn,in, startIn, i - 1);
                // 给右节点赋值
                root.right = reConstructBinaryTree_1(pre, i - startIn + startPre + 1, endPre,in, i + 1, endIn);

                break;
            }
        }
        return root;

    }
}