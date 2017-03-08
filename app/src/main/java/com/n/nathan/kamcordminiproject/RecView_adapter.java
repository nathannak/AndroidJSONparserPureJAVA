
package com.n.nathan.kamcordminiproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


public class RecView_adapter extends RecyclerView.Adapter<RecView_adapter.ViewHolder> {

    List<String> list2 = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //
        CardView cardview;
        //some variables here
        //

        public ViewHolder(CardView v) {
            //
            super(v);
            cardview = v;

        }

        @Override
        public void onClick(View v) {}

    }
    //

   public RecView_adapter(List<String> list1, List<String> list2, List<String> list3)
   {

   }

    @Override
    public RecView_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_cardview_4s, parent, false);
        //

        //

        return new ViewHolder(cv);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final CardView cardView = holder.cardview;
        //

        //end of onBindViewHolder
    }

    //
    @Override
    public void onViewRecycled(ViewHolder holder) {

        //holder.imagev.setImageBitmap(null);
    }
    //

    @Override
    public int getItemCount(){
        //return captions.length;
        //return arrayofids.size();
        return 10;
    }
}
