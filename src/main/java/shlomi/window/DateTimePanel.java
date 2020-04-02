package shlomi.window;

public class DateTimePanel extends MyGuiComps.MyPanel {

    MyGuiComps.MyLabel timeLbl;
    MyGuiComps.MyLabel dateLbl;

    public DateTimePanel() {
        initialize( );
    }

    private void initialize() {

        // This
        setLayout( null );
        setBounds( 0, 0, 550, 25 );

        // Date
        dateLbl = new MyGuiComps.MyLabel( "Date" );
        dateLbl.setBounds( 5, 5, 100, dateLbl.getHeight( ) );
        add( dateLbl );

        // Time
        timeLbl = new MyGuiComps.MyLabel( "Time" );
        timeLbl.setBounds( 110, 5, 100, timeLbl.getHeight( ) );
        add( timeLbl );

    }
}
