package com.example.developer.databaseuber02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Developer on 17/2/2017.
 */
public class databaseOps {
    private Long number;
    private String name="default";
    private DatabaseReference myRefInput;
    private Context contextMA;
    private Boolean search=false;
    private DatabaseReference userReference;



    public databaseOps(Long number, String name, DatabaseReference myRefInput, Context contextMA,DatabaseReference userReference) {
        this.number = number;
        this.name = name;
        this.myRefInput = myRefInput;
        this.contextMA = contextMA;
        this.userReference=userReference;

    }

    public databaseOps(Long number, DatabaseReference myRefInput, Context contextMA, Boolean search, DatabaseReference userReference) {
        this.number = number;
        this.myRefInput = myRefInput;
        this.contextMA = contextMA;
        this.search=search;
        this.userReference=userReference;

    }




    public void orderNumber() {

        Toast.makeText(contextMA, "Buscando.."+search, Toast.LENGTH_SHORT).show();

        myRefInput.orderByChild("number").equalTo(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Toast.makeText(contextMA, "NoNull.."+search, Toast.LENGTH_SHORT).show();
                    if (search){
                        Toast.makeText(contextMA,"MostrarUsuario TODO",Toast.LENGTH_SHORT).show();//TODO
                    }else{
                        Toast.makeText(contextMA, "Usuario ya creado" + number, Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(contextMA, "Null"+search, Toast.LENGTH_SHORT).show();
                    if (search){
                        //TODO  crearUsuario?
                        AlertDialog.Builder alert=new AlertDialog.Builder(contextMA);
                        alert.setTitle("Añadir").setMessage("¿Desea Añadir este Usuario?").setPositiveButton(
                                "Añadir", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addUser();
                                    }

                                }).setNegativeButton("Cancelar", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();


                    }else{

                        Toast.makeText(contextMA, "Creando Usuario..." + number, Toast.LENGTH_SHORT).show();
                        addUser();
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}


    public void addUser() {
        String key = myRefInput.push().getKey();
        User user = new User(number, name);
        Map<String, Object> userValues = new HashMap<>();
        userValues.put(key, user);
        myRefInput.updateChildren(userValues);
        userReference.updateChildren(userValues);


    }


    public void add1000Users() {

        for (Long i = 0L; i <= 10000L; i++) {
            String key = myRefInput.push().getKey();
            User user = new User(i,"prueba");
            Map<String,Object> userValues=new HashMap<>();
            userValues.put(key,user);
            myRefInput.updateChildren(userValues);
        }
    }
    public void cleanDB(){
        myRefInput.setValue("");
    }
}
