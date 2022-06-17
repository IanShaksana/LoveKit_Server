package com.example.demo.n_model.geofence;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private Point[] points;
    private List<Point> point_list = new ArrayList<>();

    public Polygon(Point[] points) {
        this.points = points;
    }

    public Polygon(List<Point> point_list) {
        this.point_list = point_list;
    }

    public Point[] getPoints() {
        return points;
    }

    public List<Point> getPoint_List() {
        return point_list;
    }

}