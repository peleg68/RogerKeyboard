package org.pelegtsadok.rogerkeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PopupFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner wordSpinner;
    private MainActivity managingActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // I use a reference to the calling activity to access its
        // public methods:
        managingActivity = ((MainActivity)getActivity());

        wordSpinner = view.findViewById(R.id.word_spinner);
        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this.managingActivity,
                R.array.roger_words_array,
                android.R.layout.simple_spinner_item
        );

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wordSpinner.setAdapter(arrayAdapter);

        wordSpinner.setSelection(0);

        wordSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar
                .make(managingActivity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedRogerword = parent.getItemAtPosition(position).toString();
        managingActivity.saveSharedPreference("rogerword", selectedRogerword);
        showSnackbar("Selected procedure word is: `" + selectedRogerword + "`");
    }

    public void onNothingSelected(AdapterView<?> parent) {
        String defaultRogerword = parent.getItemAtPosition(0).toString();
        managingActivity.saveSharedPreference("rogerword", defaultRogerword);
        showSnackbar("Default procedure word `" + defaultRogerword + "`  saved.");
    }
}
