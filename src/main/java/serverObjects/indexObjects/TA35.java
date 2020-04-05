package serverObjects.indexObjects;

public class TA35 extends INDEX_CLIENT_OBJECT {

    static TA35 client = null;

    // Constructor
    private TA35() {
        super( );
    }

    // Get instance
    public static TA35 getInstance() {
        if ( client == null ) {
            client = new TA35( );
        }
        return client;
    }

    public static TA35 getNewInstance() {
        client = new TA35();
        return client;
    }

    @Override
    public double getTheoAvgMargin() {
        return 0;
    }

    @Override
    public void initIds() {

    }

    @Override
    public void initTwsData() {
    }

    @Override
    public void initName() {
        setName( "TA35" );
    }

    @Override
    public void initRacesMargin() {
        setRacesMargin( 0.1 );
    }

    @Override
    public double getStrikeMargin() {
        return 10;
    }

    @Override
    public void initStartOfIndexTrading() {

    }

    @Override
    public void initEndOfIndexTrading() {

    }

    @Override
    public void initEndOfFutureTrading() {

    }

    @Override
    public void initDbId() {

    }

    @Override
    public void initTablesHandlers() {

    }
}
