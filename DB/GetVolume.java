package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class GetVolume extends VoltProcedure {

	public final SQLStmt getVolume = new SQLStmt(
		    "select symbol,TIME,trade_price,trade_volume from trades where symbol = ?;"
	);
	
	public VoltTable[] run() throws VoltAbortException {
        // Add a SQL statement to the current execution queue
        voltQueueSQL(getVolume);

        // Run all queued queries.
        // Passing true parameter since this is the last voltExecuteSQL for this procedure.
        return voltExecuteSQL(true);
    }
}
