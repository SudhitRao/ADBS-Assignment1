package ed.inf.adbs.lightdb;

import java.util.ArrayList;




public class ProjectOperator extends Operator {

    private Operator child;
    private int[] indexes;

    public ProjectOperator(Operator child, int[] indexes) {
        this.child = child;
        this.indexes = indexes;
    }

    @Override
    public Tuple getNextTuple() {
        Tuple tup = child.getNextTuple();
        if (tup != null) {
            ArrayList<Integer> extracted = new ArrayList<>();
            for (int index : indexes) {
                extracted.add(tup.get(index));
            }
            return new Tuple(extracted);
        } 
        return null;
        
    }

    @Override
    public void reset() {
        child.reset();
    }
    
}
