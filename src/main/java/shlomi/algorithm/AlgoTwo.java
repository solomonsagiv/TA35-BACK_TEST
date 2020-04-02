package shlomi.algorithm;

import locals.L;
import options.Options;
import serverObjects.BASE_CLIENT_OBJECT;
import shlomi.positions.Positions;

public class AlgoTwo extends Algorithm {

    // Variables
    Options options;
    double opAvgPlag = 0.1;
    int indexSumPlag = 0;
    int quantity = 1;

    // Constructor
    public AlgoTwo( BASE_CLIENT_OBJECT client, int type ) {
        super( client, type );
        this.options = client.getOptionsHandler( ).getMainOptions( );
        dateTime = client.getDateTimeHandler();
    }

    @Override
    public void doAlgo() {

        int indSum = client.getIndexSum( );
        double opAvg = options.getOpAvg( );

        // Long
        if ( positions.posStatus == Positions.NOT_IN_POSITION ) {
            if ( opAvg < -opAvgPlag && indSum > indexSumPlag ) {
                System.out.println( "OpAvg: " + opAvg + " ind: " + indSum );
                positions.doLong( quantity );
            }
        }

        // Exit long
        if ( positions.posStatus == Positions.LONG ) {
            if ( indSum <= 0 ) {
                System.out.println( "OpAvg: " + opAvg + " ind: " + indSum );
                positions.closeLong( );
            }
        }

        // Short
        if ( positions.posStatus == Positions.NOT_IN_POSITION ) {
            if ( opAvg > opAvgPlag && indSum < -indexSumPlag ) {
                System.out.println( "OpAvg: " + opAvg + " ind: " + indSum );
                positions.doShort( quantity );
            }
        }

        // Exit short
        if ( positions.posStatus == Positions.SHORT ) {
            if ( indSum >= 0 ) {
                System.out.println( "OpAvg: " + opAvg + " ind: " + indSum );
                positions.closeShort( );
            }
        }
        
        // End of the day
        if ( dateTime.getTime().isAfter( client.getEndOfIndexTrading().minusSeconds( 10 ) ) && positions.posStatus != Positions.CLOSE_END_OF_DAY ) {

            if ( positions.posStatus == Positions.LONG ) {
                positions.closeLong();
                positions.posStatus = Positions.CLOSE_END_OF_DAY;
            }

            if ( positions.posStatus == Positions.SHORT ) {
                positions.closeShort();
                positions.posStatus = Positions.CLOSE_END_OF_DAY;
            }

            System.out.println( );
            System.out.println( " ---------- Total ---------- " );
            System.out.println( L.floor( positions.getTotalPnl(), 10 ) );
            System.out.println( );
        }

    }

    @Override
    public void setClient( BASE_CLIENT_OBJECT client ) {
        super.setClient( client );
        options = client.getOptionsHandler( ).getMainOptions( );
    }
}
