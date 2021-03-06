package charts.myChart;

import lists.MyChartList;
import lists.MyChartPoint;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;

import java.awt.*;
import java.util.Date;

public abstract class MyTimeSeries extends XYSeries implements ITimeSeries {

    public static final int TIME = 0;
    public static final int VALUE = 1;

    int x = 0;

    private Color color;
    private float stokeSize;
    private MyChartList myChartList;
    MyProps props;

    public MyTimeSeries( Comparable name, Color color, float strokeSize, MyProps props, MyChartList myChartList ) {
        super( name );
        this.color = color;
        this.stokeSize = strokeSize;
        this.props = props;
        this.myChartList = myChartList;
    }

    public double add() {
        double data;
        // live data
        if ( props.getBool( ChartPropsEnum.IS_LIVE ) ) {
            data = getData();
            add( x, data );
        } else {
            MyChartPoint point = myChartList.getLast();
            data = point.getY();
            add(x , data );
        }
        x++;
        return data;
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }

    public float getStokeSize() {
        return stokeSize;
    }

    public void setStokeSize( float stokeSize ) {
        this.stokeSize = stokeSize;
    }

    public MyChartList getMyChartList() {
        return myChartList;
    }

    public void setMyChartList( MyChartList myChartList ) {
        this.myChartList = myChartList;
    }
}

interface ITimeSeries {
    double getData();
}