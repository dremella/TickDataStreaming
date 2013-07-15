package com.procedures;

import org.voltdb.ProcInfo;
import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class QuotesInsert extends VoltProcedure{
	 /*
	  * Insert query
	  */
   public final SQLStmt quoteInsert = new SQLStmt(
           "INSERT INTO quotes VALUES (? ,?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ? ,? ,?, ?, ?, ?, ?, ? ,?, ?, ? , ?);"
   );
   /*
    * execute run 
    */
   public VoltTable[] run(long valTime,
   						  String strExchange,
   						  String strSymbol,
						  String suffix,
   						  double strBidPrice,
   						  int strBidSize,
   						  double valAskPrice,
   						  int valAskSize,
   						  String strQuoteCondition,
   						  short valMarketMaker,
   						  String strBidExchange,
   						  String strAskExchange,
   						  int valSequenceNumber,
   						  int valNationalBBOInd,
   						  int valNASDAQBBOInd,
   						  String valQuoteCancelCorrection,
   						  String strSourceofQuote,
   						  String strRPI,
   						  String strShortSale,
   						  String strCQS,
   						  String strUTP,
   						  String strFINRAADFMPIDIndicator
   						  )
   throws VoltAbortException {
           voltQueueSQL( quoteInsert,
        		   	   valTime,
					   strExchange,
					   strSymbol,
					   suffix,
					   strBidPrice,
					   strBidSize,
					   valAskPrice,
					   valAskSize,
					   strQuoteCondition,
					   valMarketMaker,
					   strBidExchange,
					   strAskExchange,
					   valSequenceNumber,
					   valNationalBBOInd,
					   valNASDAQBBOInd,
					   valQuoteCancelCorrection,
					   strSourceofQuote,
					   strRPI,
					   strShortSale,
					   strCQS,
					   strUTP,
					   strFINRAADFMPIDIndicator,
					   System.currentTimeMillis()*1000
					   	);
           return voltExecuteSQL();
   }
}

