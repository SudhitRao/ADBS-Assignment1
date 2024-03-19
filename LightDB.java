package ed.inf.adbs.lightdb;



import java.io.FileReader;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;


/**
 * Lightweight in-memory database system
 *
 */
public class LightDB {

	public static void main(String[] args) {

		if (args.length != 3) {
			System.err.println("Usage: LightDB database_dir input_file output_file");
			return;
		}

		String databaseDir = args[0];
		String inputFile = args[1];
		String outputFile = args[2];

		// Just for demonstration, replace this function call with your logic
		//DataCatalog.initialize(databaseDir);
		//parsingExample(inputFile);
		//catalogExample(databaseDir);
		//scanExample(databaseDir);
		//selectExample(databaseDir);
		//projectExample(databaseDir);
		queryExample(databaseDir, inputFile, outputFile);
	}

	public static void queryExample(String databaseDir, String inputFile, String outputFile) {
		//String eval = "SELECT * FROM Sailors";
		String eval = "SELECT Sailors.C, Sailors.A FROM Sailors WHERE Sailors.C >= 200 AND Sailors.C <= 500";
		//String eval = "SELECT * FROM Sailors WHERE Sailors.C >= 200 AND Sailors.C <= 500";
		Interpreter interpreter = new Interpreter(databaseDir, eval, outputFile, false);
		interpreter.execute();
	}

	public static void projectExample(String filename) {
		try {
			String eval = "SELECT Sailors.C, Sailors.A FROM Sailors WHERE Sailors.C >= 200 AND Sailors.C <= 500";
			//String eval = "SELECT * FROM Sailors";
			Statement statement = CCJSqlParserUtil.parse(eval);
			if (statement != null) {
				PlainSelect select = (PlainSelect) statement;
				List<SelectItem<?>> arr = select.getSelectItems();
				System.out.println(arr);
				for (SelectItem<?> item : arr) {
					Expression exp = item.getExpression();
					Column col = (Column) exp;
					System.out.println(col.getColumnName());
					System.out.println(col.getTable().getName());
				}
				
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}

	public static void selectExample(String filename) {
		try {
			String eval = "SELECT * FROM Sailors WHERE Sailors.C >= 200 AND Sailors.C <= 500";
			//String eval = "SELECT * FROM Sailors";
			Statement statement = CCJSqlParserUtil.parse(eval);
			if (statement != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body is " + select.getSelectBody());
				PlainSelect select2 = (PlainSelect) statement;
				System.out.println(select2.getWhere());
				Expression exp = select2.getWhere();
				//TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
				String tableName = select2.getFromItem().toString();
				ScanOperator scan = new ScanOperator(tableName);
				Operator root = new SelectOperator(scan, exp);
				root.dump();
				
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}

	public static void catalogExample(String filename) {
		//DataCatalog.initialize(filename);

		DataCatalog catalog = DataCatalog.getInstance();

		System.out.println(catalog.getSchema("Boats"));
		System.out.println(catalog.getPath("Reserves"));
		System.out.println(catalog.getColumnIndex("Sailors", "C"));
	}

	public static void scanExample(String filename) {
		
		//DataCatalog.initialize(filename);
		
		ScanOperator op = new ScanOperator("Boats");
		Tuple tup;
		while ((tup = op.getNextTuple()) != null) {
			System.out.println(tup);
		}

		op.reset();
		while ((tup = op.getNextTuple()) != null) {
			System.out.println(tup);
		}

	}

	/**
	 * Example method for getting started with JSQLParser. Reads SQL statement from
	 * a file and prints it to screen; then extracts SelectBody from the query and
	 * prints it to screen.
	 */

	public static void parsingExample(String filename) {
		try {
			Statement statement = CCJSqlParserUtil.parse(new FileReader(filename));
            //Statement statement = CCJSqlParserUtil.parse("SELECT * FROM Boats");
			//Statement statement = CCJSqlParserUtil.parse("SELECT * FROM Boats WHERE Boats.id = 4");
			if (statement != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body is " + select.getSelectBody());
				PlainSelect select2 = (PlainSelect) statement;
				System.out.println(select2.getWhere());
				Expression exp = select2.getWhere();
				
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
}

