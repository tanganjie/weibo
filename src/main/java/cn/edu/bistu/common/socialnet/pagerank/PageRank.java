package cn.edu.bistu.common.socialnet.pagerank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanjie on 11/11/15.
 */
public class PageRank {
    public static void main(String[] args) {
        Graph graph = new Graph(5);
        List<String> labels = new ArrayList<>();
        Map<String, Integer> murls = new HashMap<>();

        labels.add("A");
        labels.add("B");
        labels.add("C");
        labels.add("D");
        labels.add("E");

        for(int i=0; i<labels.size(); i++) {
            murls.put(labels.get(i), i);
        }

        graph.addEdge(murls.get("A"), murls.get("B"));
        graph.addEdge(murls.get("A"), murls.get("D"));
        graph.addEdge(murls.get("A"), murls.get("E"));
        graph.addEdge(murls.get("B"), murls.get("A"));
        graph.addEdge(murls.get("B"), murls.get("C"));
        graph.addEdge(murls.get("B"), murls.get("D"));
        graph.addEdge(murls.get("B"), murls.get("E"));
        graph.addEdge(murls.get("C"), murls.get("B"));
        graph.addEdge(murls.get("C"), murls.get("D"));
        graph.addEdge(murls.get("C"), murls.get("E"));

        Vector vector = graph.computePagerank(0.85, 0.0001);

        System.out.println(vector);
    }
}
