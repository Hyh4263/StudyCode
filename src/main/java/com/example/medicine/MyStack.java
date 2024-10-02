package com.example.medicine;

import java.util.LinkedList;
import java.util.Queue;

class MyStack {
    //定义两个队列
    Queue<Integer> queueOld;
    Queue<Integer> queueNew;

    public static void main(String[] args) {
        MyStack myStack = new MyStack();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);

        myStack.top(); // 返回 2
        myStack.pop(); // 返回 2
        myStack.empty(); // 返回 False
    }
    public MyStack() {
        queueNew = new LinkedList<>();
        queueOld = new LinkedList<>();
    }
    
    public void push(int x) {
        //先将入栈数据存放到新队列
        queueNew.offer(x);
        //如果老队列有数据，将老队列的数据存放到新队列中
        while(!queueOld.isEmpty()){
            queueNew.offer(queueOld.poll());
        }
        //将新队列转换为老队列，老队列转换为新队列->保证新队列永远为空
        //1.定义定时队列
        Queue<Integer> temp = new LinkedList<>();
        //2.新老队列交换
        temp = queueOld;
        queueOld = queueNew;
        queueNew = temp;
    }
                                    
    public int pop() {
       //对新队列进行出队
      return queueOld.poll();
        
    }
    
    public int top() {
        //返回新队列的头部元素
       return queueOld.peek();
    }
    
    public boolean empty() {
        //检查新老队列是否为空
        return  queueNew.isEmpty() && queueOld.isEmpty();
    }
}

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */