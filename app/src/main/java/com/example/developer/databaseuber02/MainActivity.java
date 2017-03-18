package com.example.developer.databaseuber02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private FirebaseDatabase database;
    private String child = "Numbers";
    private String mUsername = "Default";
    private String inputNumberFB;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference userReference;
    private ChildEventListener mChildEventListener;
    private userAdapter mUserAdapter;
    private List<User> usersList;
    private String A="alto";

    private DatabaseReference myRefInput;


    final Context c = this;
    private Long numberInput;
    private String nameInput;
    /*
    debug TODO NUNCA añadir otro parametro a la FIREBASE MANUAlMENTE!
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUsername = ANONYMOUS;
        ListView userListView = (ListView) findViewById(R.id.listViewMain);
        mFirebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();//SetPE funcionalidades offline
        myRefInput = database.getReference("Strobe").child("Numbers");
        final User prueba = new User(125L, "andres");

        usersList = new ArrayList<>();
        usersList.add(prueba);

        mUserAdapter = new userAdapter(this, R.layout.user_list_item, usersList);


        userListView.setAdapter(mUserAdapter);
        /*
        Debug TODO limpiar codigo, añadir usuarios creados por el user;
         */


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutEnterNumber = LayoutInflater.from(MainActivity.this);
                View mView = layoutEnterNumber.inflate(R.layout.user_number_input, null);
                AlertDialog.Builder alertDialogNumberInput = new AlertDialog.Builder(c);
                alertDialogNumberInput.setView(mView);
                final EditText userInputDialogEditNumber = (EditText) mView.findViewById(R.id.editNumber);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.editName);
                alertDialogNumberInput.setCancelable(true).setPositiveButton(R.string.send, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        inputNumberFB = userInputDialogEditNumber.getText().toString();
                        nameInput = userInputDialogEditText.getText().toString();


                        try {
                            numberInput = Long.parseLong(inputNumberFB);
                            databaseOps ops = new databaseOps(numberInput, nameInput, myRefInput, getApplicationContext(), userReference);
                            ops.orderNumber();
                            userInputDialogEditNumber.setText("");
                            userInputDialogEditText.setText("");

                        } catch (NumberFormatException NFE) {
                            Toast.makeText(c, "Numero no valido", Toast.LENGTH_LONG).show();
                        }


                        Log.v(MainActivity.class.getSimpleName(), userInputDialogEditNumber.getText().toString());


                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                AlertDialog alert = alertDialogNumberInput.create();
                alert.show();

            }
        });
        FloatingActionButton FAB2 = (FloatingActionButton) findViewById(R.id.fab2);
        FAB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsername != null) {
                    Toast.makeText(c, "UserID" + mUsername, Toast.LENGTH_SHORT).show();
                    //   addItemsList(prueba);
                    usersList.clear();
                    mUserAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(c, "UserIDNUll", Toast.LENGTH_SHORT).show();
                }


            }
        });


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(MainActivity.class.getSimpleName(), "Signed in" + user.getUid());
                    setUserID(user.getUid());
                    userReference = database.getReference("User").child(mUsername);
                    attachDatabaseReadListener();


                } else {
                    startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().setIsSmartLockEnabled(false).setProviders(AuthUI.EMAIL_PROVIDER, AuthUI.GOOGLE_PROVIDER).build(), RC_SIGN_IN);
                }


            }
        };


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                //noinspection SimplifiableIfStatement

                Toast.makeText(this, "SearchButton Pressed", Toast.LENGTH_SHORT).show();

                LayoutInflater layoutEnternumber = LayoutInflater.from(MainActivity.this);
                View mView = layoutEnternumber.inflate(R.layout.search_input, null);
                AlertDialog.Builder alertDialogSearch = new AlertDialog.Builder(MainActivity.this);
                final EditText numberInputEditText = (EditText) mView.findViewById(R.id.search_input);
                alertDialogSearch.setView(mView);

                alertDialogSearch.setPositiveButton("Buscar", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            numberInput = Long.parseLong(numberInputEditText.getText().toString());
                            databaseOps Search = new databaseOps(numberInput, myRefInput, MainActivity.this, true, userReference);
                            Search.orderNumber();

                        } catch (NumberFormatException NFE) {
                            Toast.makeText(c, "Numero invalido", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog aDialog = alertDialogSearch.create();
                aDialog.show();


                return true;
            case R.id.menu_item:
                AuthUI.getInstance().signOut(this);
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

/*
    private void addValueEventListener(final DatabaseReference myRef) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<User>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();//A DataSnapshot instance contains data from a Firebase Database location. Any time you read Database data, you receive the data as a DataSnapshot.
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    User value = iterator.next().getValue(User.class);
                    userList.add(value);
                }
                //Todo Make THE ADAPTER


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK)
                Toast.makeText(c, "Hello!", Toast.LENGTH_SHORT).show();
            else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
//           detachDatabaseReadListener();
    }

    private void setUserID(String userID) {
        mUsername = userID;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {


            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    try {
                        addItemsList(user);
                    } catch (NullPointerException pe) {

                    }


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    for (int i = 0; i <= usersList.size()-1; i++){
                        try{
                            if(user.getNumber().equals(usersList.get(i).getNumber())){
                                Toast.makeText(c, "Removing...", Toast.LENGTH_SHORT).show();
                                usersList.remove(i);
                                mUserAdapter.notifyDataSetChanged();
                                //(user.getNumber().equals(mUserAdapter.getItem(i).getNumber()))

                            }}
                        catch (NullPointerException npe){
                            Log.e(MainActivity.class.getSimpleName(),"NPE");
                        }

                    }



                    Toast.makeText(c, "onChildRemoved", Toast.LENGTH_SHORT).show();



                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            userReference.addChildEventListener(mChildEventListener);
        }

    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            userReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void addItemsList(User user) {
        usersList.add(user);
        mUserAdapter.notifyDataSetChanged();
    }

}