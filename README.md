# BitBacktester
A backtesting library designed to be used with Bitcoin exchange data.

#Intro
BitBacktester is a library which simulates a backtest on market data. It will analyze a trading strategy against a set of historical data.
It tracks & logs trading positions & orders. Supports any OHLC data.

#How to Use
BitBacktester has been designed to be easily extensible, allowing the user to run many strategies with little code:
1. Override <code>TradingStrategy</code> class, specifically the <code>onBar(Tick t)</code> method.
   This is where all of your trading logic goes.
   #Accessing Historical Data
   The trading data can be accessed through the <code>DataFeed dataFeed</code> class field. To get the current price bar, simply call
   <code>getTick()</code>. This will return a <code>Tick</code> object containing TOHLCV (Time, open, high, low, close) data.
   To access previous bars, call the <code>getHistory()</code> method. This will return all of the history up to the current point
   in the backtest. 
   #Placing Orders
   Orders are placed through the <code>Broker</code> interface. Simply call <code>limit{TYPE}Order()</code> with the appropriate arguments
   to place an order with the broker. The broker will throw a number of exceptions in the case of an error. These can be caught by the 
   trading logic. Otherwise, they will be handled according to the superclass <code>TradingStrategy</code>.
   #Accessing the Portfolio
   The <code>Broker</code> object contains another member <code>Portfolio</code>. Through this object, you can view your current positions,
   check current portfolio value, returns, etc...
   NOTE: You should _NOT_ <code>add()</code> to any of the positions via the <code>getPositions()</code> method. All orders are handled &
   sanitized via the <code>Broker</code>.
   #Logging
   Any info you wish to log to output can be logged via the <code>TradingStrategy.log(...)</code> method. Simply pass a <code>LogType</code>,
   the current <code>Tick</code>, and your message. The message will be logged to the current working directory.
#TODO 
The rest of this README   
