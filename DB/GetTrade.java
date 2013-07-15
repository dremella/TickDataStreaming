package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltProcedure.VoltAbortException;

public class GetTrade extends VoltProcedure {

	public final SQLStmt getTrade = new SQLStmt(
		    
			"select symbol,time, trade_price, trade_volume,  1 as index from trades where symbol = ? order by symbol, time DESC, trade_price, trade_volume limit 1;"
	);
	
	public VoltTable[] run(String i,String symbol) throws VoltAbortException {
        // Add a SQL statement to the current execution queue
        voltQueueSQL(getTrade,i,symbol);

        // Run all queued queries.
        // Passing true parameter since this is the last voltExecuteSQL for this procedure.
        return voltExecuteSQL(true);
    }
}
