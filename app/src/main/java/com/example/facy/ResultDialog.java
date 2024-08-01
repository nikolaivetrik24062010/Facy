package com.example.facy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultDialog extends DialogFragment {
    Button btn;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_result_dialog, container, false);
        String resultText = "";
        btn = view.findViewById(R.id.okButton);
        textView = view.findViewById(R.id.dialog);
        // getting arguments from bundle
        Bundle bundle = getArguments();
        resultText = bundle.getString("RESULT_TEXT");
        textView.setText(resultText);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
