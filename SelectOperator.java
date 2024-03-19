package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;;

public class SelectOperator extends Operator {

    private Operator child;
    private Expression whereCondition;

    public SelectOperator(Operator childOperator, Expression whereCondition) {
        this.child = childOperator;
        this.whereCondition = whereCondition;

    }

    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        while ((tuple = child.getNextTuple()) != null) {
            TupleExpressionEvaluator evaluator = new TupleExpressionEvaluator(tuple);
            whereCondition.accept(evaluator);
            if (evaluator.getResult()) {
                return tuple;
            }
        }

        return null;
    }

    @Override
    public void reset() {
       child.reset();
    }
}
