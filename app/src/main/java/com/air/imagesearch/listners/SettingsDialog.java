package com.air.imagesearch.listners;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.air.imagesearch.R;
import com.air.imagesearch.models.SettingsModel;

/**
 * Created by hetashah on 2/13/15.
 */
public class SettingsDialog extends DialogFragment {
    private Spinner spImageSize, spColorFilter, spTypeFilter;
    private Button btnSave, btnCancel;
    private TextView txtVwImgSize, txtVwImgColorFilter, txtVwImgTypeFilter;

    public interface SettingsDialogListener {
        void onSaveSettingsDialog(SettingsModel settings);
    }

    public SettingsDialog() {
        // Empty constructor required for DialogFragment
    }

    public static SettingsDialog newInstance(SettingsModel settings) {
        SettingsDialog frag = new SettingsDialog();
        Bundle args = new Bundle();
        args.putString("title", "Advanced Filters");
        args.putParcelable("settings", settings);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.cust_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_image_search, container);
        String title = getArguments().getString("title", "Enter Name");
        SettingsModel settings = getArguments().getParcelable("settings");

        setupView(view ,settings);
        addListenerOnButton(settings);

        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);


        getDialog().setTitle(title);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    private void setupView(View view, SettingsModel settings) {
        Typeface fontTypografixDemo = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Typografix-demo.otf");

        txtVwImgSize = (TextView) view.findViewById(R.id.txtVwImgSize);
        txtVwImgSize.setTypeface(fontTypografixDemo);
        txtVwImgColorFilter = (TextView) view.findViewById(R.id.txtVwImgColorFilter);
        txtVwImgColorFilter.setTypeface(fontTypografixDemo);
        txtVwImgTypeFilter = (TextView) view.findViewById(R.id.txtVwImgTypeFilter);
        txtVwImgTypeFilter.setTypeface(fontTypografixDemo);

        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        ArrayAdapter<CharSequence> spImageSizeAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.image_size_arrays, R.layout.spinner_item);
        spImageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(spImageSizeAdapter);
        spImageSize.setSelection(spImageSizeAdapter.getPosition(settings.getImgSize()));

        spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
        ArrayAdapter<CharSequence> spColorFilterAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.image_color_arrays, R.layout.spinner_item);
        spColorFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColorFilter.setAdapter(spColorFilterAdapter);
        spColorFilter.setSelection(spColorFilterAdapter.getPosition(settings.getImgColor()));

        spTypeFilter = (Spinner) view.findViewById(R.id.spTypeFilter);
        ArrayAdapter<CharSequence> spTypeFilterAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.image_type_arrays, R.layout.spinner_item);
        spTypeFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypeFilter.setAdapter(spTypeFilterAdapter);
        spTypeFilter.setSelection(spTypeFilterAdapter.getPosition(settings.getImgType()));

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
    }

    // get the selected dropdown list value
    public void addListenerOnButton(final SettingsModel settings) {

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingsDialogListener listener = (SettingsDialogListener) getActivity();
                settings.setImgColor(spColorFilter.getSelectedItem().toString());
                settings.setImgSize(spImageSize.getSelectedItem().toString());
                settings.setImgType(spTypeFilter.getSelectedItem().toString());
                listener.onSaveSettingsDialog(settings);
                dismiss();
            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
