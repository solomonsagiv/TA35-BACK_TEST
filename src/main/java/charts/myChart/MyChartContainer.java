package charts.myChart;

import charts.MyChartPanel;
import serverObjects.BASE_CLIENT_OBJECT;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class MyChartContainer extends JFrame {

    private static final long serialVersionUID = 1L;

    // Index series array
    MyChart[] charts;

    BASE_CLIENT_OBJECT client;
    String name;

    public MyChartContainer( BASE_CLIENT_OBJECT client, MyChart[] charts, String name ) {
        this.charts = charts;
        this.client = client;
        this.name = name;
        init( );
    }

    @Override
    public String getName() {
        return name;
    }

    private void init() {

        // On Close
        addWindowListener( new java.awt.event.WindowAdapter( ) {
            public void windowClosing( WindowEvent e ) {
                onClose( e );
            }
        } );

        // Layout
        setLayout( new GridLayout( charts.length, 0 ) );

        // Append charts
        appendCharts( );

    }

    public void create() {
        pack();
        setVisible( true );
    }

    private void appendCharts() {
        for ( MyChart myChart : charts ) {
            MyChartPanel chartPanel = new MyChartPanel( myChart.chart, myChart.props.getBool( ChartPropsEnum.IS_INCLUDE_TICKER ) );
            myChart.chartPanel = chartPanel;
            add( chartPanel );
        }
    }


    public void onClose( WindowEvent e ) {

        for ( MyChart myChart : charts ) {
            myChart.getUpdater().getHandler().close();
        }

        dispose( );
    }

}
