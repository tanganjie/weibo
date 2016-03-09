package cn.edu.bistu.common.socialnet.pagerank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanjie on 11/11/15.
 */
public class UserRankTest {
    public static void main(String[] args) {
        UserGraph graph = new UserGraph(5);

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

        graph.addEdge(murls.get("A"), murls.get("B"), 2.5);
        graph.addEdge(murls.get("A"), murls.get("D"), 25.0);
        graph.addEdge(murls.get("A"), murls.get("E"), 22.5);
        graph.addEdge(murls.get("B"), murls.get("A"), 7.0);
        graph.addEdge(murls.get("B"), murls.get("C"), 1.5);
        graph.addEdge(murls.get("B"), murls.get("D"), 60.0);
        graph.addEdge(murls.get("B"), murls.get("E"), 40.0);
        graph.addEdge(murls.get("C"), murls.get("B"), 1.5);
        graph.addEdge(murls.get("C"), murls.get("D"), 5.0);
        graph.addEdge(murls.get("C"), murls.get("E"), 4.0);

        Vector vector = graph.computePagerank(0.85, 0.0001);

        System.out.println(vector);
    }
}
