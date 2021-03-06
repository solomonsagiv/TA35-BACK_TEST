package serverObjects;

public interface IBaseClient {

    void initIds();

    void initTwsData();

    void initName();

    void initRacesMargin();

    double getStrikeMargin();

    void initStartOfIndexTrading();

    void initEndOfIndexTrading();

    void initEndOfFutureTrading();

    void initDbId();

    void initTablesHandlers();

}
