package org.ktln2.android.callstat;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import android.graphics.Color;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewStyle;


public class GraphActivity extends SherlockFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_stat_surface);

        createGraph();
    }

    private void createGraph() {
        int delta = 60;
        int[] bins = MainActivity.map.getBinsForDurations(60);
        int nBins = bins.length;

        GraphViewData[] data = new GraphViewData[nBins];

        for (int cycle = 0 ; cycle < nBins ; cycle++) {
            data[cycle] = new GraphViewData(cycle, bins[cycle]);
        }

        GraphViewSeries durationSeries = new GraphViewSeries(data);

        GraphView graphView = new BarGraphView(this, "# of calls of given minutes");
        graphView.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLACK, Color.DKGRAY));
        graphView.setViewPort(0, 20);
        //graphView.setScrollable(true);
        graphView.addSeries(durationSeries); // data
        ((LinearLayout)findViewById(R.id.graph_container)).addView(graphView);
    }
}
