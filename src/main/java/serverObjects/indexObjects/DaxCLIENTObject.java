package serverObjects.indexObjects;

import dataBase.mySql.mySqlComps.MyTableHandler;
import dataBase.mySql.tables.MyDayTable;
import dataBase.mySql.tables.MySumTable;

import java.time.LocalTime;

public class DaxCLIENTObject extends INDEX_CLIENT_OBJECT {

    static DaxCLIENTObject client = null;

    // Private constructor
    private DaxCLIENTObject() {
        super( );
        setSpecificData( );
    }

    // Get instance
    public static DaxCLIENTObject getInstance() {
        if ( client == null ) {
            client = new DaxCLIENTObject( );
        }
        return client;
    }

    // Specification
    private void setSpecificData() {
        // Equal move
        setEqualMovePlag( 1 );
    }

    @Override
    public void initTwsData() {


    }


    @Override
    public void initTablesHandlers() {

        MyDayTable myDayTable = new MyDayTable( this, "dax" );
        MySumTable mySumTable = new MySumTable( this, "dax_daily" );

        myTableHandler = new MyTableHandler( this, myDayTable, mySumTable );
    }

    @Override
    public double getTheoAvgMargin() {
        return 0;
    }

    @Override
    public void initName() {
        setName( "dax" );
    }

    @Override
    public void initRacesMargin() {
        setRacesMargin( 1.5 );
    }

    @Override
    public double getStrikeMargin() {
        return 100;
    }

    @Override
    public void initStartOfIndexTrading() {
        setStartOfIndexTrading( LocalTime.of( 10, 0, 0 ) );
    }

    @Override
    public void initEndOfIndexTrading() {
        setEndOfIndexTrading( LocalTime.of( 18, 30, 0 ) );
    }

    @Override
    public void initEndOfFutureTrading() {
        initEndOfIndexTrading( );
    }

    @Override
    public void initIds() {
        setBaseId( 5000 );
    }

    @Override
    public void initDbId() {
        setDbId( 1 );
    }

}
