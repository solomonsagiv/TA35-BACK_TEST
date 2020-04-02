package shlomi.positions;

import options.Option;
import serverObjects.BASE_CLIENT_OBJECT;

import java.time.LocalTime;

public abstract class Position {

    public static final int LONG = 0;
    public static final int SHORT = 1;

    // Variables
    private int id;
    private int quantity;
    private int type;
    private double stoplossPrice;
    private double profitPrice;
    private int stoplossId;
    private int profitId;
    private double startPrice;
    private double closePrice;
    private boolean live;
    private BASE_CLIENT_OBJECT client;
    private Option option;
    private LocalTime startTime;
    private LocalTime closeTime;

    // Constructor
    public Position( BASE_CLIENT_OBJECT client, int quantity, int id ) {

        setClient( client );
        setStartTime( client.getDateTimeHandler( ).getTime( ) );

        this.id = id;
        this.quantity = quantity;
        this.live = true;
        this.startPrice = client.getIndex( );
        this.quantity = Math.abs( this.quantity );
        this.type = quantity > 0 ? LONG : SHORT;

    }

    public void close() {
        this.live = false;
        setCloseTime( client.getDateTimeHandler( ).getTime( ) );
        this.quantity = 0;
        this.closePrice = client.getIndex( );
    }

    // Abstracts
    public abstract double getTheoPnl();

    public abstract double getPnl();

    // ---------- Getters and Setters ---------- //
    public String toStringVertical() {
        String string = toString( );
        String[] array = string.split( ", " );
        String returnString = "";
        for ( int i = 0; i < array.length; i++ ) {
            returnString += array[ i ] + "\n";
        }
        return returnString;
    }

    public double floor( double d ) {
        return Math.floor( d * 10 ) / 10;
    }

    public double oposite( double d ) {
        return d * -1;
    }

    @Override
    public String toString() {
        return "Position{" +
                "Pnl=" + getPnl() +
                "type=" + type +
                ", live=" + live +
                ", startTime=" + startTime +
                ", closeTime=" + closeTime +
                '}';
    }

    public String toStringVerticalYogi() {
        String string = toString( );
        String[] array = string.split( ", " );
        String returnString = "";
        for ( int i = 0; i < array.length; i++ ) {
            returnString += array[ i ] + "\n";
        }
        return returnString;
    }

    public Option getOption() {
        return option;
    }

    public void setOption( Option option ) {
        this.option = option;
    }

    public int getType() {
        return type;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime( LocalTime startTime ) {
        this.startTime = startTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime( LocalTime closeTime ) {
        this.closeTime = closeTime;
    }

    public BASE_CLIENT_OBJECT getClient() {
        return client;
    }

    public void setClient( BASE_CLIENT_OBJECT client ) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive( boolean live ) {
        this.live = live;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity( int quantity ) {
        this.quantity = quantity;
    }

    public double getStoplossPrice() {
        return stoplossPrice;
    }

    public void setStoplossPrice( double stoplossPrice ) {
        this.stoplossPrice = stoplossPrice;
    }

    public double getProfitPrice() {
        return profitPrice;
    }

    public void setProfitPrice( double profitPrice ) {
        this.profitPrice = profitPrice;
    }

    public int getStoplossId() {
        return stoplossId;
    }

    public void setStoplossId( int stoplossId ) {
        this.stoplossId = stoplossId;
    }

    public int getProfitId() {
        return profitId;
    }

    public void setProfitId( int profitId ) {
        this.profitId = profitId;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice( double startPrice ) {
        this.startPrice = startPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice( double closePrice ) {
        this.closePrice = closePrice;
    }

}
