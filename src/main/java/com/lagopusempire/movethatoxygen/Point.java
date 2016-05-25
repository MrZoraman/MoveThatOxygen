package com.lagopusempire.movethatoxygen;

public class Point {

    public Point(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }
    public int X;
    public int Y;

    public boolean equals(Point p) {
        return (p.X == X && p.Y == Y);
    }
}
