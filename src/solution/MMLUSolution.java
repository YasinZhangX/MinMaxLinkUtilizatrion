package solution;

import java.util.Arrays;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import alg.control.YenTopKShortestPathsAlg;
import alg.model.Demand;
import alg.model.DemandPair;
import alg.model.Graph;
import alg.model.Pair;
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
            IloNumVar[] rVar = new IloNumVar[1];
            IloNumVarType varType = IloNumVarType.Int;
            
            for (DemandPair<Integer, Integer> demandPair : demandPairs) {
                List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
                        graph.get_vertex(demandPair.o1), graph.get_vertex(demandPair.o2), 5);
                System.out.println("Paths:"+shortest_paths_list);
                System.out.println(shortest_paths_list.size());
                demandPair.set_path_list(shortest_paths_list);    // Add the path to the demand pair
                Xdp[demandPair.get_id()] = new IloNumVar[shortest_paths_list.size()];  // Add Xdp parameter
            }
            
            buildModelByColumn(cplex, demandPairs, Xdp, rVar, varType);
            
        } catch (IloException e) {
            System.out.println("Concert Error: " + e);
        }
    }


    private static void buildModelByColumn(IloMPModeler model, List<DemandPair<Integer, Integer>> demandPairs,
            IloNumVar[][] xdp, IloNumVar[] rVar, IloNumVarType type) throws IloException {

        List<Pair<Integer, Integer>> edges = graph.get_edge_list();
        DemandPair<Integer, Integer> demandPair;
        int nPath;
        double edgeCapacity;
        int nEdge = graph.get_edge_num();
        int nDemand = demandPairs.size();
        int i = 0;
        
        // Add target
        IloObjective utilization = model.addMinimize();    
        IloRange[]   constraint  = new IloRange[nEdge];
        
        IloColumn utilizationColumn = model.column(utilization, 1.0);
        for (Pair<Integer,Integer> pair : edges) {
            constraint[i] = model.addRange(-Double.MAX_VALUE, 0);
            edgeCapacity = graph.get_edge_capacity(graph.get_vertex(pair.first()), graph.get_vertex(pair.second()));
            utilizationColumn.and(model.column(constraint[i], -1.0*edgeCapacity));
            i++;
        }
        rVar[0] = model.numVar(utilizationColumn, 0, Double.MAX_VALUE, IloNumVarType.Float);

        for(int d = 0; d < nDemand; d++) {
            demandPair = demandPairs.get(d);
            nPath = demandPair.get_path_list().size();
            for(int p = 0; p < nPath; p++) {
                double[] delta = GetDeltaForDemandPath(demandPair.get_path_list().get(p), edges);
            }
        }
     
//        for (int j = 0; j < nFoods; j++) {
//           IloColumn col = model.column(cost, data.foodCost[j]);
//           for (int i = 0; i < nNutrs; i++) {
//              col = col.and(model.column(constraint[i], data.nutrPerFood[i][j]));
//           }
//           Buy[j] = model.numVar(col, data.foodMin[j], data.foodMax[j], type);
//        }
    }


    private static double[] GetDeltaForDemandPath(Path path, List<Pair<Integer, Integer>> edges) {
        
        int index;
        List<Pair<Integer, Integer>> linkList = path.get_link_list();
        double[] delta = new double[edges.size()];
        Arrays.fill(delta, 0);
        
        for (Pair<Integer, Integer> link : linkList) {
            index = edges.indexOf(link);
            delta[index] = 1.0;
        }
        
        return delta;
    }
}
