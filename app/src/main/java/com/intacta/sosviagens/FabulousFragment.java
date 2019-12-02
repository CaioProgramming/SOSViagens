package com.intacta.sosviagens;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.intacta.sosviagens.Adapter.RecyclerAdapter;
import com.intacta.sosviagens.Beans.Road;

import java.util.ArrayList;

public class FabulousFragment extends AAH_FabulousFragment {
    private ArrayList<Road> roadlist;
    public static FabulousFragment newInstance() {
         return new FabulousFragment();
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.roads_fragment, null);
        RelativeLayout rl_content = contentView.findViewById(R.id.main);
         contentView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });

        RecyclerAdapter  recyclerAdapter= new RecyclerAdapter(getActivity(),roadlist);
        GridLayoutManager llm = new GridLayoutManager(getContext(),1,RecyclerView.VERTICAL,false);
        RecyclerView recyclerView = contentView.findViewById(R.id.roadlist);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(llm);
        //params to set
        setAnimationDuration(600); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        //setCallbacks((Callbacks) getContext()); //optional; to get back result
        //setAnimationListener((AnimationListener) getActivity()); //optional; to get animation callbacks
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

    public ArrayList<Road> getRoadlist() {
        return roadlist;
    }

    void setRoadlist(ArrayList<Road> roadlist) {
        this.roadlist = roadlist;
    }
}
