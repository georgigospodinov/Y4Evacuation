package searches.general;

import problem.Intersection;
import problem.Node;
import util.Props;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class GraphSearch {
    public static final String H_EUCLIDEAN = "euclidean";
    public static final String H_MANHATTAN = "manhattan";
    public static final String H_CHEBYSHEV = "chebyshev";
    public static final String H_TO_USE;

    static {
        String hGiven = Props.getString("heuristic to use");
        if (H_CHEBYSHEV.equals(hGiven))
            H_TO_USE = H_CHEBYSHEV;
        else if (H_MANHATTAN.equals(hGiven))
            H_TO_USE = H_MANHATTAN;
        else H_TO_USE = H_EUCLIDEAN;
    }

    protected final LinkedHashSet<Intersection> visited = new LinkedHashSet<>();
    private boolean searching;
    private Node solution;
    protected Node lastNode;
    private Consumer<GraphSearch> statusUpdater;

    public LinkedHashSet<Intersection> getVisited() {
        return new LinkedHashSet<>(visited);
    }

    public LinkedHashSet<Intersection> getFrontierIntersections() {
        LinkedHashSet<Intersection> frontier = new LinkedHashSet<>();
        getFrontier().forEach(n -> frontier.add(n.intersection));
        return frontier;
    }

    protected abstract void createFrontier();

    protected abstract Collection<Node> getFrontier();

    protected abstract void addToFrontier(Node child);

    protected abstract Node getNext();

    public Node getLastNode() {
        return lastNode;
    }

    public boolean isSearching() {
        return searching;
    }

    public GraphSearch(Consumer<GraphSearch> statusUpdater) {
        createFrontier();
        searching = true;
        this.statusUpdater = statusUpdater;
    }

    public double heuristic(Node n) {
        return n == null ? -1 : 1;
    }

    public String stringVisited() {
        StringBuilder sb = new StringBuilder("{");
        visited.forEach(intersection -> sb.append(intersection.id).append(","));
        sb.append("}");
        return sb.toString();
    }

    public String stringFrontier() {
        StringBuilder sb = new StringBuilder("{");
        getFrontier().forEach(node -> {
            char id = node.intersection.id;
            sb.append(id).append(",");
        });
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    protected void showProgress() {
        statusUpdater.accept(this);
    }

    protected void endSearchAt(Node solution) {
        this.solution = solution;
        searching = false;
        showProgress();
    }

    public void nextStep() {
        Node current = getNext();
        lastNode = current;

        // Check failure.
        if (current == null) {
            endSearchAt(current);
            return;
        }

        // Check goal reached?
        if (current.intersection.isGoal()) {
            endSearchAt(current);
            return;
        }

        expand(current);
        showProgress();
        visited.add(current.intersection);
    }

    /**
     * Back tracks from the given {@link Node} to the root, stacking the {@link Intersection}s that it goes through.
     * The returned list can be iterated using its own iterator to obtain the correct sequence.
     * This method will return null if the 'solution' {@link Node} is null.
     *
     * @return a list of {@link Intersection}s that the robot needs to go through to reach the goal
     */
    public LinkedList<Intersection> backTrack() {
        if (solution == null) return null;

        LinkedList<Intersection> path = new LinkedList<>();
        Node current = solution;
        while (current != null) {
            path.addFirst(current.intersection);
            current = current.parent;
        }

        return path;
    }

    protected void expand(Node current) {
        current.forEachChild(child -> {
            // Skip visited intersections.
            if (!visited.contains(child.intersection))
                addToFrontier(child);
        });
    }

}
