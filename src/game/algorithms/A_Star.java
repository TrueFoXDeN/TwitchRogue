package game.algorithms;

import game.Dir;
import game.arena.Maze;
import geometry.Vector2f;

import java.util.*;

public class A_Star {

    private Node nodes[];

    public A_Star(Maze m) {
        this.maze = m;
        generateGraph(m);
    }
    private Maze maze;

    private void generateGraph(Maze m) {
        nodes = new Node[m.width * m.height];
        for (int i = 0; i < m.width; i++) {
            for (int j = 0; j < m.height; j++) {
                Node current = new Node(new Vector2f(i, j));
                nodes[i + j * m.width] = current;
            }
        }

        for (int i = 0; i < m.width; i++) {
            for (int j = 0; j < m.height; j++) {
                Node current = nodes[new Vector2f(i, j).to1DIndex(m.width)];

                if (m.canMove(current.pos, Dir.NORTH))
                    current.addNeighbor(nodes[current.pos.add_(new Vector2f(0, -1)).to1DIndex(m.width)]);
                if (m.canMove(current.pos, Dir.SOUTH))
                    current.addNeighbor(nodes[current.pos.add_(new Vector2f(0, 1)).to1DIndex(m.width)]);
                if (m.canMove(current.pos, Dir.EAST))
                    current.addNeighbor(nodes[current.pos.add_(new Vector2f(1, 0)).to1DIndex(m.width)]);
                if (m.canMove(current.pos, Dir.WEST))
                    current.addNeighbor(nodes[current.pos.add_(new Vector2f(-1, 0)).to1DIndex(m.width)]);
            }
        }
    }

    private void resetNodes() {
        for(Node n: nodes) {
            n.f = 0;
            n.g = 0;
            n.parent = null;
        }
    }

    public List<Dir> a_star(Vector2f startPos, Vector2f endPos) {
        resetNodes();

        Node start = nodes[startPos.to1DIndex(maze.width)], end = nodes[endPos.to1DIndex(maze.width)];

        PriorityQueue<Node> openList = new PriorityQueue<>(100,
                (Node n1, Node n2) -> Double.compare(n1.f, n2.f));

        Set<Node> closedList = new HashSet<>(10);

        openList.add(start);

        while(!openList.isEmpty()) {
            Node node = openList.poll();

            if(node.equals(end)) {
                break;
            }

            Set<Node> nodes = node.neighbors.keySet();

            for(Node n: nodes) {
                openList.remove(n);

                if(!closedList.contains(n)) {
                    n.parent = node;
                    n.calcF(end);
                    openList.add(n);
                }
            }
            closedList.add(node);
        }

        Node node = end;
        List<Dir> dirs = new LinkedList<>();
        while(node.parent != null) {
            dirs.add(Dir.vec2fToDir(node.parent.pos.sub_(node.pos)));
            node = node.parent;
        }

        return dirs;
    }

    private class Path {
        Node n1, n2;
        double weight;

        Path(Node n1, Node n2, double weight) {
            this.n1 = n1;
            this.n2 = n2;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof Path
                    && ((Path) obj).n1.equals(n1)
                    && ((Path) obj).n2.equals(n2)
                    && ((Path) obj).weight == weight;
        }
    }

    private class Node {

        double f = .0;
        double g = .0;

        Vector2f pos;
        Map<Node, Path> neighbors = new HashMap<>();

        Node parent;

        Node(Vector2f pos) {
            this.pos = pos;
        }

        void calcF(Node end) {
            g = calcG();
            f = huristic(end) + g;
        }

        double calcG() {
            if (neighbors.get(parent) != null && parent != null) {
                return neighbors.get(parent).weight + parent.g;
            }
            throw new IllegalStateException("Node that should calculate g has no parent");
        }

        double huristic(Node end) {
            return pos.dist(end.pos);
        }

        void addNeighbor(Node n) {
            neighbors.put(n, new Path(this, n, 1));
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof Node
                    && ((Node) obj).pos.equals(pos);
        }
    }

}
