package Adaptor;

import com.example.webapps.googleassigment_test.*;

import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import Database.Location_Data;


/**
 * Created by wifinetwork on 11/3/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> implements View.OnClickListener {
    private List<Location_Data> mLocationData;
    private Context mContext;
    Location_Data Loc_data;
    String loc_time;

    public MyRecyclerAdapter(Context context, List<Location_Data> mLocationData) {
        this.mLocationData = mLocationData;
        this.mContext = context;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listviewitem, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Loc_data = mLocationData.get(i);
        customViewHolder.address.setText(Loc_data.getArea());
        customViewHolder.code.setText(Loc_data.getUniqId());
        customViewHolder.lat.setText(Loc_data.getLag());
        customViewHolder.lng.setText(Loc_data.getLng());
        customViewHolder.time.setText(Loc_data.getTdate());
        customViewHolder.sendData.setOnClickListener(this);
        customViewHolder.deletedata.setOnClickListener(this);



        /*customViewHolder.sendData.setOnClickListener(new View.OnClickListener() {
            //
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"on click",Toast.LENGTH_SHORT).show();
              //  int position = (Integer) v.getTag();
               // Log.d("Tag","postion of location" +position);

               *//* Intent sendlocdata = new Intent(mContext, ShowMap.class);
                Location_Data loc_data = mLocationData.get(position);
                sendlocdata.putExtra("Lag", loc_data.getLag());sendlocdata.putExtra("Lng", loc_data.getLng());
                sendlocdata.putExtra("position", position);
                sendlocdata.putExtra("uniqne", loc_data.getUniqId());// Holy_quran
                //  mContext.setResult(100, songplaylist);
                Log.d("TAg", "Lng: " + loc_data.getLng());
                Log.d("Tag", "Lag : " + loc_data.getLag());
                Log.d("Tag", "Index Position : " + position);
                Log.d("Tag", "Uniqnecode : " + loc_data.getUniqId());
                mContext.startActivity(sendlocdata);*//*


            }
        });*/


    }

    @Override
    public int getItemCount() {
        return (null != mLocationData ? mLocationData.size() : 0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_data:
             //   int position = (int) v.getTag();
               // int thisItem = (Integer)v.getTag();
                if (v.getTag() == null) {

                    System.out.println("Bad things");
                }
                Log.d("Tag", "position" );

        }

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image, song_artist;
        protected TextView code, address, lat, lng, time;
        protected Button sendData, deletedata;

        public CustomViewHolder(View view) {
            super(view);

            code = (TextView) view.findViewById(R.id.uniqne_code);
            address = (TextView) view.findViewById(R.id.area);
            lat = (TextView) view.findViewById(R.id.lat);
            lng = (TextView) view.findViewById(R.id.lng);
            time = (TextView) view.findViewById(R.id.time);
            sendData = (Button) view.findViewById(R.id.send_data);
            deletedata = (Button) view.findViewById(R.id.delete_data);


        }

    }


}
