package com.jakebergmain.ledstrip;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LedFragment extends Fragment implements DiscoverTask.DiscoverCallback {
    private final String LOG_TAG = LedFragment.class.getSimpleName();

    private Button buttonPreview;

    public LedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_led, container, false);

        ColorPickerView picker = (ColorPickerView) view.findViewById(R.id.colorPicker);


        final TextView textViewRed = (TextView) view.findViewById(R.id.textViewRed);
        final TextView textViewGreen = (TextView) view.findViewById(R.id.textViewGreen);
        final TextView textViewBlue = (TextView) view.findViewById(R.id.textViewBlue);

        buttonPreview = (Button) view.findViewById(R.id.buttonPreview);
        buttonPreview.setOnClickListener(v -> searchForDevices());

        picker.addOnColorChangedListener(color ->
                getActivity().runOnUiThread(() -> {

                    int r = (0xFF0000 & color) >> 16;
                    int g = (0x00FF00 & color) >> 8;
                    int b = (0x0000FF & color);

                    buttonPreview.setBackgroundColor(color);
                    buttonPreview.setTextColor(Color.rgb(255 - r, 255 - g, 255 - b));

                    textViewRed.setText(String.format("RED %03d", r));
                    textViewGreen.setText(String.format("GREEN %03d", g));
                    textViewBlue.setText(String.format("BLUE %03d", b));

                    new ChangeColorTask(getContext()).execute(color);
                }));
//        searchForDevices();
        return view;
    }

    public void scanForDevice(View v) {
        searchForDevices();
    }

    /**
     * Start DiscoverTask to search for devices on local network.
     */
    public void searchForDevices() {
        Log.w(LOG_TAG, "starting DiscoverTask");
        new DiscoverTask(getContext(), this).execute(null, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFoundDevice(String address) {
        this.buttonPreview.setText("Connected: " + address);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
