package com.example.borja.finalproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;

import java.util.List;


public class Detail_Service extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */
    /*
    private List<Overlay> mapOverlays;

    private Projection projection;

    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail__service);

        //linearLayout = (LinearLayout) findViewById(R.id.zoomview);
        mapView = (MapView) findViewById(R.id.mapView2);
        //mapView.setBuiltInZoomControls(true);

        mapOverlays = mapView.getOverlays();
        projection = mapView.getProjection();
        mapOverlays.add(new MyOverlay());

    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    class MyOverlay extends Overlay {

        public MyOverlay() {

        }

        public void draw(Canvas canvas, MapView mapv, boolean shadow) {
            super.draw(canvas, mapv, shadow);

            Paint mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(2);

            GeoPoint gP1 = new GeoPoint(19240000, -99120000);
            GeoPoint gP2 = new GeoPoint(37423157, -122085008);

            Point p1 = new Point();
            Point p2 = new Point();
            Path path = new Path();

            Projection projection = mapv.getProjection();
            projection.toPixels(gP1, p1);
            projection.toPixels(gP2, p2);

            path.moveTo(p2.x, p2.y);
            path.lineTo(p1.x, p1.y);

            canvas.drawPath(path, mPaint);
        }
    }

    private class Overlay {
    }
    */
}