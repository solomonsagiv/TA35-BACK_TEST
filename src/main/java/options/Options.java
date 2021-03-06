package options;

import OPs.EqualMoveService;
import OPs.OpAvgMoveService;
import lists.MyChartList;
import locals.L;
import org.json.JSONObject;
import serverObjects.BASE_CLIENT_OBJECT;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Options {

    public static final int DAY = 1;
    public static final int MONTH = 2;
    public static final int QUARTER = 3;
    public static final int QUARTER_FAR = 4;
    public static final int FUTURE = 5;

    List< Strike > strikes;
    HashMap< Integer, Option > optionsMap;
    BASE_CLIENT_OBJECT client;
    double bidMin = 0;
    double askMax = 0;
    List< Double > opList = new ArrayList<>( );
    List< Double > opAvgList = new ArrayList<>( );
    List< Double > conList = new ArrayList<>( );
    List< Double > conBidList = new ArrayList<>( );
    List< Double > conAskList = new ArrayList<>( );
    private boolean requested = false;
    private int type;
    private String name = "";
    private LocalDate toDay = LocalDate.now( );
    private LocalDate expDate;
    private double daysToExp = -1;
    private double interest = 1;
    private double interestZero = 0;
    private double devidend = -1;
    private double borrow = 0;
    private int contractBidAskCounter = 0;
    private int baseID = 0;
    private int minId = 0;
    private int maxId = 0;
    private boolean gotData = false;
    private double contractBid = 0;
    private double contractAsk = 0;
    private double currStrike = 0;
    private double contract = 0;
    private double opAvg = 0;
    private MyChartList conBidAskCounterList = new MyChartList();

    public Options( BASE_CLIENT_OBJECT client, int type ) {
        this.type = type;
        this.client = client;

        initType( );

        strikes = new ArrayList<>( );
        optionsMap = new HashMap<>( );


    }

    public static int getQUARTER() {
        return QUARTER;
    }

    public static int getDAY() {
        return DAY;
    }

    public static int getMONTH() {
        return MONTH;
    }

    public Call getCall( double targetStrike ) {
        for ( Strike strike : strikes ) {
            if ( targetStrike == strike.getStrike( ) ) {
                return strike.getCall( );
            }
        }
        return null;
    }

    public Put getPut( double targetStrike ) {
        for ( Strike strike : strikes ) {
            if ( targetStrike == strike.getStrike( ) ) {
                return strike.getPut( );
            }
        }
        return null;
    }

    public void initOptions() {

        double startStrike = client.getStartStrike( );
        double endStrike = client.getEndStrike( );

        int id = getBaseID( );

        for ( double strike = startStrike; strike < endStrike; strike += client.getStrikeMargin( ) ) {

            // Call
            Call call = new Call( strike, id );
            setOption( call );
            id++;

            // Put
            Put put = new Put( strike, id );
            setOption( put );
            id++;

        }

    }

    private void initType() {
        switch ( type ) {
            case DAY:
                setBaseID( client.getBaseId( ) + 2000 );
                setName( "Day" );
                break;
            case MONTH:
                setBaseID( client.getBaseId( ) + 4000 );
                setName( "Month" );
                break;
            case QUARTER:
                setBaseID( client.getBaseId( ) + 6000 );
                setName( "Quarter" );
                break;
            case QUARTER_FAR:
                setBaseID( client.getBaseId( ) + 8000 );
                setName( "Quarter Far" );
                break;
            default:
                break;
        }
    }

    public Option getOption( String name ) {

        double targetStrike = Double.parseDouble( name.substring( 1 ) );

        for ( Strike strike : strikes ) {
            if ( strike.getStrike( ) == targetStrike ) {
                if ( name.toLowerCase( ).contains( "c" ) ) {
                    return strike.getCall( );
                } else {
                    return strike.getPut( );
                }
            }
        }
        return null;
    }

    public void checkOptionData() {
        new Thread( () -> {

            while ( !isGotData( ) ) {
                try {

                    // Sleep
                    Thread.sleep( 1000 );
                    boolean bool = true;

                    double increment = client.getStrikeMargin( );

                    // For each strike
                    double strikInMoney = getStrikeInMoney( );
                    double startStrike = strikInMoney - increment * 2;
                    double endStrike = strikInMoney + increment * 2;

                    for ( double strikePrice = startStrike; strikePrice < endStrike; strikePrice += client.getStrikeMargin( ) ) {

                        Strike strike = getStrike( strikePrice );

                        Option call = strike.getCall( );
                        Option put = strike.getPut( );

                        if ( call.getBid( ) == 0 || call.getAsk( ) == 0 || put.getBid( ) == 0 || put.getAsk( ) == 0 ) {
                            bool = false;
                            break;
                        }
                    }

                    // Exit the function
                    if ( bool ) {
                        setGotData( bool );
                        Thread.currentThread( ).interrupt( );
                    }

                } catch ( InterruptedException e ) {
                    break;
                } catch ( Exception e ) {

                }
            }
        } ).start( );
    }

    // Claculate the index from options
    public double getContract() {
        return contract;
    }

    // Claculate the index from options
    public double calcContractAbsolute() {

        try {
            ArrayList< Double > buys = new ArrayList<>( );
            ArrayList< Double > sells = new ArrayList<>( );

            double strikeInMoney = getStrikeInMoney( );
            double startStrike = strikeInMoney - ( client.getStrikeMargin( ) * 5 );
            double endStrike = strikeInMoney + ( client.getStrikeMargin( ) * 5 );

            for ( double strikePrice = startStrike; strikePrice <= endStrike; strikePrice += client.getStrikeMargin( ) ) {

                Strike strike;
                double call_ask = 0;
                double call_bid = 0;
                double put_ask = 0;
                double put_bid = 0;

                try {
                    strike = getStrike( strikePrice );

                    call_ask = strike.getCall( ).getAsk( );
                    call_bid = strike.getCall( ).getBid( );
                    put_ask = strike.getPut( ).getAsk( );
                    put_bid = strike.getPut( ).getBid( );

                    if ( call_ask <= 0 ) {
                        call_ask = 99999999;
                    }
                    if ( put_ask <= 0 ) {
                        put_ask = 99999999;
                    }

                    double v = ( strikePrice / ( Math.pow( getInterest( ), ( getAbsolutDays( ) / 360.0 ) ) ) );

                    double buy = ( call_ask - put_bid ) + v;
                    double sell = ( call_bid - put_ask ) + v;

                    buys.add( buy );
                    sells.add( sell );

                } catch ( Exception e ) {
                    // TODO: handle exception
                }

            }

            double currentBidMin = Collections.min( buys );
            double currentAskMax = Collections.max( sells );

            double future = floor( ( currentBidMin + currentAskMax ) / 2, 100 );

            return future;
        } catch ( Exception e ) {
            e.printStackTrace( );
            return 0;
        }
    }

    public double getAbsolutDays() {
        double d = ( int ) ChronoUnit.DAYS.between( LocalDate.now( ), getExpDate( ) );
        return d + 1;
    }

    public double getStrikeInMoney() {

        if ( currStrike != 0 ) {

            if ( client.getFuture( ) - currStrike > client.getStrikeMargin( ) ) {

                currStrike += client.getStrikeMargin( );

            } else if ( client.getFuture( ) - currStrike < -client.getStrikeMargin( ) ) {

                currStrike -= client.getStrikeMargin( );

            }
        } else {
            currStrike = getStrikeInMoneyIfZero( ).getStrike( );
        }
        return currStrike;
    }

    public double getOp() {
        return L.floor( getContract( ) - client.getIndex( ), 100 );
    }

    public Strike getStrikeInMoneyIfZero() {
        double margin = 1000000;
        Strike targetStrike = new Strike( );

        for ( Strike strike : getStrikes( ) ) {
            double newMargin = absolute( strike.getStrike( ) - client.getFuture( ) );

            if ( newMargin < margin ) {

                margin = newMargin;
                targetStrike = strike;

            } else {
                break;
            }
        }
        return targetStrike;
    }

    public Option getOption( String side, double targetStrike ) {
        for ( Strike strike : strikes ) {
            if ( strike.getStrike( ) == targetStrike ) {
                if ( side.toLowerCase( ).contains( "c" ) ) {
                    return strike.getCall( );
                } else {
                    return strike.getPut( );
                }
            }
        }
        return null;
    }

    public Option getOption( Class c, double targetStrike ) {
        for ( Strike strike : strikes ) {
            if ( strike.getStrike( ) == targetStrike ) {
                if ( c == Call.class ) {
                    return strike.getCall( );
                } else {
                    return strike.getPut( );
                }
            }
        }
        return null;
    }

    // Return single strike by strike price (double)
    public Strike getStrike( double strikePrice ) {
        for ( Strike strike : strikes ) {
            if ( strikePrice == strike.getStrike( ) ) {
                return strike;
            }
        }
        return null;
    }

    // Return list of strikes prices
    public ArrayList< Double > getStrikePricesList() {
        ArrayList< Double > list = new ArrayList<>( );
        strikes.forEach( strike -> list.add( strike.getStrike( ) ) );
        return list;
    }

    // Remove strike from strikes arr by strike price (double)
    public void removeStrike( double strike ) {
        int indexToRemove = 0;

        for ( int i = 0; i < strikes.size( ); i++ ) {
            if ( strikes.get( i ).getStrike( ) == strike ) {
                indexToRemove = i;
            }
        }
        strikes.remove( indexToRemove );
    }

    // Remove strike from strikes arr by strike class
    public void removeStrike( Strike strike ) {
        strikes.remove( strike );
    }

    // Add strike to strikes arr
    public void addStrike( Strike strike ) {

        boolean contains = getStrikePricesList( ).contains( strike.getStrike( ) );

        // Not inside
        if ( !contains ) {
            strikes.add( strike );
        }
    }

    public Option getOptionById( int id ) {
        return optionsMap.get( id );
    }

    // Set option in strikes arr
    public void setOption( Option option ) {

        // Set min || max ID
        setMinId( option.getId( ) );
        setMaxId( option.getId( ) );

        // HashMap
        optionsMap.put( option.getId( ), option );

        // Strikes list
        boolean callPut = option instanceof Call;

        Strike strike = getStrike( option.getStrike( ) );

        if ( strike != null ) {

            if ( callPut ) {
                if ( strike.getCall( ) == null ) {
                    strike.setCall( ( Call ) option );
                }
            } else {
                if ( strike.getPut( ) == null ) {
                    strike.setPut( ( Put ) option );
                }
            }
        } else {

            // Create new if doesn't exist
            strike = new Strike( );
            strike.setStrike( option.getStrike( ) );

            if ( callPut ) {
                strike.setCall( ( Call ) option );
            } else {
                strike.setPut( ( Put ) option );
            }

            // Add strike
            addStrike( strike );
        }
    }

    public JSONObject getProps() {

        JSONObject props = new JSONObject( );
        props.put( "interest", getInterest( ) );
        props.put( "borrow", getCalcBorrow( ) );
        props.put( "devidend", getDevidend( ) );
        props.put( "days", getDays( ) );
        return props;

    }

    public JSONObject getEmptyProps() {

        JSONObject json = new JSONObject( );
        json.put( "interest", 1 );
        json.put( "borrow", 0 );
        json.put( "devidend", 0 );
        json.put( "days", 0 );

        return json;
    }

    public void setPropsDataFromJson( JSONObject json ) {

        double interest = json.getDouble( "interest" );
        double devidend = json.getDouble( "devidend" );
        double borrow = json.getDouble( "borrow" );
        double days = json.getDouble( "days" );

        setInterestZero( interest - 1 );
        setInterest( interest );
        setDevidend( devidend );
        setBorrow( borrow );
        setDaysToExp( days );

    }

    public List< Strike > getStrikes() {
        return strikes;
    }

    public void setStrikes( List< Strike > strikes ) {
        this.strikes = strikes;
    }

    public String toStringVertical() {
        String string = "";

        string += getName( ) + "\n\n";

        for ( Strike strike : strikes ) {
            string += strike.toString( ) + "\n\n";
        }
        return string;
    }

    public JSONObject getOptionsAsJson() {

        JSONObject mainJson = new JSONObject( );

        JSONObject optionsData = new JSONObject( );

        JSONObject callJson;
        JSONObject putJson;
        JSONObject strikeJson;

        for ( Strike strike : strikes ) {

            callJson = new JSONObject( );
            putJson = new JSONObject( );
            strikeJson = new JSONObject( );

            Call call = strike.getCall( );
            callJson.put( "bid", call.getBid( ) );
            callJson.put( "ask", call.getAsk( ) );
            callJson.put( "bid_ask_counter", call.getBidAskCounter( ) );

            Put put = strike.getPut( );
            putJson.put( "bid", put.getBid( ) );
            putJson.put( "ask", put.getAsk( ) );
            putJson.put( "bid_ask_counter", put.getBidAskCounter( ) );

            strikeJson.put( "call", callJson );
            strikeJson.put( "put", putJson );

            optionsData.put( str( strike.getStrike( ) ), strikeJson );
        }

        mainJson.put( "contractBidAskCounter", getContractBidAskCounter( ) );
        mainJson.put( "con", getContract( ) );
        mainJson.put( "props", getProps( ) );
        mainJson.put( "data", optionsData );

        return mainJson;
    }

    public JSONObject getEmptyOptionsAsJson() {

        JSONObject mainJson = new JSONObject( );

        JSONObject optionsData = new JSONObject( );

        JSONObject callJson;
        JSONObject putJson;
        JSONObject strikeJson;

        for ( Strike strike : strikes ) {

            callJson = new JSONObject( );
            putJson = new JSONObject( );
            strikeJson = new JSONObject( );

            callJson.put( "bid", 0 );
            callJson.put( "ask", 0 );
            callJson.put( "bid_ask_counter", 0 );

            putJson.put( "bid", 0 );
            putJson.put( "ask", 0 );
            putJson.put( "bid_ask_counter", 0 );

            strikeJson.put( "call", callJson );
            strikeJson.put( "put", putJson );

            optionsData.put( str( strike.getStrike( ) ), strikeJson );
        }

        mainJson.put( "contractBidAskCounter", 0 );
        mainJson.put( "equalMove", 0 );
        mainJson.put( "con", 0 );
        mainJson.put( "props", getEmptyProps( ) );
        mainJson.put( "opAvg", 0 );
        mainJson.put( "opAvg15", 0 );

        mainJson.put( "data", optionsData );


        return mainJson;
    }

    public void setDataFromJson( JSONObject json ) {

        setContractBidAskCounter( json.getInt( "contractBidAskCounter" ) );
        setPropsDataFromJson( json.getJSONObject( "props" ) );
        setOptionsData( json.getJSONObject( "data" ) );

    }

    public void setOptionsData( JSONObject json ) {

        for ( Strike strike : getStrikes( ) ) {
            try {
                double strikePrice = strike.getStrike( );

                // Get data from json
                JSONObject callJson = json.getJSONObject( str( strikePrice ) ).getJSONObject( "call" );
                JSONObject putJson = json.getJSONObject( str( strikePrice ) ).getJSONObject( "put" );

                // Set data to options
                strike.getCall( ).setBidAskCounter( callJson.getInt( "bid_ask_counter" ) );
                strike.getPut( ).setBidAskCounter( putJson.getInt( "bid_ask_counter" ) );

            } catch ( Exception e ) {
//				e.printStackTrace ( );
            }
        }
    }

    public void resetOptionsBidAskCounter() {

        try {
            for ( Strike strike : getStrikes( ) ) {

                Call call = strike.getCall( );
                Put put = strike.getPut( );

                call.setBidAskCounter( 0 );
                call.getBidAskCounterList( ).clear( );

                put.setBidAskCounter( 0 );
                put.getBidAskCounterList( ).clear( );
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }

    public double floor( double d, int zeros ) {
        return Math.floor( d * zeros ) / zeros;
    }

    public String str( Object o ) {
        return String.valueOf( o );
    }

    public double absolute( double d ) {
        return Math.abs( d );
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate( LocalDate expDate ) {
        this.expDate = expDate;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest( double interest ) {
        this.interest = interest;
    }

    public void setInterestWithCalc( double interest ) {

        this.interestZero = interest * 0.01;
        this.interest = 1 + ( interest * 0.01 );
    }

    public double getInterestZero() {
        return interestZero;
    }

    public void setInterestZero( double interestZero ) {
        this.interestZero = interestZero;
    }

    public double getDevidend() {
        return devidend;
    }

    public void setDevidend( double devidend ) {
        this.devidend = devidend;
    }

    public double getCalcDevidend() {

        if ( devidend <= 0 ) {
            return 0;
        }

        double calcDev = getDevidend( ) * 360.0 / getDays( ) / client.getFuture( );

        if ( Double.isInfinite( calcDev ) ) {
            return 0;
        }

        return calcDev;
    }

    private double getBorrow() {
        return borrow;
    }

    public void setBorrow( double borrow ) {
        this.borrow = borrow;
    }

    public double getCalcBorrow() {
        if ( getBorrow( ) != 0 ) {
            return getBorrow( );
        } else {
            return floor( client.getFuture( ) * 0.002 / 360.0 * getDays( ), 10000 );
        }
    }

    public int getContractBidAskCounter() {
        return contractBidAskCounter;
    }

    public void setContractBidAskCounter( int contractBidAskCounter ) {
        this.contractBidAskCounter = contractBidAskCounter;
    }

    public LocalDate getToDay() {
        return toDay;
    }

    public int getMinId() {
        return minId;
    }

    public void setMinId( int minId ) {

        if ( this.minId != 0 ) {

            if ( minId < this.minId ) {
                this.minId = minId;
            }

        } else {
            this.minId = minId;
        }
    }

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId( int maxId ) {

        if ( this.maxId != 0 ) {

            if ( maxId > this.maxId ) {
                this.maxId = maxId;
            }

        } else {
            this.maxId = maxId;
        }

    }

    public int getBaseID() {
        return baseID;
    }

    public void setBaseID( int baseID ) {
        this.baseID = baseID;
    }

    // ---------- Basic Functions ---------- //
    public double getDays() {

        if ( daysToExp == -1 ) {

            if ( expDate != null ) {

                double d = ( int ) ChronoUnit.DAYS.between( getToDay( ), expDate );
                daysToExp = d + 2;

            }
        }
        return daysToExp;
    }

    public MyChartList getConBidAskCounterList() {
        return conBidAskCounterList;
    }

    public void setConBidAskCounterList( MyChartList conBidAskCounterList ) {
        this.conBidAskCounterList = conBidAskCounterList;
    }

    public void setDaysToExp( double daysToExp ) {
        this.daysToExp = daysToExp;
    }

    private double dbl( String s ) {
        return Double.parseDouble( s );
    }

    private int INT( String s ) {
        return Integer.parseInt( s );
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested( boolean requested ) {
        this.requested = requested;
    }

    public boolean isGotData() {
        return gotData;
    }

    public void setGotData( boolean gotData ) {
        this.gotData = gotData;
    }

    public int getType() {
        return type;
    }

    public void setType( int type ) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public double getContractBid() {
        return contractBid;
    }

    public void setContractBid( double contractBid ) {
        this.contractBid = contractBid;
    }

    public double getContractAsk() {
        return contractAsk;
    }

    public void setContractAsk( double contractAsk ) {
        this.contractAsk = contractAsk;
    }

    public List< Double > getOpList() {
        return opList;
    }

    public List< Double > getOpAvgList() {
        return opAvgList;
    }

    public List< Double > getConList() {
        return conList;
    }

    public List< Double > getConBidList() {
        return conBidList;
    }

    public List< Double > getConAskList() {
        return conAskList;
    }

    public void setContract( double contract ) {
        this.contract = contract;
    }

    public double getOpAvg() {
        return opAvg;
    }

    public void setOpAvg( double opAvg ) {
        this.opAvg = opAvg;
    }
}
