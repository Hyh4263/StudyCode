package com.example.medicine;

public class TestStatics {
    public static TestStatics t1 = new TestStatics();

    static {
        System.out.println("静态");
    }

    {
        System.out.println("实例");
    }

    public static TestStatics t2 = new TestStatics();

    public static void main(String[] args) {
        TestStatics t = new TestStatics();
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        System.out.println("ss:" + apiKey);
    }





}
