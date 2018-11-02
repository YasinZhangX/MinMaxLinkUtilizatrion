package solution;

import java.util.List;

import alg.control.YenTopKShortestPathsAlg;
import alg.model.Demand;
import alg.model.DemandPair;
import alg.model.Graph;
import alg.model.Path;
import alg.model.VariableGraph;
import ilog.concert.IloColumn;
import ilog.concert.IloException;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class MMLUSolution {
    // The graph should be initiated only once to guarantee the correspondence 
    // between vertex id and node id in input text file. 
    static Graph graph = new VariableGraph("data/test_6_3");
    static Demand demand = new Demand("data/test_6_3_d");
    
    
    public static void main(String[] args) {
        try {
            YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
            
            Integer numOfDemand = demand.get_demand_num();
            List<DemandPair<Integer, Integer>> demandPairs = demand.get_demand_list();
            
            // Build model
            IloCplex  cplex = new IloCplex();
            IloNumVar[][] Xdp = new IloNumVar[numOfDemand][];
            IloNumVarType varType = IloNumVarType.Int;
            
            for (DemandPair<Integer, Integer> demandPair : demandPairs) {
                List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
                        graph.get_vertex(demandPair.o1), graph.get_vertex(demandPair.o2), 5);
                System.out.println("Paths:"+shortest_paths_list);
                System.out.println(shortest_paths_list.size());
                demandPair.set_path_list(shortest_paths_list);    // Add the path to the demand pair
                Xdp[demandPair.get_id()] = new IloNumVar[shortest_paths_list.size()];  // Add Xdp parameter
            }
            
            buildModelByColumn(cplex, graph, demandPairs, Xdp, varType);
            
        } catch (IloException e) {
            System.out.println("Concert Error: " + e);
        }
    }


    static void buildModelByColumn(IloMPModeler model, Graph graph_, List<DemandPair<Integer, Integer>> demandPairs,
            IloNumVar[][] xdp, IloNumVarType varType) throws IloException {

        int nEdge = graph_.get_edge_num();

        IloObjective utilization = model.addMinimize();
        IloRange[]   constraint  = new IloRange[nEdge];
      
        for (int i = 0; i < nEdge; i++) {
           constraint[i] = model.addRange(Double.MIN_VALUE, 0);
        }
     
        for (int j = 0; j < nFoods; j++) {
           IloColumn col = model.column(cost, data.foodCost[j]);
           for (int i = 0; i < nNutrs; i++) {
              col = col.and(model.column(constraint[i], data.nutrPerFood[i][j]));
           }
           Buy[j] = model.numVar(col, data.foodMin[j], data.foodMax[j], type);
        }
    }
}
