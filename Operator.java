package ed.inf.adbs.lightdb;


public abstract class Operator {

    public abstract Tuple getNextTuple();

    public abstract void reset();

    public void dump() {
        reset(); 
        // System.out.println("starting");
        Tuple tuple = getNextTuple();
        while (tuple != null) {
            System.out.println(tuple);
            tuple = getNextTuple();
        }
    }
    
}