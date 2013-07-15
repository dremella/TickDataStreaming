package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltProcedure.VoltAbortException;

public class GetETF extends VoltProcedure {

	public final SQLStmt getEtf = new SQLStmt(
		    "select etf_ticker from basket group by etf_ticker ORDER BY etf_ticker ASC;"
	);
	
	public VoltTable[] run() throws VoltAbortException {
        // Add a SQL statement to the current execution queue
        voltQueueSQL(getEtf);

        // Run all queued queries.
        // Passing true parameter since this is the last voltExecuteSQL for this procedure.
        return voltExecuteSQL(true);
    }
}
