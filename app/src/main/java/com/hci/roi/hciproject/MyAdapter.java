package com.hci.roi.hciproject;

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
                break;
            case 1:
                view = (LayoutInflater.from(myParentView.getContext())
                        .inflate(R.layout.card_adi, myParentView, false));
                break;
            case 2:
                view = (LayoutInflater.from(myParentView.getContext())
                        .inflate(R.layout.card_radar, myParentView, false));

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
}