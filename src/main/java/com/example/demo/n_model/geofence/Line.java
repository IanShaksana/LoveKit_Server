
package com.example.demo.n_model.geofence;

public class Line {
        
    private Point from;
    private Point to;
        
    public Line(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }

}