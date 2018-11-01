package alg.model.abstracts;

import java.util.List;

import alg.model.DemandPair;

public interface BaseDemand {
    List<DemandPair<Integer, Integer>> get_demand_list();
    
    double get_demand(BaseVertex source, BaseVertex sink);
}
