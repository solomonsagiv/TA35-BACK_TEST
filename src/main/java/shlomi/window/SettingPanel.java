package shlomi.window;

import charts.myCharts.IndexVsConBidAskCounterChart;
import dataBase.mySql.MySql;
import locals.Themes;
import serverObjects.indexObjects.SpxCLIENTObject;
import serverObjects.indexObjects.TA35;
import shlomi.MainThread;
import shlomi.algorithm.AlgoTwo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class SettingPanel extends MyGuiComps.MyPanel {
    MainThread mainThread;
    JComboBox algoBox;
    public static MyGuiComps.MyButton startBtn;
    MyGuiComps.MyButton stopBtn;
    MyGuiComps.MyButton backBtn;
    MyGuiComps.MyButton continueBtn;
    MyGuiComps.MyButton slowerBtn;
    MyGuiComps.MyButton finishBtn;
    MyGuiComps.MyButton chartBtn;
    public static MyGuiComps.MyTextField targetDateField;

    Object[] items = { "none", "1", "2" };

    public SettingPanel() {
        initialize( );
        initListeners( );
    }

    private void initListeners() {
        // Start
        startBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                try {

                    String dateString = targetDateField.getText( );
                    String query = String.format( "select date, time, fut, ind, futUp, futDown, indUp, indDown, basketUp, basketDown, futBidAskCounter, opAvg, base from ta35.ta35data " +
                            " where date = '%s';", dateString );

                    // Load data from MYSQL
                    ResultSet rs = MySql.select( query );

                    try {
                        mainThread.getHandler( ).close( );
                    } catch ( Exception e ) {
                    }

                    TA35.getNewInstance();
                    System.out.println(" Client: " + TA35.getInstance().hashCode() );
                    ShlomiWindow.futurePanel.setClient( TA35.getInstance() );
                    mainThread = new MainThread( rs, new AlgoTwo( TA35.getInstance(), 0 ) );
                    mainThread.getHandler( ).start( );

                    startBtn.setEnabled( false );
                } catch ( Exception e ) {
                    e.printStackTrace( );
                    // TODO
                }
            }
        } );

        // Forward
        continueBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                mainThread.forward( );
            }
        } );

        // Forward
        backBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                mainThread.backward( );
            }
        } );

        // Stop
        stopBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                mainThread.stop( );
                startBtn.setEnabled( true );
            }
        } );

        // Slower
        slowerBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                mainThread.slower( );
            }
        } );

        // Finish
        finishBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                mainThread.finish();
            }
        } );

        // Chart
        chartBtn.addActionListener( new ActionListener( ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                IndexVsConBidAskCounterChart chart = new IndexVsConBidAskCounterChart( );
                chart.createChart();

                MainThread.chart = chart;

            }
        } );
    }

    private void initialize() {

        // This
        setLayout( null );

        // Start
        startBtn = new MyGuiComps.MyButton( "Start" );
        startBtn.setForeground( Themes.GREEN );
        startBtn.setXY( 5, 5 );
        add( startBtn );

        // Stop
        stopBtn = new MyGuiComps.MyButton( "Stop" );
        stopBtn.setForeground( Themes.RED );
        stopBtn.setXY( 80, 5 );
        add( stopBtn );

        // Back
        backBtn = new MyGuiComps.MyButton( "Back" );
        backBtn.setXY( 5, 35 );
        add( backBtn );

        // Continue
        continueBtn = new MyGuiComps.MyButton( "Continue" );
        continueBtn.setXY( 80, 35 );
        add( continueBtn );

        // Slower
        slowerBtn = new MyGuiComps.MyButton( "Slower" );
        slowerBtn.setXY( 5, 65 );
        add( slowerBtn );

        // Finish
        finishBtn = new MyGuiComps.MyButton( "Finish" );
        finishBtn.setXY( 80, 65 );
        add( finishBtn );

        // Algo comboBox
        algoBox = new JComboBox( items );
        algoBox.setFont( Themes.VEDANA_12 );
        algoBox.setBackground( Themes.GREY_LIGHT );
        algoBox.setAlignmentX( JCheckBox.CENTER );
        algoBox.setBounds( 155, 5, 70, 20 );
        add( algoBox );

        // Target date
        targetDateField = new MyGuiComps.MyTextField( 20 );
        targetDateField.setText( "2020-02-19" );
        targetDateField.setWidth( 90 );
        targetDateField.setXY( 155, 35 );
        add( targetDateField );

        // Chart
        chartBtn = new MyGuiComps.MyButton( "Chart" );
        chartBtn.setXY( targetDateField.getX(), targetDateField.getY() + targetDateField.getHeight() + 5 );
        add( chartBtn );

    }
}
