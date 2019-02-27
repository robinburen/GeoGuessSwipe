package com.example.geoguessswipe;

import android.content.ClipData;
import android.gesture.GestureOverlayView;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemTouchListener {

    List<GeoObject> mGeoObjects = new ArrayList<>();
    RecyclerView mGeoRecyclerView;
    GeoObjectAdapter mGeoAdapter;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES.length; i++) {
            mGeoObjects.add(new GeoObject(GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES[i],
                    GeoObject.PRE_DEFINED_GEO_OBJECT_IMAGE_IDS[i], GeoObject.PRE_DEFINED_GEO_OBJECT_EUROPE[i]));
        }

        mGeoRecyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        mGeoRecyclerView.setLayoutManager(mLayoutManager);

        mGeoRecyclerView.setHasFixedSize(true);

        GeoObjectAdapter mAdapter = new GeoObjectAdapter(this, mGeoObjects);

        mGeoRecyclerView.setAdapter(mAdapter);

        mGeoRecyclerView.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        /*

        Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
                An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
                and uses callbacks to signal when a user is performing these actions.
                */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    // The first integer parameter refers to the dragging directions. We ignore these here.
                    // The second integer parameter refers to the swiping directions.

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
//                        mGeoObjects.remove(position);
//                        mGeoAdapter.notifyItemRemoved(position);
                        checkEuropeCorrect(position, swipeDir);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mGeoRecyclerView);
    }

    public void checkEuropeCorrect(int index, int swipeDir) {
        if (ItemTouchHelper.LEFT == swipeDir) {
            String answer = getEurope(index);
            if (answer.equals("no")) {
                makeCorrectToast();
            } else {
                makeFalseToast();
            }
        } else if (ItemTouchHelper.RIGHT == swipeDir){
            String answer = getEurope(index);
            if (answer.equals("yes")) {
                makeCorrectToast();
            } else {
                makeFalseToast();
            }
        }
    }

    public void makeCorrectToast() {
        Toast.makeText(this, "Correct answer", Toast.LENGTH_SHORT).show();
    }

    public void makeFalseToast() {
        Toast.makeText(this, "False answer", Toast.LENGTH_SHORT).show();
    }

    public String getEurope(int index) {
        String getAnswer = mGeoObjects.get(index).getmGeoEurope();
        return getAnswer;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        int mAdapterPosition = rv.getChildAdapterPosition(child);

        if (child != null && mGestureDetector.onTouchEvent(e)) {
            Toast.makeText(this, mGeoObjects.get(mAdapterPosition).getmGeoName(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
