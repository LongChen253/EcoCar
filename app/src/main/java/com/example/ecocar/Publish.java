package com.example.ecocar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Publish#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Publish extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button occasional;
    private Button habitual;

    public Publish() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Publicate.
     */
    // TODO: Rename and change types and number of parameters
    public static Publish newInstance(String param1, String param2) {
        Publish fragment = new Publish();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        occasional = view.findViewById(R.id.occasionalButton);
        habitual = view.findViewById(R.id.regularButton);

        occasional.setOnClickListener(view1 -> {
            if (CurrentUser.getInstance().getCarSelected().equals("")) {
                new AlertDialog.Builder(getContext())
                        .setMessage("??Debe seleccionar un coche en tu perfil para poder realizar esta operaci??n!")
                        .setPositiveButton("De auerdo", (dialogInterface, i) -> {
                        })
                        .show();
            }
            else {
                Intent intent = new Intent(getActivity(), PV_Salir.class);
                intent.putExtra("tripType", "occasional");
                startActivity(intent);
            }
        });

        habitual.setOnClickListener(view12 -> {
            if (CurrentUser.getInstance().getCarSelected().equals("")) {
                new AlertDialog.Builder(getContext())
                        .setMessage("??Debe seleccionar un coche en tu perfil para poder realizar esta operaci??n!")
                        .setPositiveButton("De auerdo", (dialogInterface, i) -> {
                        })
                        .show();
            }
            else {
                Intent intent = new Intent(getActivity(), PV_Salir.class);
                intent.putExtra("tripType", "habitual");
                startActivity(intent);
            }
        });
    }
}