package test;

import org.junit.jupiter.api.Test;

import alg.model.Demand;

class DemandTest {
    
    Demand demand = new Demand("data/test_6_3_d");

    @Test
    void testImport_from_file() {
        System.out.println("Length " + demand.get_demand_list().size() + ": " + demand.get_demand_list());
        for(int i = 0; i <= 5; i++) {
            for(int j = 0; j <= 5; j++) {
                if (i == j) 
                    continue;
                System.out.println("source:" + i + " sink:" + j + " Demand: " + demand.get_demand(i, j));
            }
            System.out.println();
        }
    }

}
