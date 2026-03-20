package com.example.bp.analysis.model;

import org.opencv.core.Point;

public record Circle(Point center, double radius) {
    public double x() { return center.x; }
    public double y() { return center.y; }

    public double radius() { return radius; }
}
