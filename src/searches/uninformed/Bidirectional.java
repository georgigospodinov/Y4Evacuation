package searches.uninformed;

import problem.Intersection;
import problem.Node;
import searches.general.GraphSearch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import static gui.Display.BREAK;

public class Bidirectional extends GraphSearch {
    private boolean fromTop = true;
    private LinkedList<Node> frontInit, frontGoal;

    public Bidirectional(Consumer<GraphSearch> statusUpdater) {
        super(statusUpdater);
    }

    private static Node findNodeAt(LinkedList<Node> frontier, Intersection i) {
        for (Node n : frontier)
            if (n.intersection.equals(i))
                return n;

        return null;
    }

    private Node connectionItoG = null, connectionGtoI = null;

    @Override
    protected void createFrontier() {
        frontInit = new LinkedList<>();
        frontInit.add(new Node(Intersection.init));

        frontGoal = new LinkedList<>();
        frontGoal.add(new Node(Intersection.goal));
    }

    @Override
    protected void addToFrontier(Node child) {
        if (fromTop) frontInit.add(child);
        else frontGoal.add(child);
    }

    @Override
    protected Node getNext() {
        return fromTop ? frontInit.pollFirst() : frontGoal.pollFirst();
    }

    @Override
    protected Collection<Node> getFrontier() {
        LinkedList<Node> frontier = new LinkedList<>(frontInit);
        frontier.addAll(frontGoal);
        return frontier;
    }

    @Override
    public String stringFrontier() {
        StringBuilder sb = new StringBuilder("from init {");

        frontInit.forEach(node -> {
            char id = node.intersection.id;
            sb.append(id).append(",");
        });
        if (!frontInit.isEmpty())
            sb.deleteCharAt(sb.length() - 1);
        sb.append("}").append(BREAK);

        sb.append("&#09;from goal {");
        frontGoal.forEach(node -> {
            char id = node.intersection.id;
            sb.append(id).append(",");
        });
        if (!frontGoal.isEmpty())
            sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    private void didConnect() {
        if (connectionItoG == null) return;

        // these two nodes should be at the same intersection
        Node resolution = connectionItoG;
        Node p = connectionGtoI.parent;
        while (p != null) {
            resolution = new Node(p.intersection, resolution);
            p = p.parent;
        }
        endSearchAt(resolution);
    }

    @Override
    public void nextStep() {
        Node current = getNext();
        if (current == null) {
            endSearchAt(current);
            return;
        }
        lastNode = current;

        if (fromTop) {
            if (current.intersection.isGoal()) {
                endSearchAt(current);
                return;
            }

            connectionGtoI = findNodeAt(frontGoal, current.intersection);
            if (connectionGtoI != null)
                connectionItoG = current;
            else expand(current);

        }

        else {
            if (current.intersection.isInit()) {
                endSearchAt(current);
                return;
            }

            connectionItoG = findNodeAt(frontInit, current.intersection);
            if (connectionItoG != null)
                connectionGtoI = current;
            else expand(current);
        }
        showProgress();
        visited.add(current.intersection);
        didConnect();

        // set to go from other direction on next call.
        fromTop = !fromTop;
    }
}
