package com.intacta.sosviagens.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

 import com.google.android.material.tabs.TabLayout;
import com.intacta.sosviagens.Home;
import com.intacta.sosviagens.R;
import com.intacta.sosviagens.Utils.PermissionRequests;
import com.intacta.sosviagens.Utils.Preferences;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;

    private Dialog m_dialog;

    private String titles[]={"Bem-vindo","Antes de começar...","Tudo pronto"},
            descriptions[] = {"Bem-vindo ao SOSRodovias, " +
                    "Estamos aqui para tornar sua viagem mais segura!",
                    "Asseguramos sua privacidade, todos os dados utilizados no app serão apenas para ajudá-lo em seu resgate, não se preocupe",
                    "Ao prosseguir você estará concordando com nossas políticas de uso e privacidade"};

    public ViewPagerAdapter(Activity activity) {
        this.activity = activity;

    }


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Animation fadein = AnimationUtils.loadAnimation(activity,R.anim.fade_in);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pager_layout, container, false);
         TextView description = view.findViewById(R.id.description);
        TextView title = view.findViewById(R.id.title);
        CardView card = view.findViewById(R.id.card);
        RelativeLayout background = view.findViewById(R.id.background);
        TextView privacypolicy = view.findViewById(R.id.privacy_policy);


        title.setText(titles[position]);
        description.setText(descriptions[position]);
        final Animation in = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top);

        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_dialog = new Dialog(activity, R.style.Dialog_No_Border);
                LayoutInflater m_inflater = LayoutInflater.from(activity);
                View m_view = m_inflater.inflate(R.layout.dialog, null);
                m_dialog.setContentView(m_view);
                Button agreebutton = m_view.findViewById(R.id.agreebutton);
                agreebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        m_dialog.dismiss();
                        start_app();
                    }
                });
                m_dialog.show();
                m_view.startAnimation(in);

            }
        });






        container.addView(view);
        title.startAnimation(in);
        card.startAnimation(fadein);
         return view;

    }

    private void start_app() {
        int ALL_PERMISSIONS = 101;

        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE};

        ActivityCompat.requestPermissions(activity, permissions, ALL_PERMISSIONS);
        Preferences preferences = new Preferences(activity);
        preferences.setAgree(true);
        final Intent i = new Intent(activity, Home.class);
        if (PermissionRequests.hasPermissions(activity,permissions)) {
            activity.startActivity(i);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
