package serverObjects.indexObjects;

import dataBase.mySql.mySqlComps.MyTableHandler;
import dataBase.mySql.tables.MyDayTable;
import dataBase.mySql.tables.MySumTable;

import java.time.LocalTime;

public class NdxCLIENTObject extends INDEX_CLIENT_OBJECT {

    static NdxCLIENTObject client = null;

    // Private constructor
    public NdxCLIENTObject() {
        super( );
    }

    // Get instance
    public static NdxCLIENTObject getInstance() {
        if ( client == null ) {
            client = new NdxCLIENTObject( );
        }
        return client;
    }

    @Override
    public double getEqualMovePlag() {
        return .6;
    }

    @Override
    public double getIndexBidAskMargin() {
        return 1.25;
    }

    @Override
    public void initTwsData() {


    }

    @Override
    public void initStartOfIndexTrading() {
        setStartOfIndexTrading( LocalTime.of( 16, 30, 0 ) );
    }

    @Override
    public void initEndOfIndexTrading() {
        setEndOfIndexTrading( LocalTime.of( 23, 0, 0 ) );
    }

    @Override
    public void initEndOfFutureTrading() {
        setEndFutureTrading( LocalTime.of( 23, 15, 0 ) );
    }

    @Override
    public void initIds() {

        setBaseId( 20000 );

    }

    @Override
    public void initDbId() {
        setDbId( 3 );
    }

    @Override
    public void initName() {
        setName( "ndx" );
    }

    @Override
    public void initRacesMargin() {
        setRacesMargin( .7 );
    }

    @Override
    public double getStrikeMargin() {
        return 40;
    }

    @Override
    public void initTablesHandlers() {
        MyDayTable myDayTable = new MyDayTable( this, "ndx" );
        MySumTable mySumTable = new MySumTable( this, "ndx_daily" );

        myTableHandler = new MyTableHandler( this, myDayTable, mySumTable );
    }

    @Override
    public double getTheoAvgMargin() {
        return 0;
    }

}
