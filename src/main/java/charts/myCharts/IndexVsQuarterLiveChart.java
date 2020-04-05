package charts.myCharts;

import charts.myChart.*;
import locals.Themes;
import serverObjects.BASE_CLIENT_OBJECT;
import serverObjects.indexObjects.TA35;
import shlomi.MainThread;

import java.awt.*;

public class IndexVsQuarterLiveChart extends MyChartCreator {

    @Override
    public void createChart() {

        // Props
        props = new MyProps();
        props.setProp( ChartPropsEnum.SECONDS, 150 );
        props.setProp( ChartPropsEnum.IS_INCLUDE_TICKER, false );
        props.setProp( ChartPropsEnum.MARGIN, .17 );
        props.setProp( ChartPropsEnum.RANGE_MARGIN, 0.0 );
        props.setProp( ChartPropsEnum.IS_GRID_VISIBLE, false );
        props.setProp( ChartPropsEnum.IS_LOAD_DB, false );
        props.setProp( ChartPropsEnum.IS_LIVE, true );
        props.setProp( ChartPropsEnum.SLEEP, 200 );
        props.setProp( ChartPropsEnum.CHART_MAX_HEIGHT_IN_DOTS, (double)INFINITE );
        props.setProp( ChartPropsEnum.SECONDS_ON_MESS, 10 );

        // ----- Chart 1 ----- //
        // Index
        MyTimeSeries index = new MyTimeSeries( "Index", Color.BLACK, 2.25f, props, TA35.getInstance().getIndexList() ) {
            @Override
            public double getData() {
                return TA35.getInstance().getIndex();
            }
        };

        // Index
        MyTimeSeries bid = new MyTimeSeries( "Bid", Themes.BLUE, 2.25f, props, TA35.getInstance().getIndexBidList() ) {
            @Override
            public double getData() {
                return TA35.getInstance().getIndexBid();
            }
        };

        // Index
        MyTimeSeries ask = new MyTimeSeries( "Ask", Themes.RED, 2.25f, props, TA35.getInstance().getIndexAskList() ) {
            @Override
            public double getData() {
                return TA35.getInstance().getIndexAsk();
            }
        };


        MyTimeSeries[] series = {index, bid, ask};

        // Chart
        MyChart chart = new MyChart(  series, props );

        // ----- Charts ----- //
        MyChart[] charts = { chart };

        // ----- Container ----- //
        MyChartContainer chartContainer = new MyChartContainer( charts, getClass().getName() );
        chartContainer.create();


    }

}
