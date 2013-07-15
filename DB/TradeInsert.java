package com.procedures;

import org.voltdb.ProcInfo;
import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
/*
 * TradeInsert main class
 * @author irmg
 *
 */

public class TradeInsert extends VoltProcedure{
	 /*
	  * Insert query
	  */
    public final SQLStmt tradeInsert = new SQLStmt(
            "INSERT INTO trades VALUES (?, ? ,?, ?, ?, ?, ?,? ,?, ?, ?, ?, ?);"
    );
    /*
     * execute run 
     * 
     * changed by softsmith added created on 
     */
    public VoltTable[] run(   long strtime,
    						  String strExchange,
    						  String strSymbol,
    						  String strSuffix,
    						  String strSaleCondition,
    						  double valTradeVolume,
    						  double valTradePrice,
    						  String strTradeStopStockIndicator,
    						  String strTradeCorrectionIndicator,
    						  int valTradeSequenceNumber,
    						  String strSourceofTrade,
    						  String strTradeReportingFacility,
    						  long created_on
    					  )
    throws VoltAbortException {
            voltQueueSQL( tradeInsert,strtime,
            					strExchange, 
            					strSymbol, 
            					strSuffix,
            					strSaleCondition,
            					valTradeVolume,
            					valTradePrice,
            					strTradeStopStockIndicator,
					   			strTradeCorrectionIndicator,
					   			valTradeSequenceNumber,
					   			strSourceofTrade,
					   			strTradeReportingFacility,
					   			created_on
					   	);
            return voltExecuteSQL();
    }
}
