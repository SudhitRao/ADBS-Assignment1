package ed.inf.adbs.lightdb;

public class JoinOperator extends Operator {

    private Operator leftChild;
    private Operator rightChild;

    private Tuple leftTuple;
    private Tuple rightTuple;

    @Override
    public Tuple getNextTuple() {
        leftTuple = leftChild.getNextTuple();
        while (leftTuple != null) {
            rightChild.reset();
            rightTuple = rightChild.getNextTuple();
            while (rightTuple != null) {
                
                // if condition is held, then return glued tuple (update datacatalog accordingly)

                
            }
            leftTuple = leftChild.getNextTuple();
        }
        return null;
    }

    @Override
    public void reset() {
        return;
    }
    
}
