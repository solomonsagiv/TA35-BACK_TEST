package shlomi.positions;

import locals.L;
import serverObjects.BASE_CLIENT_OBJECT;

public class Long extends Position {

    // Constructor
    public Long( BASE_CLIENT_OBJECT client, int quantity, int id ) {
        super( client, quantity, id );
    }

    @Override
    public double getTheoPnl() {
        return L.floor( getClosePrice() - getClient().getIndex(), 10 );
    }

    @Override
    public double getPnl() {
        return L.floor( getClosePrice() - getStartPrice(), 10 );
    }

}
