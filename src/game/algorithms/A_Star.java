package game.algorithms;

import game.Dir;
import game.arena.Maze;
import geometry.Vector2f;

import java.util.*;

public class A_Star {

    private Node nodes[];

    public A_Star(Maze m) {
        generateGraph(m);
    }

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
                Node current = nodes[i + j * m.width];

                if (m.canMove(current.pos, Dir.NORTH)) current.addNeighbor(nodes[i + (j - 1) * m.width]);
                if (m.canMove(current.pos, Dir.SOUTH)) current.addNeighbor(nodes[i + (j + 1) * m.width]);
                if (m.canMove(current.pos, Dir.EAST)) current.addNeighbor(nodes[i + 1 + j * m.width]);
                if (m.canMove(current.pos, Dir.WEST)) current.addNeighbor(nodes[i - 1 + j * m.width]);
            }
        }
    }

    public List<Dir> a_star(Node start, Node end) {
        PriorityQueue<Node> openList = new PriorityQueue(10, Comparator.comparingDouble((Node n) -> n.f));
        Set<Node> closeList = new HashSet<>(10);

        openList.add(start);

        while(!openList.isEmpty()) {
            Node node = openList.poll();

            if(node == end) {
                break;
            }

            Set<Node> nodes = node.neighbors.keySet();

            for(Node n: nodes) {
                openList.remove(n);

                if(!closeList.contains(n)) {
                    n.parent = node;
                    n.calcF(end);
                    openList.add(n);

                }
            }
            closeList.add(node);
        }

        Node node = end;
        List<Dir> dirs = new LinkedList<>();
        while(node.parent != null) {
            double dx = node.parent.pos.x - node.pos.x;
            double dy = node.parent.pos.y - node.pos.y;

            dirs.add(Dir.vec2fToDir(new Vector2f(dx, dy)));
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
            neighbors.put(n, new Path(this, n, pos.dist(n.pos)));
        }
    }

}
