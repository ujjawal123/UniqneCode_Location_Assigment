package Adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webapps.googleassigment_test.ShowMap;
import com.example.webapps.googleassigment_test.*;

import java.util.List;

import Database.DBhelper;
import Database.Location_Data;


/**
 * Created by ujjaw on 14-04-2017.
 */
public class MyRecyclerAdapter1 extends RecyclerView.Adapter<MyRecyclerAdapter1.CustomViewHolder> {
    private List<Location_Data> mLocationdata;
    private Context mContext;
    private DBhelper DbHelper;
    public final int position = 0;
    //UpdateAdapter listener;
    ShowLocationData listener;
    public MyRecyclerAdapter1(Context context, List<Location_Data> mToptensong) {
        this.mLocationdata = mToptensong;
        this.mContext = context;
        this.listener= (ShowLocationData) context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listviewitem, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Location_Data Loc_data = mLocationdata.get(i);
        customViewHolder.address.setText(Loc_data.getArea());
        customViewHolder.code.setText(Loc_data.getUniqId());
        customViewHolder.lat.setText(Loc_data.getLag());
        customViewHolder.lng.setText(Loc_data.getLng());
        customViewHolder.time.setText(Loc_data.getTdate());
        customViewHolder.sendData.setTag(i);
        customViewHolder.deletedata.setTag(i);
        customViewHolder.sendData.setOnClickListener(new View.OnClickListener() {
            //
            @Override
            public void onClick(View arg0) {

                int position = (Integer) arg0.getTag();
                Intent sendlocdata = new Intent(mContext, ShowMap.class);
                Location_Data loc_data = mLocationdata.get(position);
                sendlocdata.putExtra("Lat", loc_data.getLag());
                sendlocdata.putExtra("Lng", loc_data.getLng());
                sendlocdata.putExtra("address", loc_data.getArea());
                sendlocdata.putExtra("Time", loc_data.getTdate());
                sendlocdata.putExtra("uniqne", loc_data.getUniqId());
                sendlocdata.putExtra("position", position);
                mContext.startActivity(sendlocdata);
            }
        });

        customViewHolder.deletedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final int position = (Integer) v.getTag();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);

                // set title
                alertDialogBuilder.setTitle("Location");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you Really want to delete the location")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Location_Data loc_data = mLocationdata.get(position);
                                Log.d("Tag", "sort code" + loc_data.getUniqId());
                                DbHelper = new DBhelper(mContext);
                                DbHelper.open();
                                DbHelper.deleteLocation(loc_data.getUniqId());
                                mLocationdata.remove(position);
                                DbHelper.close();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return (null != mLocationdata ? mLocationdata.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
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

   public  interface UpdateAdapter{
        void updateAdpater();
    }

}
