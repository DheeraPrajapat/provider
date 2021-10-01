package com.marius.valeyou_admin.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.marius.valeyou_admin.R;

public class WeekDrawActivity extends AppCompatActivity {

    private int req_width, req_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_draw);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        req_width = width - 200;
        req_height = (height - 100) / 4;
        LinearLayout relativeLayout = findViewById(R.id.rl_main);
        for (int j = 0; j < 12; j++) {
            ImageView imageView = new ImageView(this);
            Bitmap returnedBitmap = Bitmap.createBitmap(req_width, req_height, Bitmap.Config.ARGB_8888);
            imageView.setImageBitmap(returnedBitmap);
            Canvas canvas = new Canvas(returnedBitmap);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.white));
            paint.setStrokeWidth(10);

            Canvas canvas2 = new Canvas(returnedBitmap);
            Rect rect = new Rect();
            Paint paint2 = new Paint();
            paint2.setColor(getResources().getColor(R.color.black));
            paint2.setTextSize(35);

            Canvas canvas3 = new Canvas(returnedBitmap);
            Paint paint3 = new Paint();
            paint3.setColor(getResources().getColor(R.color.grey_color));
            paint3.setStrokeWidth(10);

            if (j % 2 == 0) {
                int startx = req_width;
                int starty = 0;
                int endx = 0;
                int endy = req_height;
                canvas.drawLine(startx, starty, endx, endy, paint);

                String text = getStringValue(j);
                paint2.getTextBounds(text, 0, text.length(), rect);
                canvas2.drawRect(rect, paint);
                canvas2.rotate(-35, 150 + rect.exactCenterX(), 150 + rect.exactCenterY());
                canvas2.drawText(text, 150, 250, paint2);
                for (int i = 0; i < 4; i++) {
                    if (j == 6 && i == 2) {
                        canvas3.drawBitmap(getMarkerBitmapFromView(), (i + 1) * req_width / 5 - 40, req_height - (i + 1) * (req_height / 5) - 40, null);
                    } else {
                        canvas3.drawCircle((i + 1) * req_width / 5, req_height - (i + 1) * (req_height / 5), 30, paint3);
                    }
                }
            } else {
                int startx = 0;
                int starty = 0;
                int endx = req_width;
                int endy = req_height;
                canvas.drawLine(startx, starty, endx, endy, paint);

                String text = getStringValue(j);
                paint2.getTextBounds(text, 0, text.length(), rect);
                canvas2.drawRect(rect, paint);
                canvas2.rotate(35, 250 + rect.exactCenterX(), 80 + rect.exactCenterY());
                canvas2.drawText(text, 270, 250, paint2);

                for (int i = 0; i < 4; i++) {
                    if (j == 6 && i == 2) {
                        canvas3.drawBitmap(getMarkerBitmapFromView(), (i + 1) * req_width / 5 - 40, req_height - (i + 1) * (req_height / 5) - 40, null);
                    } else {
                        canvas3.drawCircle((i + 1) * req_width / 5, (i + 1) * (req_height / 5), 30, paint3);
                    }
                }

            }
            relativeLayout.addView(imageView);
        }
    }

    private String getStringValue(int count) {
        String month = "";
        switch (count) {
            case 0:
                month = "DECEMBER";
                break;
            case 1:
                month = "NOVEMBER";
                break;
            case 2:
                month = "OCTOBER";
                break;
            case 3:
                month = "SEPTEMBER";
                break;
            case 4:
                month = "AUGUST";
                break;
            case 5:
                month = "JULY";
                break;
            case 6:
                month = "JUNE";
                break;
            case 7:
                month = "MAY";
                break;
            case 8:
                month = "APRIL";
                break;
            case 9:
                month = "MARCH";
                break;
            case 10:
                month = "FEBRUARY";
                break;
            case 11:
                month = "JANUARY";
                break;
        }
        return month;
    }

    private Bitmap getMarkerBitmapFromView() {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_circle, null);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

}
