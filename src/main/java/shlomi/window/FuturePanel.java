package shlomi.window;

import locals.L;
import locals.Themes;
import options.Options;
import serverObjects.BASE_CLIENT_OBJECT;

import javax.swing.*;
import java.awt.*;

public class FuturePanel extends MyGuiComps.MyPanel implements IMyPanels {

    public MyGuiComps.MyTextField conRacesField;
    public MyGuiComps.MyTextField indRacesField;

    // Ticker
    MyGuiComps.MyPanel ticker;
    MyGuiComps.MyTextField openField;
    MyGuiComps.MyTextField openPresentField;
    MyGuiComps.MyTextField indexField;
    MyGuiComps.MyTextField indexPresentField;
    MyGuiComps.MyTextField lowField;
    MyGuiComps.MyTextField lowPresentField;
    MyGuiComps.MyTextField highField;
    MyGuiComps.MyTextField highPresentField;
    MyGuiComps.MyTextField futureField;
    MyGuiComps.MyTextField opField;
    MyGuiComps.MyTextField opAvgField;
    MyGuiComps.MyTextField opAvgEqualeMoveField;
    // Exp
    MyGuiComps.MyPanel exp;

    // Quarter
    MyGuiComps.MyTextField opAvgQuarterField;
    MyGuiComps.MyTextField opQuarterField;
    MyGuiComps.MyTextField contractQuarterField;

    // Races and roll
    MyGuiComps.MyPanel racesAndRollPanel;
    MyGuiComps.MyLabel conRacesLbl;
    MyGuiComps.MyLabel indRacesLbl;
    MyGuiComps.MyLabel rollLbl;
    MyGuiComps.MyTextField rollField;

    // Exp
    MyGuiComps.MyPanel expPanel;
    MyGuiComps.MyLabel expMoveLbl;
    MyGuiComps.MyLabel expRacesLbl;
    MyGuiComps.MyLabel expOpAvgLbl;

    MyGuiComps.MyTextField expMoveField;
    MyGuiComps.MyTextField expRacesField;
    MyGuiComps.MyTextField expOpAvgField;

    int height = 200;
    Color backGround = Themes.GREY_LIGHT;

    BASE_CLIENT_OBJECT client;

    Options optionsMonth;
    Options mainOptions;

    public FuturePanel() {
        init();
    }

    public void setClient( BASE_CLIENT_OBJECT client ) {
        this.client = client;
        optionsMonth = client.getOptionsHandler( ).getOptionsMonth( );
        mainOptions = client.getOptionsHandler( ).getMainOptions( );
    }

