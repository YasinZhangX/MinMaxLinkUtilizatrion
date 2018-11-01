package alg.model;

public class DemandPair<TYPE1, TYPE2> extends Pair<TYPE1, TYPE2> {
    
    private static int CURRENT_DEMAND_PAIR_NUM = 0;
    private int _id = CURRENT_DEMAND_PAIR_NUM++;

    public TYPE1 o1;
    public TYPE2 o2;
    
    public DemandPair(TYPE1 o1, TYPE2 o2) {
        super(o1, o2);
        this.o1 = o1; this.o2 = o2; 
    }
    
    public TYPE1 first()
    {
        return o1;
    }
    
    public TYPE2 second()
    {
        return o2;
    }
    
    public int get_id()
    {
        return _id;
    }
    
    public int hashCode() 
    {
        int code = 0;
        if(o1 != null)
            code = o1.hashCode();
        if(o2 != null)
            code = code/2 + o2.hashCode()/2;
        return code;
    }

    public static boolean same(Object o1, Object o2) 
    {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) 
    {
        if( ! (obj instanceof Pair))
            return false;
        Pair<TYPE1, TYPE2> p = (Pair<TYPE1, TYPE2>)obj;
        return same(p.o1, this.o1) && same(p.o2, this.o2);
    }

    public String toString() 
    {
        return "DemandPair{"+o1+", "+o2+"}";
    }
    
    public static void reset()
    {
        CURRENT_DEMAND_PAIR_NUM = 0;
    }
}
