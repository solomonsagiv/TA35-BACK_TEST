package shlomi.algorithm;

import serverObjects.BASE_CLIENT_OBJECT;
import shlomi.positions.Positions;
import shlomi.window.DateTimeHandler;

public abstract class Algorithm implements IAlgorithm {


    // Variables
    BASE_CLIENT_OBJECT client;
    int type;
    protected Positions positions;
    protected DateTimeHandler dateTime;

    // Constructor
    public Algorithm( BASE_CLIENT_OBJECT client, int type ) {
        this.client = client;
        this.type = type;
        dateTime = client.getDateTimeHandler();
        positions = new Positions( client );
    }

    public Algorithm() {
    }

    //  Getters and Setters
    public BASE_CLIENT_OBJECT getClient() {
        return client;
    }

    public void setClient( BASE_CLIENT_OBJECT client ) {
        this.client = client;
        positions.setClient( client );
        dateTime = client.getDateTimeHandler();
    }

    public int getType() {
        return type;
    }

    public void setType( int type ) {
        this.type = type;
    }
}
