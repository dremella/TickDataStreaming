package com.procedures;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltProcedure.VoltAbortException;

public class GetIOPV extends VoltProcedure {
	 public final SQLStmt GetConst_ticker = new SQLStmt("SELECT CONST_TICKER,C_WEIGHT FROM BASKET WHERE ETF_TICKER=?;");
	 public final SQLStmt GetTradePrice = new SQLStmt("SELECT TOP 1 TRADE_PRICE FROM TRADES  WHERE SYMBOL = ? and created_on>? and created_on<=? ORDER BY created_on DESC;");
	 public static long valIOPV = 0;
	 public long run(String strEtfticker,long timeFrom,long timeTo) throws VoltAbortException {
		 	valIOPV = 0;
		 	voltQueueSQL(GetConst_ticker, strEtfticker);
		 	VoltTable[] results = voltExecuteSQL();
		 	for (VoltTable node : results){
           	 for (int r=0;r<node.getRowCount();r++) {
           		 VoltTableRow row = node.fetchRow(r);
           		 voltQueueSQL(GetTradePrice, row.getString("CONST_TICKER"),timeFrom,timeTo);
           		 VoltTable[] results1 = voltExecuteSQL();
           		 for (VoltTable tradenode : results1){ 
                    	 for (int rtrade=0;rtrade<tradenode.getRowCount();rtrade++) {
                    		 VoltTableRow traderow = tradenode.fetchRow(rtrade);
                    		 if(traderow.toString().length() > 0) {
                    			 valIOPV += traderow.getDouble("TRADE_PRICE") * row.getDouble("C_WEIGHT");
                    		 }
                    	 } 
                    }
           	 }
           }
		 return valIOPV;
	 }
}
