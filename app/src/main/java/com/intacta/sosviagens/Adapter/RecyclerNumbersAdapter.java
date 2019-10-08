package com.intacta.sosviagens.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

 import com.intacta.sosviagens.Beans.Number;
import com.intacta.sosviagens.R;
import com.intacta.sosviagens.Utils.Utilities;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.rmiri.skeleton.SkeletonGroup;

public class RecyclerNumbersAdapter extends RecyclerView.Adapter<RecyclerNumbersAdapter.MyViewHolder> {
    private Activity mActivity;
    private ArrayList<Number> numberlist;


    public RecyclerNumbersAdapter(Activity mActivity, ArrayList<Number> numberlist) {
        this.mActivity = mActivity;
        this.numberlist = numberlist;
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
    public RecyclerNumbersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mActivity.getBaseContext());
        view = mInflater.inflate(R.layout.roadsuggest,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerNumbersAdapter.MyViewHolder holder, int position) {
        CountDownTimer timer = new CountDownTimer(500,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                holder.skeletonGroup.setShowSkeleton(false);
                holder.skeletonGroup.finishAnimation();
            }
        }.start();
        final Number n = numberlist.get(position);
        Animation animation = null;
        if (!n.getIdent().equals(Utilities.calltitle)) {
            animation = AnimationUtils.loadAnimation(mActivity,R.anim.fade_in);
            holder.road.setText(n.getIdent());
            holder.concess.setText(n.getTelefone());
            System.out.println(n.getTipo());

            if (n.getTipo().equals("Seguradora")){
                holder.type.setImageDrawable(mActivity.getDrawable(R.drawable.ic_shield));
           }else if(n.getTipo().equals("Órgão regulador")){
                holder.type.setImageDrawable(mActivity.getDrawable(R.drawable.ic_university_with_a_flag));
           }else{
                holder.type.setImageDrawable(mActivity.getDrawable(R.drawable.ic_plus));

            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ligacao(n.getTelefone());
                }
            });
            holder.layout.startAnimation(animation);


        /*holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligacao(n.getTelefone());

            }
        }); */
        }else{
            holder.road.setText(n.getIdent());
            holder.concess.setVisibility(View.GONE);
        }


    }

    private void ligacao(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
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
            dialntent.setData(Uri.parse("tel:" + Uri.encode(number.trim())));
            dialntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(dialntent);
            return;
        }
         mActivity.startActivity(callIntent);
    }

    @Override
    public int getItemCount() {
        if(numberlist.size() == 0){
            return 0;

        }else{
            return numberlist.size();}
    }

    static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView road,concess;
        ImageView type;
         RelativeLayout layout;
        SkeletonGroup skeletonGroup;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            road = itemView.findViewById(R.id.road);
            concess = itemView.findViewById(R.id.concess);
             layout = itemView.findViewById(R.id.layout);
            skeletonGroup = itemView.findViewById(R.id.skeleton_group);
            type = itemView.findViewById(R.id.type);

        }
    }
}
