package gr.apphub.motostats;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.cards.BasicListCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.view.MaterialListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import gr.apphub.motostats.db.HotOrNotVehiclesDB;
import gr.apphub.motostats.db.HotOrNotadd;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu1_layout, container, false);
        entry = new HotOrNotadd(getActivity());
        entry.open();
        MaterialListView mListView = (MaterialListView) rootview.findViewById(R.id.material_listview);
        SDCardRoot = new File(
                android.os.Environment.getExternalStorageDirectory(),
                "Fuellog-Data");
        entryVehiclesDB = new HotOrNotVehiclesDB(getActivity());
        entryVehiclesDB.open();

//        myveh_linear = (LinearLayout) rootview.findViewById(R.id.myveh_linear);

        new getVehiclesTask().execute();

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

    public void getDataFromServer() {
        try {

            JSONParser jParser = new JSONParser();

            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(VEHICLES_URL);
            File jfile = new File(SDCardRoot, json_file);

            if (json.toString().equals(json_readFromFile())) {
                //an to json einai idio me to palio min kaneis tpt
            } else {
                //1.diegrapse ta ola
                //2.grapse to neo json
                //3.katevase ta arxeia

                //1
                entryVehiclesDB.deleteAll();

                //2
                json_writeToFile(json.toString());

                //3
                VEHICLES = json.getJSONArray("VEHICLES");
                for (int i = 0; i < VEHICLES.length(); i++) {
                    JSONObject jsonobject = VEHICLES.getJSONObject(i);
                    String MYTAG_ID, MYTAG_MODEL_1, MYTAG_MODEL_2, MYTAG_YEAR, MYTAG_CC, MYTAG_TYPE;

                    MYTAG_ID = jsonobject.getString(TAG_ID);
                    MYTAG_MODEL_1 = jsonobject.getString(TAG_MODEL_1);
                    MYTAG_MODEL_2 = jsonobject.getString(TAG_MODEL_2);
                    MYTAG_YEAR = jsonobject.getString(TAG_YEAR);
                    MYTAG_CC = jsonobject.getString(TAG_CC);
                    MYTAG_TYPE = jsonobject.getString(TAG_TYPE);

                    entryVehiclesDB.createEntryveh(MYTAG_MODEL_1, MYTAG_MODEL_2, MYTAG_YEAR, MYTAG_CC, MYTAG_TYPE);

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String json_readFromFile() {

        String ret = "";

        try {

            File myFile = new File(SDCardRoot, json_file);

            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(
                    fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer;
            myReader.close();

        } catch (Exception e1) {
            Log.e(json_file, "Read file failed: " + e1.toString());
        }

        return ret;

    }

    private void json_writeToFile(String json) {

        try {
            File myFile = new File(SDCardRoot, json_file);

            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(json);
            myOutWriter.close();
            fOut.close();
            Log.i(json_file, "File writen");

        } catch (IOException e) {
            Log.e(json_file, "File write failed: " + e.toString());
        }

    }

    public class getVehiclesTask extends AsyncTask<Void, Void, Void> {

        int myProgress;

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            Cursor c = entryVehiclesDB.getDatavehModel1();

            System.out.println("items" + c.getCount());
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = ProgressDialog.show(getActivity(), "", "Loading data", true);
            myProgress = 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            // TODO Auto-generated method stub
            // super.onProgressUpdate(values);
            // dialogfeed.setProgress(progress[0]);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            getDataFromServer();


            return null;
        }
    }
}
