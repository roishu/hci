package com.hci.roi.hciproject;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cc.roi.aircc.R;

import java.util.ArrayList;

/**
 * Created by Roi on 14/06/2017.
 */
public class MyAdapter extends RecyclerView
        .Adapter<MyAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;
    private ViewGroup myParentView;
    private HSICardView mHSIView;
    private ADIView mADIView;
    private RadarView mRadarView;
    public ValueAnimator va;// = ValueAnimator.ofFloat(0, 120);
    public int mDuration = 10000; //in millis
    private PropertyValuesHolder animatorHolder;
    private int chosenViewIndex;
    //ADI-MEMBERS
    public ValueAnimator va2 = ValueAnimator.ofInt(0, 100);
    public int mDuration2 = 5000; //in millis


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyAdapter(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        Log.e("MyAdaperTAG" , "ViewType="+viewType +"");
        myParentView = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card_view, parent, false);

        switch(viewType){
            case 0:
               view = (LayoutInflater.from(myParentView.getContext())
                        .inflate(R.layout.card_hsi, myParentView, false));
                mHSIView = (HSICardView) view.findViewById(R.id.radarView);
                mHSIView.startAnimation();
                animHSI();
                break;
            case 1:
                view = (LayoutInflater.from(myParentView.getContext())
                        .inflate(R.layout.card_adi, myParentView, false));
                mADIView = (ADIView) view.findViewById(R.id.adiView);
                mADIView.setShowCircles(true);
                mADIView.startAnimation();
                animADI();
                break;
            case 2:
                view = (LayoutInflater.from(myParentView.getContext())
                        .inflate(R.layout.card_radar, myParentView, false));
                mRadarView = (RadarView) view.findViewById(R.id.radarView);
                mRadarView.setShowCircles(true);
                mRadarView.startAnimation();
                break;
            case 3:
                view = (LayoutInflater.from(myParentView.getContext())
                    .inflate(R.layout.my_card_view, myParentView, false));
                break;
        }

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {


       // holder.label.setText(mDataset.get(position).getmText1());
        //holder.dateTime.setText(mDataset.get(position).getmText2());
        //holder.
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    private void animADI() {

        va2.setRepeatCount(ValueAnimator.INFINITE);
        va2.setRepeatMode(ValueAnimator.RESTART);
        va2.setDuration(mDuration2);
        va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mADIView.setPivot((int)animation.getAnimatedValue());
            }
        });

        va2.start();

    }

    private void animHSI() {
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("1", 0f, 150f);
        final PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("2", 150f, 45f);
        final PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("3", 45f, 360f);

        setAnimationProperties(pvh1);
        va.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAnimationProperties(pvh2);
                va.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setAnimationProperties(pvh3);
                        va.start();
                    }
                }, mDuration);

            }
        }, mDuration);
        //animHSI();
    }

    private void setAnimationProperties(PropertyValuesHolder animatorHolder) {

        va = ValueAnimator.ofPropertyValuesHolder(animatorHolder);
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.RESTART);
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mHSIView.setAngle((float)animation.getAnimatedValue());
                //Log.e("ANIMATOR" ,(float)animation.getAnimatedValue()+"" ); //WORK!
            }
        });
    }

}