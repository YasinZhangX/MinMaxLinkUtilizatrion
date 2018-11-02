package alg.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import alg.model.abstracts.BaseDemand;
import alg.model.abstracts.BaseVertex;

public class Demand implements BaseDemand {
    
    /**
     *  index for demands in the graph
     */
    protected Map<DemandPair<Integer, Integer>, Double> _vertex_pair_demand_index = 
        new HashMap<DemandPair<Integer,Integer>, Double>();
    
    /**
     *  index for demands in the graph
     */
    protected Map<Integer, DemandPair<Integer, Integer>> _id_demand_index = 
        new HashMap<Integer, DemandPair<Integer, Integer>>();
    
    /**
     *  list of demands in the graph 
     */
    protected List<DemandPair<Integer, Integer>> _demand_list = new Vector<DemandPair<Integer, Integer>>();
    
    /**
     *  the number of demands in the graph
     */
    protected int _demand_num = 0;
    
    /**
     * Constructor 1 
     * @param data_file_name
     */
    public Demand(final String data_file_name)
    {
        import_from_file(data_file_name);
    }

    /**
     * Constructor 2
     * 
     * @param demand
     */
    public Demand(final Demand demand_)
    {
        _demand_num = demand_._demand_num;
        _id_demand_index = demand_._id_demand_index;
        _vertex_pair_demand_index = demand_._vertex_pair_demand_index;
    }
    
    /**
     * Default constructor 
     */
    public Demand(){};
    
    /**
     * Clear members of the graph.
     */
    public void clear()
    {
        DemandPair.reset();
        _demand_num = 0;
        _demand_list.clear();
        _id_demand_index.clear();
        _vertex_pair_demand_index.clear();
    }
    
    /**
     * Get demands from file
     *  
     * @param data_file_name
     */
    public void import_from_file(String data_file_name) {
        // 0. Clear the variables 
        clear();
        
        try
        {
            // 1. read the file and put the content in the buffer
            FileReader input = new FileReader(data_file_name);
            BufferedReader bufRead = new BufferedReader(input);

            String line;    // String that holds current file line
            
            // 2. Read first line
            line = bufRead.readLine();
            while(line != null)
            {
                // 2.1 skip the empty line
                if(line.trim().equals("")) 
                {
                    line = bufRead.readLine();
                    continue;
                }
                
                String[] str_list = line.trim().split("\\s");
                
                // 2.2 add demand pair
                int start_vertex_id = Integer.parseInt(str_list[0]);
                int end_vertex_id = Integer.parseInt(str_list[1]);
                double demand = Double.parseDouble(str_list[2]);
                DemandPair<Integer, Integer> demandPair = new DemandPair<Integer, Integer>(start_vertex_id, end_vertex_id);
                _demand_list.add(demandPair);
                _id_demand_index.put(demandPair.get_id(), demandPair);
                add_demand(start_vertex_id, end_vertex_id, demandPair, demand);

                line = bufRead.readLine();
            }
            bufRead.close();

        } catch (IOException e) {
            // If another exception is generated, print a stack trace
            e.printStackTrace();
        }
    }

    
    protected void add_demand(int start_vertex_id, int end_vertex_id, DemandPair<Integer,Integer> demandPair, double demand) {
        // actually, we should make sure all vertices ids must be correct. 
        if(start_vertex_id == end_vertex_id)
        {
            throw new IllegalArgumentException("The demand from itself to itself does not allowed.");
        }    
        
        _vertex_pair_demand_index.put(demandPair, demand);
        
        ++_demand_num;
    }
    
    /**
     * Get demand list
     */
    public List<DemandPair<Integer, Integer>> get_demand_list() {
        return _demand_list;
    }
    
    /**
     * Get demand value by s-t vertex
     * @param source BaseVertex
     * @param sink BaseVertex
     * @return demandValue
     */
    public double get_demand(BaseVertex source, BaseVertex sink) {
        return _vertex_pair_demand_index.containsKey(
                new DemandPair<Integer, Integer>(source.get_id(), sink.get_id()))? 
                        _vertex_pair_demand_index.get(
                                new DemandPair<Integer, Integer>(source.get_id(), sink.get_id())) 
                      : 0;
    }
    
    /**
     * Get demand value by s-t id
     * @param source Integer
     * @param sink Integer
     * @return demandValue
     */
    public double get_demand(Integer source, Integer sink) {
        return _vertex_pair_demand_index.containsKey(
                new DemandPair<Integer, Integer>(source, sink))? 
                        _vertex_pair_demand_index.get(
                                new DemandPair<Integer, Integer>(source, sink)) 
                      : 0;
    }
    
    /**
     * Get the number of the damands
     * @return the number of demand
     */
    public int get_demand_num() {
        return _demand_num;
    }

}
