package ed.inf.adbs.lightdb;

import java.io.FileReader;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

public class Interpreter {

    private Statement statement;
    private String outputFile;
    

    public Interpreter(String databaseDir, String input, String outputFile, boolean fromFile) {
        if (fromFile) {
            try {
                statement = CCJSqlParserUtil.parse(new FileReader(input));
            } catch (Exception e) {
                System.err.println("Exception occurred during parsing");
                e.printStackTrace();
            }
            this.outputFile = outputFile;
            DataCatalog.initialize(databaseDir);
        } else {
            try {
                statement = CCJSqlParserUtil.parse(input);
            } catch (Exception e) {
                System.err.println("Exception occurred during parsing");
                e.printStackTrace();
            }
            this.outputFile = outputFile;
            DataCatalog.initialize(databaseDir);
        }
        
    }


    public void execute() {
        QueryPlanBuilder builder = new QueryPlanBuilder();
        try {
            Operator root = builder.buildQueryPlan(statement);
            root.dump();
        } catch (Exception e) {
            throw new IllegalArgumentException("Query type not supported");
        }
    }






    
}
