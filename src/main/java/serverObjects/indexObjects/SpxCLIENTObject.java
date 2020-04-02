package serverObjects.indexObjects;

import dataBase.mySql.mySqlComps.MyTableHandler;
import dataBase.mySql.tables.MyDayTable;
import dataBase.mySql.tables.MySumTable;

import java.time.LocalTime;

public class SpxCLIENTObject extends INDEX_CLIENT_OBJECT {

    // Constructor
    public SpxCLIENTObject() {
        super( );
    }

    @Override
    public double getEqualMovePlag() {
        return .25;
    }

    @Override
    public double getIndexBidAskMargin() {
        return .5;
    }

    @Override
    public void initTwsData() {
    }

    @Override
    public void initName() {
        setName( "spx" );
    }

    @Override
    public void initRacesMargin() {
        setRacesMargin( .3 );
    }

    @Override
    public double getStrikeMargin() {
        return 5;
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

        setBaseId( 10000 );

    }

    @Override
    public void initDbId() {
        setDbId( 2 );
    }

    @Override
    public void initTablesHandlers() {

        MyDayTable myDayTable = new MyDayTable( this, "spx" );
        MySumTable mySumTable = new MySumTable( this, "spx_daily" );

        myTableHandler = new MyTableHandler( this, myDayTable, mySumTable );
    }

    @Override
    public double getTheoAvgMargin() {
        return 0.05;
    }


}
