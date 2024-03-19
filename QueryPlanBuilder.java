package ed.inf.adbs.lightdb;


import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.schema.Column;


import java.util.List;

import net.sf.jsqlparser.expression.Expression;

public class QueryPlanBuilder {

    public Operator buildQueryPlan(Statement statement) throws Exception {
        //Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            return handleSelectStatement((Select) statement);
        }
        throw new IllegalArgumentException("Query type must be Select");
    }

    private Operator handleSelectStatement(Select selectStatement) {
        if (selectStatement instanceof PlainSelect) {
            return processPlainSelect((PlainSelect) selectStatement);
        }

        throw new IllegalArgumentException("Query type must be PlainSelect");
    }

    private Operator processPlainSelect(PlainSelect plainSelect) {
        
        
        String tableName = plainSelect.getFromItem().toString();
        Operator currentOperator = new ScanOperator(tableName);

        Expression exp = plainSelect.getWhere();
        if (exp != null) {
            currentOperator = new SelectOperator(currentOperator, exp);
        }

        List<SelectItem<?>> arr = plainSelect.getSelectItems(); //guaranteed to be length at least 1 I think
        if (! (arr.size() == 0 || arr.get(0).getExpression().toString().equals("*"))) {
            currentOperator = buildProject(currentOperator, arr);
        }
       return currentOperator;
    }

    private Operator buildProject(Operator childOperator, List<SelectItem<?>> cols) {

        int[] indexes = new int[cols.size()];
        int i = 0;
        for (SelectItem<?> item : cols) {
            Expression exp = item.getExpression();
            Column col = (Column) exp;
            String tableName = col.getTable().getName();
            String colName = col.getColumnName();
            indexes[i] = DataCatalog.getInstance().getColumnIndex(tableName, colName);
            i++;
        }   
        return new ProjectOperator(childOperator, indexes);
    }
}
