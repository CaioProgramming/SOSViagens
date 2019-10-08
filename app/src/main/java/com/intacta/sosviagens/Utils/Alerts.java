package com.intacta.sosviagens.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.intacta.sosviagens.Adapter.RecyclerMoreThenOneAdapter;
import com.intacta.sosviagens.Beans.Comment;
import com.intacta.sosviagens.Beans.Road;
import com.intacta.sosviagens.Beans.Suggest;
import com.intacta.sosviagens.Database.DBUtilities;
import com.intacta.sosviagens.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hyogeun.github.com.colorratingbarlib.ColorRatingBar;

import static com.intacta.sosviagens.Utils.Utilities.RC_SIGN_IN;

public class Alerts {
    private Activity activity;

    private int theme;
    private TextView title;
    private ProgressBar loading;
    private LinearLayout ratinglayout;
    private ColorRatingBar rating;
    private TextInputLayout commentlayout;
    private EditText comment;
    private TextInputLayout cortesylayout;
    private EditText cortesy;
    private EditText time;
    private Button save;
    private LinearLayout timelayout;
    private Spinner timetype;


    public Alerts(Activity activity, int theme) {
        this.activity = activity;
        this.theme = theme;
    }

    private boolean checkuser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }


    public void noConcess(final String address) {
        Query reference = FirebaseDatabase.getInstance().getReference("suggestion")
                .orderByChild("rodovia").equalTo(address);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists() && !address.contains("Rua") || !address.contains("rua") ){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Esse endereço " + address + " não está no nosso sistema, caso você acredite que deveria estar, aperte Ok")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBUtilities dbUtilities = new DBUtilities(activity);
                                    Suggest suggest = new Suggest();
                                    suggest.setRodovia(address);
                                    suggest.setDia(getDia());
                                    dbUtilities.SendSuggest(suggest);
                                }
                            })
                            .setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.black));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, theme);
        builder.setMessage("Seu GPS está desabilitado, ative-o para que possamos ajudar")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void CommentAlert(final Road road) {
        final Animation out = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_out_left);
        final Animation in = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_in_right);
        if (checkuser()) {
            final BottomSheetDialog myDialog = new BottomSheetDialog(Objects.requireNonNull(activity), R.style.Dialog_No_Border);
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myDialog.setContentView(R.layout.comment_dialog);
            initView(myDialog);
            if (!myDialog.isShowing()) {
                myDialog.show();
            }
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Salvar(comment,cortesy,time,rating,road,myDialog);
                }
            });

        } else {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            activity.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN);
        }


    }

    private void Salvar(EditText comment, EditText cortesy, EditText time, ColorRatingBar rating, Road road, BottomSheetDialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getDisplayName();
        String comnt = comment.getText().toString();
        String oppinion = String.valueOf(rating.getRating());
        String tempo = time.getText().toString() + timetype.getSelectedItem().toString();
        String dia = getDia();
        Comment cb = new Comment(comnt, username, dia, cortesy.getText().toString(),tempo, oppinion,road.getId());
        cb.setRodovia(road.getRodovia());
        cb.setConcessionaria(road.getConcessionaria());
        DBUtilities commentsDB = new DBUtilities(activity);
        commentsDB.SendComment(cb);
        dialog.dismiss();
    }


    public void MoreConcess(final ArrayList<Road> roads) {
        final ArrayList<Road> options = new ArrayList<>(roads);
        final BottomSheetDialog myDialog = new BottomSheetDialog(Objects.requireNonNull(activity), R.style.Dialog_No_Border);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.morethenonelayout);
        if (myDialog.isShowing()) {
            System.out.println("Alerta sendo exibido");
            return;

        }
        RecyclerView roadssuggestion = myDialog.findViewById(R.id.roads);

        Button cancelar = myDialog.findViewById(R.id.cancel);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        if (roadssuggestion.getChildCount() > 0) {
            myDialog.show();
            return;
        }
        RecyclerMoreThenOneAdapter recyclerMoreThenOneAdapter = new RecyclerMoreThenOneAdapter(activity, options);
        GridLayoutManager llm = new GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false);

        roadssuggestion.setAdapter(recyclerMoreThenOneAdapter);
        roadssuggestion.setLayoutManager(llm);

        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                options.clear();
            }
        });
        myDialog.show();

    }


    private String getDia() {
        Date datenow = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(datenow);
    }


    private void initView(BottomSheetDialog myDialog) {
        title = myDialog.findViewById(R.id.title);
        loading = myDialog.findViewById(R.id.loading);
        ratinglayout = myDialog.findViewById(R.id.ratinglayout);
        rating = myDialog.findViewById(R.id.rating);
        commentlayout = myDialog.findViewById(R.id.commentlayout);
        comment = myDialog.findViewById(R.id.comment);
        cortesylayout = myDialog.findViewById(R.id.cortesylayout);
        cortesy = myDialog.findViewById(R.id.cortesy);
        time = myDialog.findViewById(R.id.time);
        save = myDialog.findViewById(R.id.save);
        timelayout = myDialog.findViewById(R.id.timelayout);
        timetype = myDialog.findViewById(R.id.timetype);
    }
}



