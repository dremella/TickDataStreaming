create table TRADES (
	 Time TIMESTAMP,
	 Exchange VARCHAR(1),
	 Symbol VARCHAR(6) NOT NULL,
	 Suffix VARCHAR(10),
	 Sale_Condition VARCHAR(50),	
	 Trade_Volume BIGINT,
	 Trade_Price FLOAT,
	 Trade_Stop_Stock_Indicator VARCHAR(5),
	 Trade_Correction_Indicator VARCHAR(5),
	 Trade_Sequence_Number INTEGER,
	 Source_of_Trade VARCHAR(5),
	 Trade_Reporting_Facility VARCHAR(5),
	 Created_On BIGINT
);

CREATE INDEX Idx_Trades_Time ON TRADES (Symbol, Time);
CREATE PROCEDURE DeleteAllTrades AS
DELETE FROM TRADES;

create table QUOTES (
 	Time TIMESTAMP,
	Exchange VARCHAR(1),
	Symbol VARCHAR(6) NOT NULL,
	Suffix VARCHAR(10),
	Bid_Price FLOAT,
	Bid_Size INTEGER,
	Ask_Price FLOAT,
	Ask_Size INTEGER,
	Quote_Condition VARCHAR(1),
	Market_Maker SMALLINT,
	Bid_Exchange VARCHAR(1),
	Ask_Exchange VARCHAR(1),
	Sequence_Number	 INTEGER,
	National_BBO_Ind TINYINT,
	NASDAQ_BBO_Ind TINYINT,
	Quote_Cancel_Correction VARCHAR(1),
	Source_of_Quote VARCHAR(1),
	RPI VARCHAR(1),
	Short_Sale_Restriction_Ind VARCHAR(1),
	CQS VARCHAR(1),
	UTP VARCHAR(1),
	FINRA_ADF_MPID_Indicator VARCHAR(1),
	Created_On TIMESTAMP
 );
 
 CREATE INDEX Idx_Quotes_Time ON QUOTES (Symbol, Time);

CREATE PROCEDURE DeleteAllQuotes AS
DELETE FROM QUOTES;

create table premiumdiscount(
	etf_ticker varchar(50),	
	premium_discount float
);

create table basket (
	etf_ticker varchar(10),	
	const_ticker varchar(10),
	c_weight float,
	category varchar(20),
	focus varchar(30)
);

CREATE TABLE SymbolSectorMapping (
	Symbol VARCHAR(10) NOT NULL,
	Sector VARCHAR(30)
);

PARTITION TABLE TRADES ON COLUMN Symbol;
PARTITION TABLE QUOTES ON COLUMN Symbol;

CREATE PROCEDURE FROM CLASS com.procedures.TradeInsert;
PARTITION PROCEDURE TradeInsert ON TABLE TRADES COLUMN Symbol PARAMETER 2;
CREATE PROCEDURE FROM CLASS com.procedures.QuotesInsert;
PARTITION PROCEDURE QuotesInsert ON TABLE QUOTES COLUMN Symbol PARAMETER 2;

create table activity_log(
	client_name varchar(50),
	etf_name varchar(50),
	trade_value float,
	buy_sell varchar(10),
	current_pd float,
	current_adv float,
	created_on TIMESTAMP
);

CREATE PROCEDURE FROM CLASS com.procedures.BasketInsert;
CREATE PROCEDURE FROM CLASS com.procedures.GetConstTicker;
CREATE PROCEDURE FROM CLASS com.procedures.GetETF;
CREATE PROCEDURE FROM CLASS com.procedures.GetIOPV;
CREATE PROCEDURE FROM CLASS com.procedures.GetVolume;
CREATE PROCEDURE FROM CLASS com.procedures.InsertPremiumDiscount;



