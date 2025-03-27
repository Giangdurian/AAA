import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;
    private int size;

    private static class Node{
        private Point2D point;
        private Node left;
        private Node right;
        private boolean cut;

        public Node (Point2D point){
            this.point = point;
        }
    }

    public KdTree(){
        root = null;
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    /*
    * Trường hợp trung bình: O(log n) (nếu cây cân bằng).
    *Trường hợp xấu nhất: O(n) (nếu cây suy biến thành linked list).
    *
    * */
    public void insert(Point2D p){
        if(p == null)   throw new IllegalArgumentException("Point cannot be null");
        root = insert(root, p, VERTICAL);
    }

    private Node insert(Node node, Point2D p, boolean cut){
        if(node == null){
            Node newNode = new Node(p);
            newNode.cut = cut;
            size++;
            return newNode;
        }

        if(cut == VERTICAL){
            if(p.x() < node.point.x()){
                node = insert(node.left, p, !cut);
            }else if(p.x() > node.point.x()){
                node = insert(node.right, p, !cut);
            }else if(p.y() != node.point.y()){
                node = insert(node.right, p, !cut);
            }
        }else{
            if(p.y() < node.point.y()){
                node = insert(node.left, p, !cut);
            }else if(p.y() > node.point.y()){
                node = insert(node.right, p, !cut);
            }else if(p.x() != node.point.x()){
                node = insert(node.right, p, !cut);
            }
        }
        return node;
    }

    public boolean contains(Point2D p){
        if(p == null)   throw new IllegalArgumentException("Point cannot be null");
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p){
        while(node != null){
            if(node.cut == VERTICAL){
                if(p.x() < node.point.x()){
                    node = node.left;
                }else if(p.x() > node.point.x()){
                    node = node.right;
                }else if(p.y() != node.point.y()){
                    node = node.right;
                }else{
                    return true;
                }
            }else{
                if(p.y() < node.point.y()){
                    node = node.left;
                }else if(p.y() > node.point.y()){
                    node = node.right;
                }else if(p.x() != node.point.x()){
                    node = node.right;
                }else{
                    return true;
                }
            }
        }
        return false;

    }

    public Iterable<Point2D> range(RectHV rect){
        if(rect == null)    throw new IllegalArgumentException("Error!");
        List<Point2D> results = new ArrayList<>();
        RectHV rootRect = new RectHV(0,0,1,1);
        range(root, rootRect, rect, results);
        return results;
    }

    private void range(Node node, RectHV nodeRect, RectHV queryRect, List<Point2D> pointsInRect){
        if(node == null){
            return;
        }
        if(!nodeRect.intersects(queryRect)){
            return;
        }

        if(queryRect.contains(node.point)){
            pointsInRect.add(node.point);
        }

        if(node.cut == VERTICAL){
            double x = node.point.x();
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), x, nodeRect.ymax());
            RectHV rightRect = new RectHV(x, nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
            range(node.left, leftRect, queryRect, pointsInRect);
            range(node.right, rightRect, queryRect, pointsInRect);
        }else{
            double y = node.point.y();
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), y);
            RectHV rightRect = new RectHV(nodeRect.xmin(), y, nodeRect.xmax(), nodeRect.ymax());
            range(node.left, leftRect, queryRect, pointsInRect);
            range(node.right, rightRect, queryRect, pointsInRect);
        }
    }

    public Point2D nearest(Point2D queryPoint){
        if(queryPoint == null)   throw new IllegalArgumentException("Error!");
        if(isEmpty())   return null;

        RectHV rootRect = new RectHV(0,0,1,1);
        return nearest(root, rootRect, root.point, queryPoint);
    }

    private Point2D nearest(Node node, RectHV nodeRect, Point2D closet, Point2D queryPoint){
        if(node == null){
            return closet;
        }
        double currentDistance = queryPoint.distanceSquaredTo(node.point);
        double closetDistance = queryPoint.distanceSquaredTo(closet);
        if(currentDistance < closetDistance){
            closet = node.point;
        }

        if(node.cut == VERTICAL){
            double x = node.point.x();
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), x, nodeRect.ymax());
            RectHV rightRect = new RectHV(x, nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());

            if(queryPoint.x() <= x){
                closet = nearest(node.left, leftRect, closet, queryPoint);
                if(rightRect.distanceSquaredTo(queryPoint) < closetDistance){
                    closet = nearest(node.right, rightRect, closet, queryPoint);
                }
            }else{
                closet = nearest(node.right, rightRect, closet, queryPoint);
                if(leftRect.distanceSquaredTo(queryPoint) < closetDistance){
                    closet = nearest(node.left, leftRect, closet, queryPoint);
                }
            }
        }else{
            double y = node.point.y();
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), y);
            RectHV rightRect = new RectHV(nodeRect.xmin(), y, nodeRect.xmax(), nodeRect.ymax());

            if(queryPoint.y() <= y){
                closet = nearest(node.left, leftRect, closet, queryPoint);
                if(rightRect.distanceSquaredTo(queryPoint) < closetDistance){
                    closet = nearest(node.right, rightRect, closet, queryPoint);
                }
            }else{
                closet = nearest(node.right, rightRect, closet, queryPoint);
                if(leftRect.distanceSquaredTo(queryPoint) < closetDistance){
                    closet = nearest(node.left, leftRect, closet, queryPoint);
                }
            }
        }

        return closet;
    }

    public void draw() {
        draw(root, 0.0, 0.0, 1.0, 1.0);
    }

    private void draw(Node node, double xmin, double ymin, double xmax, double ymax) {
        if (node == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        if (node.cut == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            // draw red vertical line
            RectHV rect = new RectHV(node.point.x(), ymin, node.point.x(), ymax);
            rect.draw();
            draw(node.right, node.point.x(), ymin, xmax, ymax);
            draw(node.left, xmin, ymin, node.point.x(), ymax);
        }

        if (node.cut == HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            // draw blue horizontal line
            RectHV rect = new RectHV(xmin, node.point.y(), xmax, node.point.y());
            rect.draw();
            draw(node.right, xmin, node.point.y(), xmax, ymax);
            draw(node.left, xmin, ymin, xmax, node.point.y());
        }
    }
}
