package shlomi;

import charts.myCharts.IndexVsConBidAskCounterChart;
import options.Options;
import serverObjects.BASE_CLIENT_OBJECT;
import serverObjects.indexObjects.SpxCLIENTObject;
import serverObjects.indexObjects.TA35;
import shlomi.algorithm.Algorithm;
import shlomi.window.DateTimeHandler;
import shlomi.window.ShlomiWindow;
import threads.MyThread;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;

public class MainThread extends MyThread implements Runnable {

    public int preSleep = 0;
    public static int sleep = 100;
    public final int baseSleep = 10;
    Algorithm algorithm;
    ResultSet rs;

    public static IndexVsConBidAskCounterChart chart;

    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int STOP = 2;
    public int direction = FORWARD;

    Options mainOptions;
    DateTimeHandler dateTimeHandler;
    ClientUpdate clientUpdate;

    // Constructor
    public MainThread( ResultSet rs, Algorithm algorithm ) {
        super( TA35.getInstance() );
        System.out.println("Client 3: " + TA35.getInstance().hashCode() );
        this.rs = rs;
        this.algorithm = algorithm;
        clientReset();
    }

    public void clientReset( ) {
        System.out.println("Client 4: " + TA35.getInstance().hashCode() );
        clientUpdate = new ClientUpdate();
        mainOptions = TA35.getInstance().getOptionsHandler( ).getMainOptions( );
        algorithm.setClient( TA35.getInstance() );
        dateTimeHandler = TA35.getInstance().getDateTimeHandler();
        ShlomiWindow.futurePanel.setClient( TA35.getInstance() );
        sleep = 100;

        if ( chart != null ) {
            chart.createChart();
        }

    }

    @Override
    public void run() {
        init( );
    }

    private void init() {

        while ( isRun( ) ) {
            try {

                // Sleep
                Thread.sleep( sleep );

                // Handle direction
                directionHandle( rs );

                // Handle days
                handlerDays( );

                // Update the client
                clientUpdate.updateDayData( rs );

                // Calculations
                calcs();

                // Text
                text();

                if ( chart != null ) {
                    chart.doWork( );
                }

                // TODO shlomi algo
                algorithm.doAlgo();

            } catch ( SQLException | DateTimeParseException e ) {
                e.printStackTrace( );
                getHandler().close();
                break;
            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }
    }

    private void text() throws InterruptedException {
        // Set future panel text
        ShlomiWindow.futurePanel.setText( );

        // Set text
        ShlomiWindow.updateWindowText( TA35.getInstance().getDateTimeHandler() );
    }

    long startTime = System.currentTimeMillis( );
    long finishTime = System.currentTimeMillis( );

    private void handlerDays() throws SQLException {

        if ( dateTimeHandler.getDate( ) == null || !rs.getString( "date" ).equals( dateTimeHandler.getDate( ).toString( ) ) ) {
            setClient( new SpxCLIENTObject( ) );
            clientReset();
        }

        if ( rs.isLast( ) ) {
            ShlomiWindow.settingPanel.startBtn.setEnabled( true );
            getHandler().close();
        }

        if ( rs.isFirst( ) ) {
            getClient( ).setOpen( rs.getDouble( "ind" ) );
        }
    }

    private void calcs() {
        // OpAvgMove
    }

    public void forward() {
        if ( direction == STOP ) {
            direction = FORWARD;
            sleep = baseSleep;
            getHandler( ).interrupt( );
        } else if ( direction == FORWARD ) {
            sleep /= 2;
        } else if ( direction == BACKWARD ) {
            direction = FORWARD;
            sleep = baseSleep;
        }
    }

    public void backward() {
        if ( direction == STOP ) {
            direction = BACKWARD;
            sleep = baseSleep;
            getHandler( ).interrupt( );
        } else if ( direction == BACKWARD ) {
            sleep /= 2;
        } else if ( direction == FORWARD ) {
            direction = BACKWARD;
            sleep = baseSleep;
        }
    }

    public void finish() {
        sleep = 0;
        direction = FORWARD;
    }


    public void stop() {
        sleep = 1000000;
        direction = STOP;
    }

    public void slower() {
        if ( sleep == 0 ) {
            sleep = 2;
        }
        sleep *= 3;
    }

    private void directionHandle( ResultSet rs ) throws SQLException {
        if ( direction == FORWARD ) {
            if ( !rs.isLast( ) ) {
                rs.next( );
            }
        } else if ( direction == BACKWARD ) {
            if ( !rs.isFirst( ) ) {
                rs.previous( );
            }
        }
    }

    private void go() {
        algorithm.doAlgo( );
    }

    @Override
    public void initRunnable() {
        setRunnable( this );
    }


}
