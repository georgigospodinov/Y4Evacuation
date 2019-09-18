package problem;

import java.util.function.Consumer;

import static searches.general.GraphSearch.*;

public class Node {
    public final Intersection intersection;
    public final Node parent;

    private final double euclideanFromParent, euclideanToGoal, euclideanFromInit;
    private final double manhattanFromParent, manhattanToGoal, manhattanFromInit;
    private final double chebyshevFromParent, chebyshevToGoal, chebyshevFromInit;

    private double totalEuclidean() {
        double d = euclideanFromParent;
        Node c = parent;
        while (c != null) {
            d += c.euclideanFromParent;
            c = c.parent;
        }

        return d;
    }

    private double totalManhattan() {
        double d = manhattanFromParent;
        Node c = parent;
        while (c != null) {
            d += c.manhattanFromParent;
            c = c.parent;
        }

        return d;
    }

    private double totalChebyshev() {
        double d = chebyshevFromParent;
        Node c = parent;
        while (c != null) {
            d += c.chebyshevFromParent;
            c = c.parent;
        }

        return d;
    }

    public double getHeuristicFromInit() {
        switch (H_TO_USE) {
            case H_CHEBYSHEV:
                return chebyshevFromInit;
            case H_MANHATTAN:
                return manhattanFromInit;
            default:
                return euclideanFromInit;
        }
    }

    public double getHeuristicToGoal() {
        switch (H_TO_USE) {
            case H_CHEBYSHEV:
                return chebyshevToGoal;
            case H_MANHATTAN:
                return manhattanToGoal;
            default:
                return euclideanToGoal;
        }
    }

    public Node(Intersection intersection, Node parent) {
        this.intersection = intersection;
        this.parent = parent;
        if (parent != null) {
            this.euclideanFromParent = Intersection.euclidean(this.intersection, parent.intersection);
            this.manhattanFromParent = Intersection.manhattan(this.intersection, parent.intersection);
            this.chebyshevFromParent = Intersection.chebyshev(this.intersection, parent.intersection);
        }
        else {
            this.euclideanFromParent = 0;
            this.manhattanFromParent = 0;
            this.chebyshevFromParent = 0;
        }

        euclideanToGoal = Intersection.euclidean(intersection, Intersection.goal);
        manhattanToGoal = Intersection.manhattan(intersection, Intersection.goal);
        chebyshevToGoal = Intersection.chebyshev(intersection, Intersection.goal);

        euclideanFromInit = totalEuclidean();
        manhattanFromInit = totalManhattan();
        chebyshevFromInit = totalChebyshev();
    }

    public Node(Intersection intersection) {
        this(intersection, null);
    }

    public void forEachChild(Consumer<? super Node> action) {
        intersection.forEachConnected(i -> {
            Node n = new Node(i, this);
            action.accept(n);
        });
    }

}
