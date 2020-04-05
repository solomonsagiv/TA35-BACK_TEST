package serverObjects;

import arik.Arik;
import arik.locals.Emojis;
import dataBase.mySql.MySqlService;
import dataBase.mySql.mySqlComps.MyTableHandler;
import lists.MyChartList;
import locals.L;
import options.OptionsHandler;
import service.MyServiceHandler;
import shlomi.ClientUpdate;
import shlomi.window.DateTimeHandler;
import threads.MyThread;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class BASE_CLIENT_OBJECT implements IBaseClient {

    private DateTimeHandler dateTimeHandler;
    private ClientUpdate clientUpdate;

    protected MyTableHandler myTableHandler;
    // Table
    JTable racesTable;
    DefaultTableModel model = new DefaultTableModel( );
    // Options
    OptionsHandler optionsHandler;
    // Services
    MySqlService mySqlService;

    MyChartList indexList = new MyChartList( );
    MyChartList indexBidList = new MyChartList( );
    MyChartList indexAskList = new MyChartList( );
    MyChartList indexRacesList = new MyChartList( );
    MyChartList indexBidAskCounterList = new MyChartList( );

    private double startStrike;
    private double endStrike;
    private LocalTime startOfIndexTrading;
    private LocalTime endOfIndexTrading;
    private LocalTime endFutureTrading;
    private String[] stocksNames;
    private boolean loadFromDb = false;
    private boolean dbRunning = false;
    // Base id
    private int baseId;
    // Position
    private ArrayList< MyThread > threads = new ArrayList<>( );
    private HashMap< String, Integer > ids = new HashMap<>( );
    private boolean started = false;
    private boolean loadStatusFromHB = false;
    private boolean loadArraysFromHB = false;
    // Lists map
    private String name = null;
    // DB
    private int dbId = 0;
    // Options handler
    // MyService
    private MyServiceHandler myServiceHandler = new MyServiceHandler( this );
    // OpMove
    private double equalMovePlag = 0;
    // Basic
    private double dbContract = 0;
    private double index = 0;
    private double indexBid = 0;
    private double indexAsk = 0;
    private double future = 0;
    private double futureBid = 0;
    private double futureAsk = 0;
    private double open = 0;
    private double high = 0;
    private double low = 0;
    private double base = 0;
    private int futureBidAskCounter = 0;
    private int indexBidAskCounter = 0;
    private double indexBidAskMargin = 0;
    private int basketUp = 0;
    private int basketDown = 0;

    // Races
    private double racesMargin = 0;
    private double optimiPesimiMargin = 0;
    private int conUp = 0;
    private int conDown = 0;
    private int indexUp = 0;
    private int indexDown = 0;
    private int optimiPesimiCount = 0;

    public BASE_CLIENT_OBJECT() {

        initTwsData( );

        // Call subClasses abstract functions
        initIds( );
        initName( );
        initRacesMargin( );
        initStartOfIndexTrading( );
        initEndOfIndexTrading( );
        initEndOfFutureTrading( );
        initDbId( );
        initTablesHandlers( );

        // MyServices
//        mySqlService = new MySqlService( this );
//        clientUpdate = new ClientUpdate( );
        dateTimeHandler = new DateTimeHandler( );

    }

    public LocalDate convertStringToDate( String dateString ) {

        if ( dateString.length( ) == 8 ) {

            String year = dateString.substring( 0, 4 );
            String month = dateString.substring( 4, 6 );
            String day = dateString.substring( 6, 8 );

            String fullDate = year + "-" + month + "-" + day;
            return LocalDate.parse( fullDate );
        }

        return null;

    }

    // Start all
    public void closeAll() {
        getMyServiceHandler( ).getHandler( ).close( );
        for ( MyThread myThread : getThreads( ) ) {
            myThread.getHandler( ).close( );
        }
        setStarted( false );
    }

    public void fullExport() {
        try {
            getMyTableHandler( ).getMySumTable( ).insert( );
            getMyTableHandler( ).getMyStatusTable( ).reset( );
            getMyTableHandler( ).getMyArraysTable( ).reset( );

            // Notify
            Arik.getInstance( ).sendMessage( Arik.sagivID, getName( ) + " Export success " + Emojis.check_mark, null );

        } catch ( Exception e ) {
            // Notify
            Arik.getInstance( ).sendMessage( Arik.sagivID,
                    getName( ) + " Export faild " + Emojis.stop + "\n" + e.getStackTrace( ).toString( ), null );
        }

    }

    public void handleHigh() {
        if ( index > high ) {
            high = index;
        }
    }

    public void handleLow() {
        if ( low != 0 ) {
            if ( index < low ) {
                low = index;
            }
        } else {
            low = index;
        }
    }

    // ---------- basic functions ---------- //

    public String str( Object o ) {
        return String.valueOf( o );
    }

    public double floor( double d, int zeros ) {
        return Math.floor( d * zeros ) / zeros;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted( boolean started ) {
        this.started = started;
    }

    public int getConDown() {
        return conDown;
    }

    public void setConDown( int future_down ) {
        this.conDown = future_down;
    }

    public int getIndexUp() {
        return indexUp;
    }

    public void setIndexUp( int index_up ) {
        this.indexUp = index_up;
    }

    public int getIndexDown() {
        return indexDown;
    }

    public void setIndexDown( int index_down ) {
        this.indexDown = index_down;
    }

    public double getStartStrike() {
        return startStrike;
    }

    public void setStartStrike( double startStrike ) {
        this.startStrike = startStrike;
    }

    public double getEndStrike() {
        return endStrike;
    }

    public void setEndStrike( double endStrike ) {
        this.endStrike = endStrike;
    }

    public double getRacesMargin() {
        return racesMargin;
    }

    public void setRacesMargin( double racesMargin ) {
        this.racesMargin = racesMargin;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void setModel( DefaultTableModel model ) {
        this.model = model;
    }

    public LocalTime getStartOfIndexTrading() {
        return startOfIndexTrading;
    }

    public void setStartOfIndexTrading( LocalTime startOfIndexTrading ) {
        this.startOfIndexTrading = startOfIndexTrading;
    }

    public LocalTime getEndOfIndexTrading() {
        return endOfIndexTrading;
    }

    public void setEndOfIndexTrading( LocalTime endOfIndexTrading ) {
        this.endOfIndexTrading = endOfIndexTrading;
    }

    public void setIds( HashMap< String, Integer > ids ) {
        this.ids = ids;
    }

    public String toStringPretty() {
        String originalToString = toString( );
        String newTostring = originalToString.replaceAll( ", ", "\n" );
        return newTostring;
    }

    public String getArikSumLine() {

        String text = "";
        text += "***** " + getName( ).toUpperCase( ) + " *****" + "\n";
        text += "Date: " + LocalDate.now( ).minusDays( 1 ) + "\n";
        text += "Open: " + open + "\n";
        text += "High: " + high + "\n";
        text += "Low: " + low + "\n";
        text += "Close: " + index + "\n";
        text += "OP avg: " + L.format100( getOptionsHandler( ).getMainOptions( ).getContract( ) ) + "\n";
        text += "Ind races: " + getIndexSum( ) + "\n";
        text += "Contract counter: " + getOptionsHandler( ).getMainOptions( ).getContractBidAskCounter( ) + "\n";

        return text;
    }

    public boolean isLoadFromDb() {
        return loadStatusFromHB && loadArraysFromHB;
    }

    public void setLoadFromDb( boolean loadFromDb ) {
        this.loadFromDb = loadFromDb;
    }

    public int getConUp() {
        return conUp;
    }

    public void setConUp( int conUp ) {
        this.conUp = conUp;
    }

    public void conUpPlus() {
        conUp++;
    }

    public void conDownPlus() {
        conDown++;
    }

    public void indUpPlus() {
        indexUp++;
    }

    public void indDownPlus() {
        indexDown++;
    }

    public boolean isDbRunning() {
        return dbRunning;
    }

    public void setDbRunning( boolean dbRunning ) {
        this.dbRunning = dbRunning;
    }

    public ArrayList< MyThread > getThreads() {
        return threads;
    }

    public void setThreads( ArrayList< MyThread > threads ) {
        this.threads = threads;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId( int dbId ) {
        this.dbId = dbId;
    }

    public void setLoadStatusFromHB( boolean loadStatusFromHB ) {
        this.loadStatusFromHB = loadStatusFromHB;
    }

    public void setLoadArraysFromHB( boolean loadArraysFromHB ) {
        this.loadArraysFromHB = loadArraysFromHB;
    }

    public int getIndexSum() {
        return indexUp - indexDown;
    }

    public int getFutSum() {
        return conUp - conDown;
    }

    public double getEqualMovePlag() {
        return equalMovePlag;
    }

    public void setEqualMovePlag( double equalMovePlag ) {
        this.equalMovePlag = equalMovePlag;
    }

    public double getIndexBidAskMargin() {
        return indexBidAskMargin;
    }

    public void setIndexBidAskMargin( double indexBidAskMargin ) {
        this.indexBidAskMargin = indexBidAskMargin;
    }

    public LocalTime getEndFutureTrading() {
        return endFutureTrading;
    }

    public void setEndFutureTrading( LocalTime endFutureTrading ) {
        this.endFutureTrading = endFutureTrading;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getBaseId() {
        return baseId;
    }

    public void setBaseId( int baseId ) {
        this.baseId = baseId;
    }

    public OptionsHandler getOptionsHandler() {
        if ( optionsHandler == null ) {
            optionsHandler = new OptionsHandler( this );
        }
        return optionsHandler;
    }

    public abstract double getTheoAvgMargin();

    public MyServiceHandler getMyServiceHandler() {
        return myServiceHandler;
    }

    public MyTableHandler getMyTableHandler() {
        return myTableHandler;
    }

    public JTable getRacesTable() {
        return racesTable;
    }

    public void setRacesTable( JTable racesTable ) {
        this.racesTable = racesTable;
    }

    public double getIndex() {
        return index;
    }

    public void setIndex( double index ) {
        this.index = index;
    }

    public double getIndexBid() {
        return indexBid;
    }

    public void setIndexBid( double indexBid ) {
        this.indexBid = indexBid;
    }

    public double getIndexAsk() {
        return indexAsk;
    }

    public void setIndexAsk( double indexAsk ) {
        this.indexAsk = indexAsk;
    }

    public double getFuture() {
        return future;
    }

    // ---------- Getters and Setters ---------- //
    public void setFuture( double future ) {
        if ( this.future == 0 ) {
            this.future = future;
            getOptionsHandler( ).initOptions( future );
        }
    }

    public double getFutureBid() {
        return futureBid;
    }

    public void setFutureBid( double futureBid ) {

        if ( futureBid > this.futureBid ) {
            futureBidAskCounter++;
        }

        this.futureBid = futureBid;
    }

    public double getFutureAsk() {
        return futureAsk;
    }

    public void setFutureAsk( double futureAsk ) {

        if ( futureAsk < this.futureAsk ) {
            futureBidAskCounter--;
        }

        this.futureAsk = futureAsk;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen( double open ) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh( double high ) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow( double low ) {
        this.low = low;
    }

    public double getBase() {
        return base;
    }

    public void setBase( double base ) {
        this.base = base;
    }

    public int getFutureBidAskCounter() {
        return futureBidAskCounter;
    }

    public int getIndexSumRaces() {
        return indexUp - indexDown;
    }

    public String[] getStocksNames() {
        return stocksNames;
    }

    public void setStocksNames( String[] stocksNames ) {
        this.stocksNames = stocksNames;
    }

    public MyChartList getIndexList() {
        return indexList;
    }

    public MyChartList getIndexBidAskCounterList() {
        return indexBidAskCounterList;
    }

    public void setIndexBidAskCounterList( MyChartList indexBidAskCounterList ) {
        this.indexBidAskCounterList = indexBidAskCounterList;
    }

    public MyChartList getIndexBidList() {
        return indexBidList;
    }

    public MyChartList getIndexAskList() {
        return indexAskList;
    }

    public MyChartList getIndexRacesList() {
        return indexRacesList;
    }

    public MySqlService getMySqlService() {
        return mySqlService;
    }

    public DateTimeHandler getDateTimeHandler() {
        return dateTimeHandler;
    }

    public ClientUpdate getClientUpdate() {
        return clientUpdate;
    }

    public int getIndexBidAskCounter() {
        return indexBidAskCounter;
    }

    public void setIndexBidAskCounter( int indexBidAskCounter ) {
        this.indexBidAskCounter = indexBidAskCounter;
    }

    public int getBasketUp() {
        return basketUp;
    }

    public void setBasketUp( int basketUp ) {
        this.basketUp = basketUp;
    }

    public int getBasketDown() {
        return basketDown;
    }

    public void setBasketDown( int basketDown ) {
        this.basketDown = basketDown;
    }

    public int getBaskets() {
        return basketUp - basketDown;
    }

    @Override
    public String toString() {
        return "BASE_CLIENT_OBJECT{" +
                "racesTable=" + racesTable +
                ", model=" + model +
                ", optionsHandler=" + optionsHandler +
                ", startStrike=" + startStrike +
                ", endStrike=" + endStrike +
                ", startOfIndexTrading=" + startOfIndexTrading +
                ", endOfIndexTrading=" + endOfIndexTrading +
                ", endFutureTrading=" + endFutureTrading +
                ", stocksNames=" + Arrays.toString( stocksNames ) +
                ", loadFromDb=" + loadFromDb +
                ", dbRunning=" + dbRunning +
                ", baseId=" + baseId +
                ", threads=" + threads +
                ", ids=" + ids +
                ", started=" + started +
                ", loadStatusFromHB=" + loadStatusFromHB +
                ", loadArraysFromHB=" + loadArraysFromHB +
                ", name='" + name + '\'' +
                ", dbId=" + dbId +
                ", equalMovePlag=" + equalMovePlag +
                ", dbContract=" + dbContract +
                ", index=" + index +
                ", indexBid=" + indexBid +
                ", indexAsk=" + indexAsk +
                ", future=" + future +
                ", futureBid=" + futureBid +
                ", futureAsk=" + futureAsk +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", base=" + base +
                ", futureBidAskCounter=" + futureBidAskCounter +
                ", indexBidAskMargin=" + indexBidAskMargin +
                ", racesMargin=" + racesMargin +
                ", optimiPesimiMargin=" + optimiPesimiMargin +
                ", conUp=" + conUp +
                ", conDown=" + conDown +
                ", indexUp=" + indexUp +
                ", indexDown=" + indexDown +
                ", optimiPesimiCount=" + optimiPesimiCount +
                '}';
    }

}
