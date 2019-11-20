package com.luoye.pintu;

public class Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }//	获得触摸点在当前 View 的 X 轴坐标

    public int getY() {
        return y;
    }//获得触摸点在当前 View 的 Y 轴坐标。

    private int x=0;
    private int y=0;
}
