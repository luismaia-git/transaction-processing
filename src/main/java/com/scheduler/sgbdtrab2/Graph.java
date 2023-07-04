package com.scheduler.sgbdtrab2;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Graph {
    List<Edge> edges;
    ArrayList<Integer> sources;

    public Graph(List<Edge> edges) {
        this.edges = edges;
        for (Edge edge : this.edges) {
            if (!sources.contains(edge.s)) {
                sources.add(edge.s);
            }
        }
    }

    public Graph() {
        this.edges = new ArrayList<>();
        this.sources = new ArrayList<>();
    }

    public void insertEdge(int s, int d) {
        if (!sources.contains(s)) {
            this.sources.add(s);
        }
        boolean canInsert = true;
        for (Edge edge : this.edges) {
            if (edge.s == s && edge.d == d) {
                canInsert = false;
            }
        }
        if (canInsert) {
            Edge newEdge = new Edge(s, d);
            this.edges.add(newEdge);
        }

    }

    public void removeEdge(int s, int d) {
        Iterator<Edge> iterator = this.edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.s == s && edge.d == d) {
                iterator.remove();
                break;
            }
        }
    }

    public void removeEdgeAborted(int trId) {
        Iterator<Edge> iterator = this.edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.d == trId) {
                iterator.remove();
            }
        }

        Iterator<Integer> sourceIterator = this.sources.iterator();
        while (sourceIterator.hasNext()) {
            int s = sourceIterator.next();
            boolean sourceExists = false;
            for (Edge edge2 : this.edges) {
                if (edge2.s == s) {
                    sourceExists = true;
                    break;
                }
            }
            if (!sourceExists) {
                sourceIterator.remove();
            }
        }

    }

    // Mostra o Grafo de espera
    public void showGraph() {
        if (this.edges.size() == 0) {
            System.out.println("Grafo de Espera vazio.");
        } else {
            System.out.println("Grafo de Espera:");
            // prints the neighboring vertices including the current vertex
            for (int s : this.sources) {
                for (Edge edge : this.edges) {
                    if (edge.s == s) {
                        // prints the edge between the two vertices
                        System.out.print("(" + edge.s + " -- > " + edge.d + ")   ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /*public static void main(String args[]) {
        // creating a List of edges
        
         * List<Edge> edges = Arrays.asList(new Edge(0, 1), new Edge(1, 2), new Edge(2,
         * 4),
         * new Edge(4, 1), new Edge(3, 2),
         * new Edge(2, 5), new Edge(3, 4), new Edge(5, 4), new Edge(1, 1));
         */
        // List<Edge> edgesMut = new ArrayList<>(edges);
        // construct a graph from the given list of edges
        //Graph graph = new Graph(/* edgesMut */);
        /*
         * graph.showGraph();
         * 
         * graph.insertEdge(8, 9);
         * graph.showGraph();
         * 
         * graph.insertEdge(4, 2);
         * graph.showGraph();
         * 
         * graph.insertEdge(1, 2);
         * graph.showGraph();
         * 
         * graph.removeEdge(8, 9);
         * graph.showGraph();
         

        for (int i = 0; i < 5; i++) {
            graph.insertEdge(i, 1);
        }

        for (int j = 10; j < 15; j++) {
            graph.insertEdge(j, 2);
        }

        graph.showGraph();
        graph.removeEdgeAborted(1);
        graph.showGraph();
    }
    */
}
