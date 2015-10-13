package gr.apphub.motostats;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gr.apphub.motostats.db.HotOrNotMyVehicles;
import gr.apphub.motostats.db.HotOrNotadd;

/**
 * Created by Konstantinos on 4/2/15.
 */
public class menu3_fragment extends Fragment {
    View rootview;
    List<String> lables;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    com.rengwuxian.materialedittext.MaterialEditText ed1, ed2, ed3, ed4, ed_date;
    double total_cost = 0;
    double price_per_litre = 0;
    double number_of_litres = 0;
    double km = 0;
    String mydate = "";
    Button save_btn, cancel_btn;
    HotOrNotadd entry;
    HotOrNotMyVehicles entrymyveh;
    LinearLayout date_layout;
    Spinner spinner;
    String myVehicleName, myVehicleType;
    private DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu3_layout, container, false);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        entry = new HotOrNotadd(getActivity());
        entry.open();
        entrymyveh = new HotOrNotMyVehicles(getActivity());
        entrymyveh.open();


        save_btn = (Button) rootview.findViewById(R.id.save_btn);
        cancel_btn = (Button) rootview.findViewById(R.id.cancel_btn);
//        int buttonColor = 0xff33b5e5; //holo_blue_light
//        int rippleColor = 0x80ffffff; //transparent white
//        int buttonColor2 = 0xffFF5252; //holo_blue_light
//        int rippleColor2= 0x80ffffff; //transparent white
//        save_btn.setColors(buttonColor, rippleColor);
//        cancel_btn.setColors(buttonColor2, rippleColor2);
        myCalendar = Calendar.getInstance();

        spinner = (Spinner) rootview.findViewById(R.id.spinner_myveh);
        lables = new ArrayList<String>();

        loadSpinnerData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                myVehicleName = lables.get(arg2).toString();
                Cursor typec = entrymyveh.getDatavehByName(myVehicleName);
                for (int i = 0; i < typec.getCount(); i++) {
                    typec.moveToFirst();
                    int col = typec.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_TYPE);
                    myVehicleType = typec.getString(col);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        ed1 = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.ed1);
        ed2 = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.ed2);
        ed3 = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.ed3);
        ed4 = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.ed4);
        ed_date = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.ed_date);


        //PROTINEI TIN SIMERINI IMEROMINIA
        //-------------
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        ed_date.setText(sdf.format(myCalendar.getTime()));
        mydate = sdf.format(myCalendar.getTime());
        //-------------

        date_layout = (LinearLayout) rootview.findViewById(R.id.date_layout);


        ed1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    number_of_litres = Double.parseDouble(s.toString().replace(",", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                total_cost = price_per_litre * number_of_litres;
//                ed3.setText(Double.toString(total_cost));

            }
        });


        ed2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    price_per_litre = Double.parseDouble(s.toString().replace(",", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                total_cost = price_per_litre * number_of_litres;
//                ed3.setText(Double.toString(total_cost));


            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    total_cost = Double.parseDouble(s.toString().replace(",", "."));

                    //  price_per_litre = (double) total_cost / (double) number_of_litres;

                    // ed2.setText(Double.toString(price_per_litre));

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        ed4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //  Toast.makeText(getActivity(), "You put gas on " + s + " km", Toast.LENGTH_LONG).show();
//System.out.println("1>"+number_of_litres+"\n2>"+price_per_litre+"/n3>"+total_cost);
//                ed1.setText(Double.toString(number_of_litres));
//                ed2.setText(Double.toString(price_per_litre));
//                ed3.setText(Double.toString(total_cost));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    km = Double.parseDouble(s.toString().replace(",", "."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (number_of_litres != 0 && price_per_litre != 0 && total_cost != 0 && km != 0) {
                    try {
                        entry.createEntryadd(number_of_litres, price_per_litre, mydate, total_cost, km, myVehicleName, myVehicleType);
                        Toast.makeText(getActivity(), "Data saved!!!" + mydate, Toast.LENGTH_SHORT).show();

                        Fragment objFragment = new menu1_fragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, objFragment).commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Data **ERROR**", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "Please fill everything!", Toast.LENGTH_SHORT).show();


                }
            }

        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    if (number_of_litres == 0 && price_per_litre == 0 && total_cost == 0 && km == 0 && mydate.equals("")) {
                        Toast.makeText(getActivity(), "No Data to clean", Toast.LENGTH_SHORT).show();

                    } else {
                        number_of_litres = 0;
                        price_per_litre = 0;
                        total_cost = 0;
                        km = 0;
                        mydate = "";
                        ed1.setText("");
                        ed2.setText("");
                        ed3.setText("");
                        ed4.setText("");
                        ed_date.setText("");
                        Toast.makeText(getActivity(), "Data cleared successful!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Data cleared **ERROR**", Toast.LENGTH_SHORT).show();

                }
            }

        });


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        ed_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootview;
    }


    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        ed_date.setText(sdf.format(myCalendar.getTime()));
        mydate = sdf.format(myCalendar.getTime());
    }

    private void loadSpinnerData() {
        // database handler

        Cursor cursor = entrymyveh.getDataveh();
        if (cursor.getCount() == 0) {
            lables.add("Unknown Vehicle");
            myVehicleName = "Unknown";
            myVehicleType = "Unknown";

        } else {

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                int col = cursor.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_NAME);
                String col1 = cursor.getString(col);
                if (!lables.contains(col1)) {
                    lables.add(col1);
                }


            }

        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // THE DESIRED COLUMNS TO BE BOUND

    }
}
