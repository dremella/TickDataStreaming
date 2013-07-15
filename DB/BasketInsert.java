package com.procedures;

import org.voltdb.ProcInfo;
import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltProcedure.VoltAbortException;

public class BasketInsert extends VoltProcedure {
	 
    public final SQLStmt bas_Insert = new SQLStmt(
            "INSERT INTO basket VALUES (? ,?, ?, ?, ?);"
    );
    public final SQLStmt sectorInsert = new SQLStmt("INSERT INTO SymbolSectorMapping VALUES (?,?);");

    public VoltTable[] run(   String stretf_ticker,
    						  String strconst_ticker	,
    						  double valcweight,
                              String strcategory,
                              String strfocus
                          )
    throws VoltAbortException {
            voltQueueSQL( bas_Insert, stretf_ticker ,strconst_ticker, valcweight, strcategory, strfocus);
            if (strcategory.equals("Sector")) {
            	voltQueueSQL(sectorInsert, strconst_ticker, strfocus);
            }
            return voltExecuteSQL(true);
    }
}
