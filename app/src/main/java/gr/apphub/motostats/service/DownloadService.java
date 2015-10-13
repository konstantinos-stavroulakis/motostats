package gr.apphub.motostats.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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

/**
 * Created by Konstantinos on 07/10/15.
 */
public class DownloadService extends IntentService {
    public static final String TRANSACTION_DONE =
            " gr.apphub.motostats.TRANSACTION_DONE";
    private static final String TAG = DownloadService.class.getSimpleName();
    private String url;
    // JSON ARAYS
    private static final String TAG_VEHICLES = "VEHICLES";
    // JSON OBJECTS
    private static final String TAG_ID = "id";
    private static final String TAG_MODEL_1 = "model_1";
    private static final String TAG_MODEL_2 = "model_2";
    private static final String TAG_YEAR = "year";
    private static final String TAG_CC = "cc";
    private static final String TAG_TYPE = "type";

    public static final String INPUT_TEXT = "INPUT_TEXT";
    public static final String OUTPUT_TEXT = "OUTPUT_TEXT";
    String cardMechanincs;
    JSONArray VEHICLES = null;
    HotOrNotVehiclesDB entryVehiclesDB;
    String json_file = "json.txt";


    File SDCardRoot;


    public DownloadService() {
        super("DownloadService");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent()");
        url = intent.getExtras().getString("url");
        SDCardRoot = new File(
                android.os.Environment.getExternalStorageDirectory(),
                getApplicationContext().getPackageName());
        if (!SDCardRoot.exists()) {
            SDCardRoot.mkdirs();
        }
        getDataFromJson();
    }

    private void notifyFinished(String location, String remoteUrl) {
        Intent i = new Intent(TRANSACTION_DONE);
        i.putExtra("location", location);
        i.putExtra("url", remoteUrl);
        DownloadService.this.sendBroadcast(i);
    }

    public void getDataFromJson() {
        ServiceHandler sh = new ServiceHandler();
        try {



            File jfile = new File(SDCardRoot, json_file);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr + "\n");

            if (jsonStr != null) {
                if (jsonStr.equals(json_readFromFile())) {
                    //an to json einai idio me to palio min kaneis tpt
                } else {
                    //1.diegrapse ta ola
                    //2.grapse to neo json
                    //3.katevase ta arxeia

                    //1
                    entryVehiclesDB = new HotOrNotVehiclesDB(this);
                    entryVehiclesDB.open();
                    entryVehiclesDB.deleteAll();
                    entryVehiclesDB.close();

                    //2
                    json_writeToFile(jsonStr);

                    //3
                    JSONObject json;
                    try {
                        json = new JSONObject(jsonStr);
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

                            addToDatabase(MYTAG_MODEL_1, MYTAG_MODEL_2, MYTAG_YEAR, MYTAG_CC, MYTAG_TYPE);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }


        } catch (Exception e2) {
            e2.printStackTrace();
        }

        //notify that the job has finished
        notifyFinished("location", url);



}

    public void addToDatabase(String MODEL_1, String MODEL_2, String YEAR, String CC, String TYPE) {
        //1)open db 2)check if cardid exists (we take as a fact that cardid is unique) 3)if not exist then add a new db entry
        entryVehiclesDB.open();
        entryVehiclesDB.createEntryveh(MODEL_1, MODEL_2, YEAR, CC, TYPE);
        entryVehiclesDB.close();

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
}
