package gr.apphub.motostats;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.dexafree.materialList.cards.BasicImageButtonsCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;

import gr.apphub.motostats.db.HotOrNotMyVehicles;

/**
 * Created by Konstantinos on 4/2/15.
 */


public class my_vehicles extends Fragment {
    View rootview;
    LinearLayout myveh_linear;
    HotOrNotMyVehicles entryMyVehicles;
    Button add_btn;
    String click_fcol1, click_bcol1, click_col1, click_ccol1, click_dcol1, click_ecol1;
    Cursor c;
    MaterialListView mListView;
    int card_position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_vehicles, container, false);
//showUserSettings();

        entryMyVehicles = new HotOrNotMyVehicles(getActivity());
        entryMyVehicles.open();
        mListView = (MaterialListView) rootview.findViewById(R.id.material_listview);
        myveh_linear = (LinearLayout) rootview.findViewById(R.id.myveh_linear);
        add_btn = (Button) rootview.findViewById(R.id.add_btn);

        c = entryMyVehicles.getDataveh();

        if (c.getCount() == 0) {
            TextView tv2 = new TextView(getActivity());
            tv2.setText("Press \"ADD\" button to create new vehicles");
            tv2.setTextColor(Color.parseColor("#bdbdbd"));
            tv2.setTextSize(18);
            myveh_linear.addView(tv2);

        } else {
            myveh_linear.setVisibility(View.GONE);
            addCards();

//        System.out.println(col1+":::"+bcol1+":::"+ccol1+":::"+dcol1+":::"+ecol1);

        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Log.i("add_btn", "pressed");
                Fragment objFragment = new menu2_fragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, objFragment).addToBackStack(null).commit();


            }

        });

        return rootview;
    }


    public void dialog() {

        new AlertDialogWrapper.Builder(getActivity())
                .setTitle("Delete vehicle")
                .setMessage("You are ready to delete this vehicle.\nAre you sure??")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entryMyVehicles.deleteItem2(click_fcol1, click_bcol1);
                        Fragment objFragment = new my_vehicles();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, objFragment).addToBackStack(null).commit();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void addCards() {

        for (int i = 0; i < c.getCount(); i++) {
            card_position = i;
            c.moveToPosition(i);
            int col = c.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_MODEL1);
            final String col1 = c.getString(col);
            int bcol = c.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_MODEL2);
            final String bcol1 = c.getString(bcol);
            int ccol = c.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_YEAR);
            final String ccol1 = c.getString(ccol);
            int dcol = c.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_CC);
            final String dcol1 = c.getString(dcol);
            int ecol = c.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_TYPE);
            final String ecol1 = c.getString(ecol);
            int fcol = c.getColumnIndex(HotOrNotMyVehicles.DBHelper_myveh.VEH_NAME);
            final String fcol1 = c.getString(fcol);

            SimpleCard card4 = new BasicImageButtonsCard(getActivity());
            card4.setDescription("Year: " + ccol1 + "\nCC: " + dcol1);
            card4.setTitle(col1 + " " + bcol1);
            if (ecol1.equals("car")) {
                //mixani
                card4.setDrawable(R.mipmap.ic_car);
            } else {
                //amaksi
                card4.setDrawable(R.mipmap.ic_moto);
            }
            ((BasicImageButtonsCard) card4).setLeftButtonText("DELETE");
            ((BasicImageButtonsCard) card4).setRightButtonText("EDIT");
            ((BasicImageButtonsCard) card4).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                @Override
                public void onButtonPressedListener(View view, Card card) {
                    click_col1 = col1;
                    click_bcol1 = bcol1;
                    click_ccol1 = ccol1;
                    click_dcol1 = dcol1;
                    click_ecol1 = ecol1;
                    click_fcol1 = fcol1;

                    dialog();
                }
            });

            ((BasicImageButtonsCard) card4).setOnRightButtonPressedListener(new OnButtonPressListener() {
                @Override
                public void onButtonPressedListener(View view, Card card) {
                    click_col1 = col1;
                    click_bcol1 = bcol1;
                    click_ccol1 = ccol1;
                    click_dcol1 = dcol1;
                    click_ecol1 = ecol1;
                    click_fcol1 = fcol1;
                    edit();
                }
            });

            // card4.setDismissible(true);
            mListView.add(card4);


        }

    }

    public void edit() {

//       Cursor c= entryMyVehicles.getDatavehByData(click_fcol1,click_bcol1,click_ecol1);
        Fragment objFragment = new menu2_fragment();


        Bundle bundle = new Bundle();
        bundle.putString("click_col1", click_col1);
        bundle.putString("click_bcol1", click_bcol1);
        bundle.putString("click_ccol1", click_ccol1);
        bundle.putString("click_dcol1", click_dcol1);
        bundle.putString("click_ecol1", click_ecol1);
        bundle.putString("click_fcol1", click_fcol1);

        objFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, objFragment).addToBackStack(null).commit();


    }
//
//    private void showUserSettings() {
//        SharedPreferences sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());
//
//
//        try {
//            String prefList = sharedPrefs.getString("PREF_LIST", "*KMKMKM");
//            add_btn.setText(prefList);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//
//    }

}