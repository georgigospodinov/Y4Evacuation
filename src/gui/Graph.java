package gui;

import problem.Intersection;
import util.Props;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Graph extends JComponent {
    private static final double COORD_X_MULTIPLIER = Props.getDouble("coord x multiplier");
    private static final double COORD_Y_MULTIPLIER = Props.getDouble("coord y multiplier");
    private static final double FLAT_X_DISPLACEMENT = Props.getDouble("flat x displacement");
    private static final double FLAT_Y_DISPLACEMENT = Props.getDouble("flat y displacement");

    private int w, h;
    private int circleDiameter;
    private Font ID;
    private Graphics g;
    private Intersection current = Intersection.init;
    private LinkedHashSet<Intersection> frontier = new LinkedHashSet<>();
    private LinkedHashSet<Intersection> visited = new LinkedHashSet<>();
    private LinkedList<Intersection> solution = null;

    private void calculateDimensions() {
        w = getWidth();
        h = getHeight();

        double cd = Props.getDouble("circle diameter");
        circleDiameter = (int) Math.sqrt(w * h * cd * cd / Math.PI);

        int fontSize = (int) (h * Props.getDouble("font size"));
        ID = new Font("Ariel", Font.BOLD, fontSize);
    }

    void setCurrent(Intersection current) {
        this.current = current;
    }

    void setVisited(LinkedHashSet<Intersection> visited) {
        this.visited = visited != null ? visited : new LinkedHashSet<>();
    }

    void setFrontier(LinkedHashSet<Intersection> frontier) {
        this.frontier = frontier != null ? frontier : new LinkedHashSet<>();
    }

    void setSolution(LinkedList<Intersection> solution) {
        this.solution = solution;
        repaint();
    }

    private void prepBackground() {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, w, h);
    }

    private int getDrawingX(Intersection i) {
        return (int) (w * (i.x * COORD_X_MULTIPLIER + FLAT_X_DISPLACEMENT));
    }

    private int getDrawingY(Intersection i) {
        return (int) (h * (i.y * COORD_Y_MULTIPLIER + FLAT_Y_DISPLACEMENT));
    }

    private void drawConnection(Intersection a, Intersection b, Color c) {
        int radius = circleDiameter / 2;
        int x1 = getDrawingX(a) + radius;
        int y1 = getDrawingY(a) + radius;
        int x2 = getDrawingX(b) + radius;
        int y2 = getDrawingY(b) + radius;
        radius *= 0.3;
        // Contract lines, so that they do no overlap with the letters
        if (x1 > x2) {
            x2 += radius;
            x1 -= radius;
        }
        else if (x1 < x2) {
            x2 -= radius;
            x1 += radius;
        }
        if (y1 > y2) {
            y2 += radius;
            y1 -= radius;
        }
        else if (y1 < y2) {
            y2 -= radius;
            y1 += radius;
        }
        g.setColor(c);
        g.drawLine(x1, y1, x2, y2);
    }

    private void drawConnections() {
        Intersection.forEach(i -> {
            i.forEachConnected(c -> {
                drawConnection(i, c, Color.BLACK);
            });
        });
    }

    private void drawIntersection(Intersection i, Color c) {
        int x = getDrawingX(i);
        int y = getDrawingY(i);
        int d = circleDiameter;
        g.setColor(c);
        g.fillOval(x, y, d, d);
        g.setColor(Color.WHITE);
        g.setFont(ID);
        g.drawString(String.valueOf(i.id), x + (int) (d * 0.4), y + (int) (d * 0.6));
    }

    private void drawIntersections() {
        Intersection.forEach(i -> {
            Color c = Color.BLACK;
            if (visited.contains(i))
                c = Color.GRAY;
            if (frontier.contains(i))
                c = Color.BLUE;
            if (i.equals(Intersection.init))
                c = Color.CYAN;
            if (i.equals(Intersection.goal))
                c = Color.RED;
            if (i.equals(current))
                c = Color.GREEN;
            drawIntersection(i, c);
        });
    }

    private void drawSolution() {
        if (solution == null) return;

        Iterator<Intersection> iterator = solution.iterator();
        Intersection previous = iterator.next();
        Color c = Color.ORANGE;
        drawIntersection(previous, c);
        Graphics2D g2 = (Graphics2D) g;
        Stroke base = g2.getStroke();
        Stroke sol = new BasicStroke(5);

        while (iterator.hasNext()) {
            Intersection current = iterator.next();

            g2.setStroke(sol);
            drawConnection(previous, current, c);
            g2.setStroke(base);

            drawIntersection(current, c);

            previous = current;
        }
    }

    public void paintComponent(Graphics g) {
        this.g = g;
        calculateDimensions();
        prepBackground();
        drawConnections();
        drawIntersections();
        drawSolution();
    }
}
