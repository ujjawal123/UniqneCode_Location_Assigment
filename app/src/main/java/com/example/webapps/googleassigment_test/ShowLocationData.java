package com.example.webapps.googleassigment_test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Adaptor.MyRecyclerAdapter;
import Adaptor.MyRecyclerAdapter1;
import Adaptor.RecyclerItemClickListener;
import Database.DBhelper;
import Database.Location_Data;

/**
 * Created by ujjaw on 14-04-2017.
 */
public class ShowLocationData extends AppCompatActivity  implements MyRecyclerAdapter1.UpdateAdapter{
MyRecyclerAdapter1 adapter;
    RecyclerView showlocdata;
    DBhelper dBhelper;
    Location_Data Loc_data;
    private List<Location_Data> AllLocResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_show);

        showlocdata=(RecyclerView)findViewById(R.id.load_location_data);
        showlocdata.setLayoutManager(new LinearLayoutManager(this));
        GetLocationResult();
    }

    private void GetLocationResult() {

        dBhelper=new DBhelper(this);
        dBhelper.open();
        //   employee_One = DbHelper.retriveEmpDetails();
        AllLocResult = dBhelper.getAllRecords();
        adapter = new MyRecyclerAdapter1(this, AllLocResult);
        showlocdata.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void updateAdpater() {
        if(adapter!=null)
        adapter.notifyDataSetChanged();
    }


   /* @Override
    protected void onStart() {
        super.onStart();
        Log.d("Tag","data change");
        dBhelper=new DBhelper(this);
        AllLocResult.clear();
        AllLocResult = dBhelper.getAllRecords(); // reload the items from database
        adapter.notifyDataSetChanged();
    }*/
}