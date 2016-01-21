package com.example.redassistant;

/**
 *   用作获取手指按下当前的坐标位置
 * @author helinlin
 *
 */
public class Point {
	public Point() {
		// TODO Auto-generated constructor stub
	}
	public int posX;//点击的横坐标
	public int posY;//点击屏幕的纵坐标
	public void setX(int X) {
		posX = X;
	}
	public void setY(int Y) {
		posY = Y;
	}
	public int getX() {
		return posX;
	}
	public int getY() {
		return posY;
	}
}
