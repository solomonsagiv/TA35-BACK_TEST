package shlomi.positions;

import locals.L;
import serverObjects.BASE_CLIENT_OBJECT;

public class Short extends Position {

    // Constructor
    public Short( BASE_CLIENT_OBJECT client, int quantity, int id ) {
        super( client, quantity, id );
    }

    @Override
    public double getTheoPnl() {
        return L.floor( getStartPrice() - getClient().getIndex(), 10 );
    }

    @Override
    public double getPnl() {
        return L.floor( getStartPrice() - getClosePrice(), 10 );
    }
}
