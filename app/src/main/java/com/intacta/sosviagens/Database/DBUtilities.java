package com.intacta.sosviagens.Database;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intacta.sosviagens.Beans.Comment;
import com.intacta.sosviagens.Beans.Road;
import com.intacta.sosviagens.Beans.Suggest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import de.mateware.snacky.Snacky;

public class DBUtilities {

    private Activity activity;


    public DBUtilities(@NonNull Activity activity) {
        this.activity = activity;

    }

    public void SendComment(Comment comment){
        DatabaseReference commentsReference= FirebaseDatabase.getInstance().getReference("comments");;
        commentsReference.push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){Snacky.builder().setActivity(activity).success().setText("Comentário enviado com sucesso!").show();}
           }
       });

    }

    public void reportcall(Road road){
        Date datenow = Calendar.getInstance().getTime();
        FirebaseDatabase.getInstance().getReference("rodovias").child(road.getId())
                .child("calls").push().setValue(datenow.toString());
    }


    public void SendSuggest(Suggest suggest){
        DatabaseReference suggestions = FirebaseDatabase.getInstance().getReference("suggestion");
        suggestions.push().setValue(suggest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){Snacky.builder().setActivity(activity).success().setText("Sugestão enviada com sucesso!").show();} }
        });
    }
}
