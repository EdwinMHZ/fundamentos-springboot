package com.fundamentos.springboot.fundamentos.bean;

public class MyBeanI2mplement implements MyBean{
    @Override
    public void print() {
        System.out.println("Hola desde mi implementación propia del bean 2");
    }
}
