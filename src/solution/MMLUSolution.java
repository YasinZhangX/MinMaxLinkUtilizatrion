package solution;

import java.util.List;

import alg.control.YenTopKShortestPathsAlg;
import alg.model.Demand;
import alg.model.DemandPair;
import alg.model.Graph;
import alg.model.Path;
import alg.model.VariableGraph;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class MMLUSolution {
    // The graph should be initiated only once to guarantee the correspondence 
    // between vertex id and node id in input text file. 
    static Graph graph = new VariableGraph("data/test_6_3");
    static Demand demand = new Demand("data/test_6_3_d");
    
    
    public static void main(String[] args) {
        try {
            YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
            
            Integer numOfEdge = graph.get_edge_num();
            Integer numOfDemand = demand.get_demand_num();
            List<DemandPair<Integer, Integer>> demandPairs = demand.get_demand_list();
            
            for (DemandPair<Integer, Integer> demandPair : demandPairs) {
                List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
                        graph.get_vertex(demandPair.o1), graph.get_vertex(demandPair.o2), 5);
                System.out.println("Paths:"+shortest_paths_list);
                System.out.println(yenAlg.get_result_list().size());
            }
            
            // Build model
            IloCplex  cplex = new IloCplex();
            IloNumVar[][] Xdp = new IloNumVar[numOfDemand][1];
            
        } catch (IloException e) {
            System.out.println("Concert Error: " + e);
        }
    }
}
