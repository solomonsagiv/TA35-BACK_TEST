package shlomi;

import lists.MyChartPoint;
import options.Options;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import serverObjects.BASE_CLIENT_OBJECT;
import serverObjects.indexObjects.TA35;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class ClientUpdate {

    Options mainOptions;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "HH:mm:ss", Locale.US );

    public ClientUpdate( ) {
        mainOptions = TA35.getInstance().getOptionsHandler( ).getMainOptions( );
    }

    public void updateDayData( ResultSet rs ) throws SQLException, DateTimeParseException {

        System.out.println(TA35.getInstance().hashCode() );

        TA35.getInstance().getDateTimeHandler().setDate( rs.getString( "date" ) );
        TA35.getInstance().getDateTimeHandler().setTime( formatter.format( LocalTime.parse( rs.getString( "time" ) ) ) );

        mainOptions.setContract( rs.getDouble( "fut" ) );
        mainOptions.setOpAvg( rs.getDouble( "opAvg" ) );
        TA35.getInstance().setIndex( rs.getDouble( "ind" ) );
        TA35.getInstance().setConUp( rs.getInt( "futUp" ) );
        TA35.getInstance().setConDown( rs.getInt( "futDown" ) );
        TA35.getInstance().setIndexUp( rs.getInt( "indUp" ) );
        TA35.getInstance().setIndexDown( rs.getInt( "indDown" ) );
        TA35.getInstance().setBasketUp( rs.getInt( "basketUp" ) );
        TA35.getInstance().setBasketDown( rs.getInt( "basketDown" ) );
        TA35.getInstance().setBase( rs.getDouble( "base" ));
        TA35.getInstance().getIndexList().add(  new MyChartPoint( rs.getString( "time" ), rs.getDouble( "ind" ) )  );
        mainOptions.setContractBidAskCounter( rs.getInt( "futBidAskCounter" ) );
        mainOptions.getConBidAskCounterList().add( new MyChartPoint( rs.getString( "time" ) , rs.getInt( "futBidAskCounter" ) ) );

        TA35.getInstance().handleHigh( );
        TA35.getInstance().handleLow( );
    }
}
