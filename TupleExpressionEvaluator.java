package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class TupleExpressionEvaluator extends ExpressionDeParser {
    private Tuple tuple;
    private boolean result;
    private DataCatalog catalog;


    public TupleExpressionEvaluator(Tuple tuple) {
        this.tuple = tuple;
        this.catalog = DataCatalog.getInstance();
    }

    private int parseIntValue(Expression expr) {
        if (expr instanceof LongValue) {
            return (int)((LongValue) expr).getValue();
        } else if (expr instanceof Column) {
            String colName = ((Column) expr).getColumnName();
            String tabName = ((Column) expr).getTable().getName();
            return tuple.get(catalog.getColumnIndex(tabName, colName));
        }
        throw new IllegalArgumentException("Unsupported expression for integer value: " + expr);
    }

    private void processComparison(Expression left, Expression right, String operator) {
        int leftValue = parseIntValue(left);
        int rightValue = parseIntValue(right);

        switch (operator) {
            case "=":
                this.result = leftValue == rightValue;
                break;
            case "!=":
                this.result = leftValue != rightValue;
                break;
            case ">":
                this.result = leftValue > rightValue;
                break;
            case ">=":
                this.result = leftValue >= rightValue;
                break;
            case "<":
                this.result = leftValue < rightValue;
                break;
            case "<=":
                this.result = leftValue <= rightValue;
                break;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        processComparison(equalsTo.getLeftExpression(), equalsTo.getRightExpression(), "=");
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        processComparison(notEqualsTo.getLeftExpression(), notEqualsTo.getRightExpression(), "!=");
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        processComparison(greaterThan.getLeftExpression(), greaterThan.getRightExpression(), ">");
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        processComparison(greaterThanEquals.getLeftExpression(), greaterThanEquals.getRightExpression(), ">=");
    }

    @Override
    public void visit(MinorThan minorThan) {
        processComparison(minorThan.getLeftExpression(), minorThan.getRightExpression(), "<");
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        processComparison(minorThanEquals.getLeftExpression(), minorThanEquals.getRightExpression(), "<=");
    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        boolean leftResult = this.result;

        andExpression.getRightExpression().accept(this);
        this.result = leftResult && this.result; // Combine with AND logic
    }

    // Implement other visit methods as necessary, e.g., for Multiplication

    public boolean getResult() {
        return result;
    }
}