package com.intacta.sosviagens.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.intacta.sosviagens.Beans.Road;
import com.intacta.sosviagens.Database.DBUtilities;
import com.intacta.sosviagens.R;

import java.util.ArrayList;

import io.rmiri.skeleton.SkeletonGroup;

import static com.intacta.sosviagens.Home.lastcall;
import static com.intacta.sosviagens.Utils.Utilities.called;

public class RecyclerMoreThenOneAdapter extends RecyclerView.Adapter<RecyclerMoreThenOneAdapter.MyViewHolder> {
    private Activity mActivity;
    private ArrayList<Road> roadlist;

    public RecyclerMoreThenOneAdapter(Activity mActivity, ArrayList<Road> roadList) {
        this.mActivity = mActivity;
        this.roadlist = roadList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public RecyclerMoreThenOneAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mActivity.getBaseContext());
        view = mInflater.inflate(R.layout.roadcardlayout,parent,false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerMoreThenOneAdapter.MyViewHolder holder, final int position) {
        final Road r = roadlist.get(position);
        Animation animation = AnimationUtils.loadAnimation(mActivity,R.anim.fade_in);
        if (!r.getRodovia().equals("Rodovia já cadastrada")) {
            holder.concess.setVisibility(View.GONE);
            holder.concess.setTextSize(24);
            holder.layout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.concess.setText(r.getConcessionaria() + "(" + r.getTelefone() + ")");
            holder.layout.startAnimation(animation);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ligacao(roadlist.get(position));
                }
            });
        }
        if (position == roadlist.size()){
            holder.layout.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        }






    }

    private void ligacao(Road road) {
        DBUtilities dbUtilities = new DBUtilities(mActivity);
        dbUtilities.reportcall(road);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(road.getTelefone())));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Intent dialntent = new Intent(Intent.ACTION_DIAL);
            dialntent.setData(Uri.parse("tel:" + Uri.encode(road.getTelefone())));
            dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(dialntent);
            return;
        }
         called = true;
        lastcall = road;
        mActivity.startActivity(callIntent);

    }

    @Override
    public int getItemCount() {
        if(roadlist.size() == 0){
            return 0;

        }else{
            return roadlist.size();}
    }

    static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView concess;
         RelativeLayout layout;
         MyViewHolder(@NonNull View itemView) {
            super(itemView);
             concess = itemView.findViewById(R.id.concess);
             layout = itemView.findViewById(R.id.layout);

        }
    }
}
