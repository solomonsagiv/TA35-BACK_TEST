package charts.myCharts;

import charts.myChart.*;
import locals.Themes;
import serverObjects.BASE_CLIENT_OBJECT;
import serverObjects.indexObjects.TA35;
import shlomi.MainThread;

import java.awt.*;

public class IndexVsConBidAskCounterChart extends MyChartCreator {

    MyChart indexChart;
    MyChart indexBidAskCounterChart;

    @Override
    public void createChart() {

        MyTimeSeries[] series;

        // Props
        props = new MyProps();
        props.setProp( ChartPropsEnum.SECONDS, INFINITE );
        props.setProp( ChartPropsEnum.IS_INCLUDE_TICKER, true );
        props.setProp( ChartPropsEnum.MARGIN, .17 );
        props.setProp( ChartPropsEnum.RANGE_MARGIN, 0.0 );
        props.setProp( ChartPropsEnum.IS_GRID_VISIBLE, true );
        props.setProp( ChartPropsEnum.IS_LOAD_DB, true );
        props.setProp( ChartPropsEnum.IS_LIVE, false );
        props.setProp( ChartPropsEnum.SLEEP, 1000 );
        props.setProp( ChartPropsEnum.CHART_MAX_HEIGHT_IN_DOTS, (double) INFINITE);
        props.setProp( ChartPropsEnum.SECONDS_ON_MESS, 10 );

        // --------- Chart 1 ---------- //
        // Index
        MyTimeSeries index = new MyTimeSeries( "Index", Color.BLACK, 1.5f, props, TA35.getInstance().getIndexList() ) {
            @Override
            public double getData() {
                return TA35.getInstance().getIndex();
            }
        };

        series = new MyTimeSeries[1];
        series[0] = index;

        // Chart
        indexChart = new MyChart( series, props );

        // ---------- Chart 2 ---------- //
        // Index
        MyTimeSeries indexBidAskCounter = new MyTimeSeries( "Counter", Themes.ORANGE, 1.5f, props, TA35.getInstance().getOptionsHandler().getMainOptions().getConBidAskCounterList() ) {
            @Override
            public double getData() {
                return TA35.getInstance().getOptionsHandler().getMainOptions().getContractBidAskCounter();
            }
        };

        series = new MyTimeSeries[1];
        series[0] = indexBidAskCounter;

        indexBidAskCounterChart = new MyChart( series, props );

        // -------------------- Chart -------------------- //

        // ----- Charts ----- //
        MyChart[] charts = { indexChart, indexBidAskCounterChart };

        // ----- Container ----- //
        MyChartContainer chartContainer = new MyChartContainer( charts, getClass().getName() );
        chartContainer.create();

    }

    public void doWork() {
        indexChart.doWork();
        indexBidAskCounterChart.doWork();
    }


}
