package shlomi.window;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ShlomiWindow extends MyGuiComps.MyFrame {

    public static void main( String[] args ) {
        new ShlomiWindow( "Shlomi" );
    }

    // Variables
    public static FuturePanel futurePanel;

    // Setting
    public static SettingPanel settingPanel;

    // Status
    StatusPanel statusPanel;

    // Date and Time
    public static DateTimePanel dateTimePanel;

    // Constructor
    public ShlomiWindow( String title ) throws HeadlessException {
        super( title );
        initialize( );
    }

    @Override
    public void onClose() {
        addWindowListener( new WindowAdapter( ) {
            @Override
            public void windowClosing( WindowEvent e ) {
                System.exit( 0 );
            }
        } );
    }

    private void initialize() {

        // This
        setBounds( 200, 200, 852, 500 );

        // Future panel
        futurePanel = new FuturePanel( );
        futurePanel.setBounds( 0, 0, 550, 130 );
        getContentPane( ).add( futurePanel );

        // Setting panel
        settingPanel = new SettingPanel( );
        settingPanel.setBounds( 551, 0, 300, getHeight( ) );
        getContentPane( ).add( settingPanel );

        // Date and Time
        dateTimePanel = new DateTimePanel( );
        dateTimePanel.setBounds( 0, futurePanel.getHeight( ) + 1, futurePanel.getWidth( ), 25 );
        getContentPane( ).add( dateTimePanel );

        // Status panel
        statusPanel = new StatusPanel( );
        statusPanel.setBounds( 0, dateTimePanel.getY( ) + dateTimePanel.getHeight( ) + 1, futurePanel.getWidth( ), 300 );
        getContentPane( ).add( statusPanel );

    }

    public static void updateWindowText( DateTimeHandler dateTimeHandler ) {
        dateTimePanel.timeLbl.setText( dateTimeHandler.getTime( ).toString( ) );
        dateTimePanel.dateLbl.setText( dateTimeHandler.getDate( ).toString( ) );
    }


}