    private void init() {

        setLayout( null );
        setBounds( 0, 0, 0, height );

        // Ticker section
        ticker = new MyGuiComps.MyPanel( );
        ticker.setLayout( null );
        ticker.setBounds( 0, 0, 311, height );
        ticker.setBackground( backGround );

        openField = new MyGuiComps.MyTextField( 20 );
        openField.setXY( 5, 6 );
        ticker.add( openField );

        openPresentField = new MyGuiComps.MyTextField( 20 );
        openPresentField.setForeground( Color.WHITE );
        openPresentField.setFont( openPresentField.getFont( ).deriveFont( Font.BOLD ) );
        openPresentField.setXY( 5, 35 );
        ticker.add( openPresentField );

        indexField = new MyGuiComps.MyTextField( 20 );
        indexField.setXY( 80, 6 );
        ticker.add( indexField );

        indexPresentField = new MyGuiComps.MyTextField( 20 );
        indexPresentField.setForeground( Color.WHITE );
        indexPresentField.setFont( indexPresentField.getFont( ).deriveFont( Font.BOLD ) );
        indexPresentField.setXY( 80, 35 );
        ticker.add( indexPresentField );

        lowField = new MyGuiComps.MyTextField( 20 );
        lowField.setXY( 155, 6 );
        ticker.add( lowField );

        lowPresentField = new MyGuiComps.MyTextField( 20 );
        lowPresentField.setForeground( Color.WHITE );
        lowPresentField.setFont( lowPresentField.getFont( ).deriveFont( Font.BOLD ) );
        lowPresentField.setXY( 155, 35 );
        ticker.add( lowPresentField );

        highField = new MyGuiComps.MyTextField( 20 );
        highField.setXY( 230, 6 );
        ticker.add( highField );

        highPresentField = new MyGuiComps.MyTextField( 20 );
        highPresentField.setForeground( Color.WHITE );
        highPresentField.setFont( highPresentField.getFont( ).deriveFont( Font.BOLD ) );
        highPresentField.setXY( 230, 35 );
        ticker.add( highPresentField );

        futureField = new MyGuiComps.MyTextField( 20 );
        futureField.setXY( 5, 64 );
        ticker.add( futureField );

        opField = new MyGuiComps.MyTextField( 20 );
        opField.setForeground( Color.WHITE );
        opField.setFont( opField.getFont( ).deriveFont( Font.BOLD ) );
        opField.setXY( 80, 64 );
        ticker.add( opField );

        opAvgEqualeMoveField = new MyGuiComps.MyTextField( 20 );
        opAvgEqualeMoveField.setXY( 230, 64 );
        ticker.add( opAvgEqualeMoveField );

        opAvgField = new MyGuiComps.MyTextField( 20 );
        opAvgField.setXY( 155, 64 );
        ticker.add( opAvgField );

        // Quarter
        opAvgQuarterField = new MyGuiComps.MyTextField( 20 );
        opAvgQuarterField.setXY( 155, 93 );
        ticker.add( opAvgQuarterField );

        contractQuarterField = new MyGuiComps.MyTextField( 20 );
        contractQuarterField.setXY( 5, 93 );
        ticker.add( contractQuarterField );

        opQuarterField = new MyGuiComps.MyTextField( 20 );
        opQuarterField.setForeground( Color.WHITE );
        opQuarterField.setFont( opQuarterField.getFont( ).deriveFont( Font.BOLD ) );
        opQuarterField.setXY( 80, 93 );
        opQuarterField.setForeground( Color.WHITE );
        ticker.add( opQuarterField );

        add( ticker );

        // ---------- Races and roll ---------- //
        // Panel
        racesAndRollPanel = new MyGuiComps.MyPanel( );
        racesAndRollPanel.setLayout( null );
        racesAndRollPanel.setBackground( backGround );
        racesAndRollPanel.setBounds( 312, 0, 111, height );
        add( racesAndRollPanel );

        // Con lbl
        conRacesLbl = new MyGuiComps.MyLabel( "Cont" );
        conRacesLbl.setHorizontalAlignment( JLabel.CENTER );
        conRacesLbl.setBounds( 5, 5, 50, 25 );
        conRacesLbl.setForeground( Themes.BLUE );

        racesAndRollPanel.add( conRacesLbl );

        // Con field
        conRacesField = new MyGuiComps.MyTextField( 20 );
        conRacesField.setBounds( 55, 7, 50, 25 );
        racesAndRollPanel.add( conRacesField );

        // Ind lbl
        indRacesLbl = new MyGuiComps.MyLabel( "Ind" );
        indRacesLbl.setHorizontalAlignment( JLabel.CENTER );
        indRacesLbl.setBounds( 5, 35, 50, 25 );
        indRacesLbl.setForeground( Themes.BLUE );

        racesAndRollPanel.add( indRacesLbl );

        // Ind field
        indRacesField = new MyGuiComps.MyTextField( 20 );
        indRacesField.setBounds( 55, 37, 50, 25 );
        racesAndRollPanel.add( indRacesField );

        rollLbl = new MyGuiComps.MyLabel( "Roll" );
        rollLbl.setHorizontalAlignment( JLabel.CENTER );
        rollLbl.setBounds( 5, 65, 50, 25 );
        rollLbl.setForeground( Themes.BLUE );

        racesAndRollPanel.add( rollLbl );

        // Roll field
        rollField = new MyGuiComps.MyTextField( 20 );
        rollField.setBounds( 55, 67, 50, 25 );
        racesAndRollPanel.add( rollField );

        // ---------- Exp ---------- //
        expPanel = new MyGuiComps.MyPanel( );
        expPanel.setLayout( null );
        expPanel.setBounds( 424, 0, 111, height );
        add( expPanel );

        // Move
        expMoveLbl = new MyGuiComps.MyLabel( "Move" );
        expMoveLbl.setBounds( 5, 7, 50, 25 );
        expMoveLbl.setFont( Themes.VEDANA_12 );
        expPanel.add( expMoveLbl );

        expMoveField = new MyGuiComps.MyTextField( 20 );
        expMoveField.setBounds( 55, 7, 50, 25 );
        expPanel.add( expMoveField );

        // Races
        expRacesLbl = new MyGuiComps.MyLabel( "Races" );
        expRacesLbl.setFont( Themes.VEDANA_12 );
        expRacesLbl.setBounds( 5, 37, 50, 25 );
        expPanel.add( expRacesLbl );

        expRacesField = new MyGuiComps.MyTextField( 20 );
        expRacesField.setBounds( 55, 37, 50, 25 );
        expPanel.add( expRacesField );

        // OpAvg
        expOpAvgLbl = new MyGuiComps.MyLabel( "OP/AVG" );
        expOpAvgLbl.setFont( Themes.VEDANA_12 );
        expOpAvgLbl.setBounds( 5, 67, 50, 25 );
        expPanel.add( expOpAvgLbl );

        expOpAvgField = new MyGuiComps.MyTextField( 20 );
        expOpAvgField.setBounds( 55, 67, 50, 25 );
        expPanel.add( expOpAvgField );

    }

    @Override
    public void setText() throws InterruptedException {

        // ---------- Ticker ---------- //
        openField.setText( L.format100( client.getOpen( ) ) );
        highField.setText( L.format100( client.getHigh( ) ) );
        lowField.setText( L.format100( client.getLow( ) ) );
        indexField.setText( L.format100( client.getIndex( ) ) );
        futureField.setText( L.format100( mainOptions.getContract( ) ) );

        // Ticker present
        openPresentField.colorBack( toPresent( client.getOpen( ) ), L.format100( ), "%" );
        highPresentField.colorBack( toPresent( client.getHigh( ) ), L.format100( ), "%" );
        lowPresentField.colorBack( toPresent( client.getLow( ) ), L.format100( ), "%" );
        indexPresentField.colorBack( toPresent( client.getIndex( ) ), L.format100( ), "%" );

        // OP
        opAvgField.colorForge( mainOptions.getOpAvg( ), L.format100( ) );
        opField.colorBack( mainOptions.getOp( ), L.format100( ) );

        // Races and roll
        // Races
        conRacesField.colorForge( client.getFutSum( ) );
        indRacesField.colorForge( client.getIndexSum( ) );

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    private double toPresent( double d ) {
        double base = client.getBase( );
        return ( ( d - base ) / base ) * 100;
    }
}
