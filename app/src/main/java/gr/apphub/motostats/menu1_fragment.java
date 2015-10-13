package gr.apphub.motostats;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.cards.BasicListCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.view.MaterialListView;

import org.json.JSONArray;

import java.io.File;

import gr.apphub.motostats.db.HotOrNotVehiclesDB;
import gr.apphub.motostats.db.HotOrNotadd;
import gr.apphub.motostats.service.DownloadService;

/**
 * Created by Konstantinos on 4/2/15.
 */
public class menu1_fragment extends Fragment {
    // JSON ARAYS
    private static final String TAG_VEHICLES = "VEHICLES";
    // JSON OBJECTS
    private static final String TAG_ID = "id";
    private static final String TAG_MODEL_1 = "model_1";
    private static final String TAG_MODEL_2 = "model_2";
    private static final String TAG_YEAR = "year";
    private static final String TAG_CC = "cc";
    private static final String TAG_TYPE = "type";
    View rootview;
    //    LinearLayout myveh_linear;
    HotOrNotadd entry;
    ProgressDialog dialog;
    File SDCardRoot;
    String json_file = "json.txt";
    String VEHICLES_URL = "http://kostas-menu.gr/fuellog/vehicles.json";
    JSONArray VEHICLES = null;
    HotOrNotVehiclesDB entryVehiclesDB;
    ProgressDialog pd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu1_layout, container, false);

        MaterialListView mListView = (MaterialListView) rootview.findViewById(R.id.material_listview);
        SDCardRoot = new File(
                android.os.Environment.getExternalStorageDirectory(),
                "Fuellog-Data");

        startDownloadService();

        entryVehiclesDB = new HotOrNotVehiclesDB(getActivity());
        entryVehiclesDB.open();

        entry = new HotOrNotadd(getActivity());
        entry.open();

        Cursor c = entry.getDataadd();


        if (c.getCount() == 0) {
//            TextView tv2 = new TextView(getActivity());
//            tv2.setText("You have not any data yet.");
//            tv2.setTextColor(Color.parseColor("#bdbdbd"));
//            tv2.setTextSize(18);
//            myveh_linear.addView(tv2);
            WelcomeCard card = new WelcomeCard(getActivity());
            card.setDescription("Here you can add bla bla");
            card.setSubtitle("Welcome to the app");

            card.setTitle("Hello");
            ((WelcomeCard) card).setBackgroundColorRes(R.color.colorPrimary);
            ((WelcomeCard) card).setButtonText("Okay!");

            mListView.add(card);


            BasicListCard card2 = new BasicListCard(getActivity());
            card2.setTitle("Lets get started!");
            card2.setDescription("Thinks to do in the app");
            BasicListAdapter adapter;
            adapter = new BasicListAdapter(getActivity());
            adapter.add("Add your vehicles");
            adapter.add("Create logs");
            adapter.add("Change your preferences");
            ((BasicListCard) card2).setAdapter(adapter);
            ((BasicListCard) card2).setBackgroundColorRes(R.color.colorPrimary);

            mListView.add(card2);

        } else {
//            myveh_linear.setVisibility(View.GONE);

            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);

                int nolCol = c.getColumnIndex(HotOrNotadd.DBHelper_add.NUM_OF_LITRES);
                double nol1 = c.getDouble(nolCol);

                int pplCol = c.getColumnIndex(HotOrNotadd.DBHelper_add.PRICE_PER_LITRE);
                double ppl1 = c.getDouble(pplCol);

                int paCol = c.getColumnIndex(HotOrNotadd.DBHelper_add.PAY_AMOUNT);
                double pa1 = c.getDouble(paCol);


                int dateCol = c.getColumnIndex(HotOrNotadd.DBHelper_add.DATE);
                String dateCol1 = c.getString(dateCol);

                int nameCol = c.getColumnIndex(HotOrNotadd.DBHelper_add.VEH_NAME);
                String name = c.getString(nameCol);

                int typeCol = c.getColumnIndex(HotOrNotadd.DBHelper_add.VEH_TYPE);
                String type = c.getString(typeCol);

                SmallImageCard card = new SmallImageCard(getActivity());
                card.setTitle("You paid " + Double.toString(pa1) + " Euros for \"" + name + "\"");
                card.setDescription("Number of litres:" + Double.toString(nol1) + "\nPrice per Litre:" + Double.toString(ppl1) + "\nDate: " + dateCol1);

                if (type.equals("moto")) {
                    card.setDrawable(R.mipmap.ic_moto);
                } else if (type.equals("Unknown")) {
                    card.setDrawable(R.mipmap.ic_unknown);
                } else {
                    card.setDrawable(R.mipmap.ic_car);
                }

                mListView.add(card);
            }
        }


        return rootview;
    }

    public void DownloadJson() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.TRANSACTION_DONE);
        getActivity().registerReceiver(jsonReceiver, intentFilter);
        Intent i = new Intent(getActivity(), DownloadService.class);
        i.putExtra("url", VEHICLES_URL);
        getActivity().startService(i);
        pd = ProgressDialog.show(getActivity(), "Download Data",
                "Go intent service go!");
    }

    public void startDownloadService() {

        DownloadJson();

    }

    private BroadcastReceiver jsonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String location = intent.getExtras().getString("location");
            String url = intent.getExtras().getString("url");
            Log.d("---location---", location);
            Log.d("---url---", url);

            if (location == null || location.length() == 0) {
                Toast.makeText(context, "Failed to download json",
                        Toast.LENGTH_LONG).show();
            }
            pd.dismiss();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().unregisterReceiver(jsonReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
