package shlomi;

import options.Options;
import serverObjects.BASE_CLIENT_OBJECT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class ClientUpdate {

    BASE_CLIENT_OBJECT client;
    Options mainOptions;
    Options quarterOptions;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "HH:mm:ss", Locale.US );

    public ClientUpdate( BASE_CLIENT_OBJECT client ) {
        this.client = client;
        mainOptions = client.getOptionsHandler( ).getMainOptions( );
        quarterOptions = client.getOptionsHandler( ).getOptionsQuarter( );
    }

    public void updateDayData( ResultSet rs ) throws SQLException, DateTimeParseException {

        client.getDateTimeHandler().setDate( rs.getString( "date" ) );
        client.getDateTimeHandler().setTime( formatter.format( LocalTime.parse( rs.getString( "time" ) ) ) );

        mainOptions.setContract( rs.getDouble( "con" ) );
//            mainOptions.getOpAvgMoveService( ).setMove( rs.getDouble( "opAvgMove" ) );

        mainOptions.setOpAvg( rs.getDouble( "op_avg" ) );
        quarterOptions.setContract( rs.getDouble( "conQuarter" ) );
        client.setIndex( rs.getDouble( "ind" ) );
        client.setConUp( rs.getInt( "con_up" ) );
        client.setConDown( rs.getInt( "con_down" ) );
        client.setIndexUp( rs.getInt( "index_up" ) );
        client.setIndexDown( rs.getInt( "index_down" ) );
        client.setBase( rs.getDouble( "base" ) );
        client.handleHigh( );
        client.handleLow( );
    }
}
