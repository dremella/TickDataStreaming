package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltProcedure.VoltAbortException;

public class InsertPremiumDiscount  extends VoltProcedure { 
	 
    public final SQLStmt pdInsert = new SQLStmt(
            "INSERT INTO PremiumDiscount VALUES (? ,?);"
    );

    public VoltTable[] run(   String strName,
    						  double pd)
    throws VoltAbortException {
            voltQueueSQL( pdInsert, strName, pd);
            voltExecuteSQL();
            return null;
    }
}
