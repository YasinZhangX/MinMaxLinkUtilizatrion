package solution;

import alg.model.Demand;
import alg.model.Graph;
import alg.model.VariableGraph;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;

public class MMLUSolution {
    // The graph should be initiated only once to guarantee the correspondence 
    // between vertex id and node id in input text file. 
    static Graph graph = new VariableGraph("data/test_6_3");
    static Demand demand = new Demand("data/test_6_3_d");
    
    
    public static void main(String[] args) {
        try {
            // Build model
            IloCplex cplex = new IloCplex();
            
        } catch (IloException e) {
            System.out.println("Concert Error: " + e);
        }
    }
}
