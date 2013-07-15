package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltProcedure.VoltAbortException;

public class GetQuotes extends VoltProcedure {

	public final SQLStmt getQuotes = new SQLStmt(
		    
			"select top 1 symbol, ask_price , bid_price , 1 as indexi , 1  as indexj from quotes where symbol = ? order by time desc;"
	);
	
	public VoltTable[] run(String i, String j,String symbol) throws VoltAbortException {
        // Add a SQL statement to the current execution queue
        voltQueueSQL(getQuotes,i,j,symbol);

        // Run all queued queries.
        // Passing true parameter since this is the last voltExecuteSQL for this procedure.
        return voltExecuteSQL(true);
    }
}
