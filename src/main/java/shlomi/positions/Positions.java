package shlomi.positions;

import serverObjects.BASE_CLIENT_OBJECT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Positions {

    public static final int LONG = 0;
    public static final int SHORT = 1;
    public static final int NOT_IN_POSITION = 2;
    public static final int CLOSE_END_OF_DAY = 3;

    // Variables
    BASE_CLIENT_OBJECT client;
    private HashMap< Integer, Position > positionsMap = new HashMap<>( );
    private List< Position > positionsList = new ArrayList<>( );
    private int id = 0;
    private double twsTotalPnl = 0;
    private double twsPositionPnl = 0;
    private int positionStatus = 0;
    public int posStatus = NOT_IN_POSITION;

    // Constructor
    public Positions( BASE_CLIENT_OBJECT client ) {
        this.client = client;
    }

    // Functions
    public int getNextId() {

        int id = 0;

        for ( Entry< Integer, Position > entry : positionsMap.entrySet( ) ) {
            id = entry.getKey( );
        }

        return id++;
    }

    public Long doLong( int quantity ) {
        if ( posStatus == NOT_IN_POSITION ) {
            Long position = new Long( client, quantity, getNextId( ) );
            positionsList.add( position );
            positionsMap.put( position.getId( ), position );
            System.out.println( );
            System.out.println( "Long: " + position.getStartTime() );

            posStatus = LONG;
            return position;
        }
        return null;
    }

    public void closeLong() {
        try {
            if ( posStatus == LONG ) {
                Long position = ( Long ) getLastPosition( );
                if ( position.isLive( ) ) {
                    position.close( );
                    System.out.println( "Exit Long: " + position );

                    posStatus = NOT_IN_POSITION;
                }
            }
        } catch ( IndexOutOfBoundsException e ) {
        }
    }

    public Short doShort( int quantity ) {
        if ( posStatus == NOT_IN_POSITION ) {
            posStatus = SHORT;

            Short position = new Short( client, quantity, getNextId( ) );
            positionsList.add( position );
            positionsMap.put( position.getId( ), position );
            System.out.println( );
            System.out.println( "Short: " + position.getStartTime() );

            return position;
        }
        return null;
    }

    public void closeShort() {
        try {
            if ( posStatus == SHORT ) {
                Short position = ( Short ) getLastPosition( );
                if ( position.isLive( ) ) {
                    position.close( );
                    System.out.println( "Exit short: " + position );

                    posStatus = NOT_IN_POSITION;
                }
            }
        } catch ( IndexOutOfBoundsException e ) {
            e.printStackTrace();
        }
    }

    public Position getLastPosition() {
        if ( !positionsList.isEmpty( ) ) {
            Position position = positionsList.get( positionsList.size( ) - 1 );
            return position;
        } else {
            throw new IndexOutOfBoundsException( );
        }
    }


    public double getTotalPnl() {
        double pnl = 0;
        for ( Position position : positionsList ) {
            pnl += position.getPnl( );
        }
        return pnl;
    }

    public String getAllPositionText() {
        String text = "";
        for ( Position position : positionsList ) {
            text += position.toString( ) + " \n";
        }
        return text;
    }

    public double getTotalAvgPnl() {
        double pnl = getTotalPnl( );
        int posQuantity = positionsList.size( );
        return floor( pnl / posQuantity );
    }

    public void setClient( BASE_CLIENT_OBJECT client ) {
        this.client = client;
    }

    // Getters and setters
    public HashMap< Integer, Position > getPositions() {
        return positionsMap;
    }

    public void setPositions( HashMap< Integer, Position > positions ) {
        this.positionsMap = positions;
    }


    public double getTwsTotalPnl() {
        return twsTotalPnl;
    }


    public void setTwsTotalPnl( double twsTotalPnl ) {
        this.twsTotalPnl = twsTotalPnl;
    }


    public double getTwsPositionPnl() {
        return twsPositionPnl;
    }


    public void setTwsPositionPnl( double twsPositionPnl ) {
        this.twsPositionPnl = twsPositionPnl;
    }


    public int getPositionStatus() {
        return positionStatus;
    }


    public void setPositionStatus( int positionStatus ) {
        this.positionStatus = positionStatus;
    }


    public double floor( double d ) {
        return Math.floor( d * 100 ) / 100;
    }

}
