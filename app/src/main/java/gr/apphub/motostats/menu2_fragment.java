package gr.apphub.motostats;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.apphub.motostats.db.HotOrNotMyVehicles;
import gr.apphub.motostats.db.HotOrNotVehiclesDB;
import gr.apphub.motostats.db.HotOrNotVehiclesDB.DBHelper_veh;


/**
 * Created by Konstantinos on 4/2/15.
 */


public class menu2_fragment extends Fragment {
    View rootview;
    Spinner spinner, spinner2, spinner3, spinner4;
    HotOrNotVehiclesDB entryVehiclesDB;
    HotOrNotMyVehicles entryMyVehicles;
    String name = "", model1, model2, year, cc;
    List<String> lables_model1, lables_model2, lables_year, lables_cc;
    Button save_btn, cancel_btn;
    String myVehicle = "";
    com.rengwuxian.materialedittext.MaterialEditText ed1_veh;


    int edit = 1;
    //0 true
    // 1->false
    String click_fcol1, click_bcol1, click_col1, click_ccol1, click_dcol1, click_ecol1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu2_layout, container, false);


        Bundle bundle = getArguments();

        if (bundle != null) {
            click_col1 = bundle.getString("click_col1");
            click_bcol1 = bundle.getString("click_bcol1");
            click_ccol1 = bundle.getString("click_ccol1");
            click_dcol1 = bundle.getString("click_dcol1");
            click_ecol1 = bundle.getString("click_ecol1");
            click_fcol1 = bundle.getString("click_fcol1");
            edit = 0;
        }
        ed1_veh = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.ed1_veh);

        entryVehiclesDB = new HotOrNotVehiclesDB(getActivity());
        entryVehiclesDB.open();
        entryMyVehicles = new HotOrNotMyVehicles(getActivity());
        entryMyVehicles.open();

        save_btn = (Button) rootview.findViewById(R.id.save_btn);
        cancel_btn = (Button) rootview.findViewById(R.id.cancel_btn);

        spinner = (Spinner) rootview.findViewById(R.id.spinner);
        spinner2 = (Spinner) rootview.findViewById(R.id.spinner2);
        spinner3 = (Spinner) rootview.findViewById(R.id.spinner3);
        spinner4 = (Spinner) rootview.findViewById(R.id.spinner4);

        lables_model1 = new ArrayList<String>();
        lables_model2 = new ArrayList<String>();
        lables_year = new ArrayList<String>();
        lables_cc = new ArrayList<String>();


        if (edit == 0) {
            ed1_veh.setText(click_fcol1);
        }

        loadSpinnerData_MODEL1();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                model1 = lables_model1.get(arg2).toString();
                System.out.println(model1);
                loadSpinnerData_MODEL2();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                model2 = lables_model2.get(arg2).toString();
                System.out.println(model2);
                loadSpinnerData_YEAR();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                year = lables_year.get(arg2).toString();
                System.out.println(year);
                loadSpinnerData_CC();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                cc = lables_cc.get(arg2).toString();
                System.out.println(cc);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    name = ed1_veh.getText().toString();

                    Cursor c = entryMyVehicles.sameNameVehicle(name, myVehicle);

                    int sameitems = c.getCount();
                    if (sameitems <= 0 && !name.equals("")) {
                        printMyVehicle();

                        if (edit == 0) {
                            entryMyVehicles.updateEntry(click_fcol1, click_bcol1, click_ecol1, name, model1, model2, year, cc, myVehicle);

                            Toast.makeText(getActivity(), myVehicle + " successfully updated in DB!!", Toast.LENGTH_LONG).show();

                        } else {
                            entryMyVehicles.createEntryveh(name, model1, model2, year, cc, myVehicle);

                            Toast.makeText(getActivity(), myVehicle + " successfully added in DB!!", Toast.LENGTH_LONG).show();
                        }
                        Fragment objFragment = new my_vehicles();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, objFragment).commit();


                    } else if (sameitems > 0) {
                        Toast.makeText(getActivity(), "There is already a " + myVehicle + " named \"" + name + "\"", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getActivity(), "PLease fill everything", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "error:" + e.toString(), Toast.LENGTH_LONG).show();

                }
            }

        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Fragment objFragment = new my_vehicles();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, objFragment).commit();
            }

        });
        return rootview;
    }

    public void printMyVehicle() {
//        System.out.println("BEFORE\n"+model1+":::"+model2+":::"+year+":::"+cc);

        Cursor c = entryVehiclesDB.getDatavehType(model1, model2, year, cc);
        c.moveToFirst();
        int col = c.getColumnIndex(DBHelper_veh.VEH_MODEL1);
        String col1 = c.getString(col);
        int bcol = c.getColumnIndex(DBHelper_veh.VEH_MODEL2);
        String bcol1 = c.getString(bcol);
        int ccol = c.getColumnIndex(DBHelper_veh.VEH_YEAR);
        String ccol1 = c.getString(ccol);
        int dcol = c.getColumnIndex(DBHelper_veh.VEH_CC);
        String dcol1 = c.getString(dcol);
        int ecol = c.getColumnIndex(DBHelper_veh.VEH_TYPE);
        String ecol1 = c.getString(ecol);

//        System.out.println(col1+":::"+bcol1+":::"+ccol1+":::"+dcol1+":::"+ecol1);

        if (ecol1.equals("1")) {
            myVehicle = "moto";
        } else if (ecol1.equals("2")) {
            myVehicle = "car";
        }
        Toast.makeText(getActivity(), "Your " + myVehicle + " is a " + col1 + " " + bcol1 + " ," + ccol1 + " model and " + dcol1 + "cc !!", Toast.LENGTH_LONG).show();


    }

    private void loadSpinnerData_MODEL1() {
        // database handler


        Cursor cursor = entryVehiclesDB.getDatavehModel1();


        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int col = cursor.getColumnIndex(gr.apphub.motostats.db.HotOrNotVehiclesDB.DBHelper_veh.VEH_MODEL1);
            String col1 = cursor.getString(col);
            if (!lables_model1.contains(col1)) {
                lables_model1.add(col1);
            }

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables_model1);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // THE DESIRED COLUMNS TO BE BOUND

        if (edit == 0) {
            spinner.setSelection(((ArrayAdapter) spinner.getAdapter()).getPosition(click_col1));
        }

    }

    private void loadSpinnerData_MODEL2() {
        // database handler
        lables_model2.clear();

        Cursor cursor = entryVehiclesDB.getDatavehModel2(model1);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int col = cursor.getColumnIndex(DBHelper_veh.VEH_MODEL2);
            String col1 = cursor.getString(col);
            if (!lables_model2.contains(col1)) {
                lables_model2.add(col1);
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables_model2);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter);
        // THE DESIRED COLUMNS TO BE BOUND
        if (edit == 0) {
            spinner2.setSelection(((ArrayAdapter) spinner2.getAdapter()).getPosition(click_bcol1));
        }
    }

    private void loadSpinnerData_YEAR() {
        // database handler
        lables_year.clear();

        Cursor cursor = entryVehiclesDB.getDatavehYear(model2);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int col = cursor.getColumnIndex(DBHelper_veh.VEH_YEAR);
            String col1 = cursor.getString(col);
            if (!lables_year.contains(col1)) {
                lables_year.add(col1);
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables_year);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner3.setAdapter(dataAdapter);
        // THE DESIRED COLUMNS TO BE BOUND

        if (edit == 0) {
            spinner3.setSelection(((ArrayAdapter) spinner3.getAdapter()).getPosition(click_ccol1));
        }
    }

    private void loadSpinnerData_CC() {
        // database handler
        lables_cc.clear();

        Cursor cursor = entryVehiclesDB.getDatavehCc(model2, year);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int col = cursor.getColumnIndex(DBHelper_veh.VEH_CC);
            String col1 = cursor.getString(col);
            if (!lables_cc.contains(col1)) {
                lables_cc.add(col1);
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables_cc);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner4.setAdapter(dataAdapter);
        // THE DESIRED COLUMNS TO BE BOUND

        if (edit == 0) {
            spinner4.setSelection(((ArrayAdapter) spinner4.getAdapter()).getPosition(click_dcol1));
        }

    }


}

