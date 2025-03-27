import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;
    public PointSET(){
        pointSet = new TreeSet<>();
    }
    public boolean isEmpty(){
        return pointSet.isEmpty();
    }
    public int size(){
        return pointSet.size();
    }

    public void insert(Point2D p){
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        pointSet.add(p);
    }

    public boolean contains(Point2D p){
        if (p == null) throw new IllegalArgumentException("Point cannot be null");

        return pointSet.contains(p);
    }

    public void draw(){
        for(Point2D p2D : pointSet){
            p2D.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null");
        List<Point2D> pointList = new ArrayList<>();
        for(Point2D p2D : pointSet){
            if(rect.contains(p2D)){
                pointList.add(p2D);
            }
        }
        return pointList;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (isEmpty()) return null;
        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : pointSet) {
            double distance = p.distanceSquaredTo(point);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
}