package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltProcedure.VoltAbortException;

public class GetConstTicker extends VoltProcedure {

	public final SQLStmt getConstTicker = new SQLStmt(
		    "select category, const_ticker from basket where etf_ticker = ? group by category, const_ticker order by category, const_ticker asc;"
	);
	
	public VoltTable[] run(String etf_ticker) throws VoltAbortException {
        // Add a SQL statement to the current execution queue
        voltQueueSQL(getConstTicker,etf_ticker);

        // Run all queued queries.
        // Passing true parameter since this is the last voltExecuteSQL for this procedure.
        return voltExecuteSQL(true);
    }
}
