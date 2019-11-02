package com.firasshawa.prayertracker.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firasshawa.prayertracker.Adapters.PrayerAdapterRV;
import com.firasshawa.prayertracker.Models.Prayer;
import com.firasshawa.prayertracker.Models.Prayers;
import com.firasshawa.prayertracker.Models.User;
import com.firasshawa.prayertracker.PrayerController;
import com.firasshawa.prayertracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Home extends AppCompatActivity  implements View.OnClickListener, View.OnLongClickListener {

    TextView DateTime_tv , Name_tv;
    CardView Fajer,Duhher,Asser,Mograb,Esha;

    ArrayList<Prayer> CurrentPrayerData;
    ArrayList<CardView> CardViewList;
    SimpleDateFormat DateFormatter ;
    SimpleDateFormat TimeFormatter ;
    Date date ;

    Intent lastActivity;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    DatabaseReference UserRef ;

    RecyclerView recyclerView ;
    PrayerAdapterRV adapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getSharedPreferences("SharedPref",getApplicationContext().MODE_PRIVATE);
        editor = preferences.edit();

        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        lastActivity = getIntent();

        date = new Date();
        DateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        TimeFormatter = new SimpleDateFormat("HH:mm:ss");
        DateTime_tv = findViewById(R.id.DateTime_tv);
        Name_tv = findViewById(R.id.Name_tv);
        Fajer = findViewById(R.id.cardView_fajer);
        Duhher = findViewById(R.id.cardView_duhher);
        Asser = findViewById(R.id.cardView_asser);
        Mograb = findViewById(R.id.cardView_mogreb);
        Esha = findViewById(R.id.cardView_esha);

        CurrentPrayerData = new ArrayList<>();
        CardViewList = new ArrayList<CardView>(){
            {
                add(Fajer);
                add(Duhher);
                add(Asser);
                add(Mograb);
                add(Esha);
            }
        };

        setupOnclicks();

//        recyclerView = findViewById(R.id.PrayerList);
//        adapterRV = new PrayerAdapterRV();
//        recyclerView.setAdapter(adapterRV);

        date = new Date();
        DateTime_tv.setText(DateFormatter.format(date));

        GetUsersData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart Method Start");
        NewDay();
        System.out.println("onStart Method Finished");
    }

    void ShowToast(String name){
        Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();
    }

    ValueEventListener getUserEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildren() != null){
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    User user = data.getValue(User.class);
                    Name_tv.setText(user.getName());
                    System.out.println(user);
                }
            }else{
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onClick(View view) {
        CardView cardView = (CardView) view;

        LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);

        //new Date Object
        date = new Date();

        //the User Key from the shared preferences
        final String userKey = preferences.getString("PrayerUserKey","");

        //the Prayer child String
        final String Prayers = "Prayers";

        //Create Date Key with the current Date
        final String Datekey = DateFormatter.format(date).toString();

        //get Prayer index
        int index = -1;
        switch (getResources().getResourceEntryName(linearLayout.getId())){
            case "LinearLayoutFajer" :
                index = 0 ;
                break;

            case "LinearLayoutDuhher" :
                index = 1 ;
                break;

            case "LinearLayoutAsser" :
                index = 2 ;
                break;

            case "LinearLayoutMogreb" :
                index = 3 ;
                break;

            case "LinearLayoutEsha" :
                index = 4 ;
                break;
        }

        Prayer prayer = CurrentPrayerData.get(index);
        prayer.setState(!prayer.isState());
        prayer.setTime(TimeFormatter.format(date));
        UserRef.child(userKey).child(Prayers).child(Datekey).child(index+"").setValue(CurrentPrayerData.get(index));
//        NewDay();
    }

    @Override
    public boolean onLongClick(View view) {
        CardView cardView = (CardView) view;

        LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);

        Drawable drawable = linearLayout.getBackground();

        linearLayout.setBackgroundResource(R.drawable.disable_gradient);
        return true;
    }

    public void setupOnclicks(){
        Fajer.setOnClickListener(this);
        Duhher.setOnClickListener(this);
        Asser.setOnClickListener(this);
        Mograb.setOnClickListener(this);
        Esha.setOnClickListener(this);

        Fajer.setOnLongClickListener(this);
        Duhher.setOnLongClickListener(this);
        Asser.setOnLongClickListener(this);
        Mograb.setOnLongClickListener(this);
        Esha.setOnLongClickListener(this);
    }

    public void GetUsersData(){
        //Toast.makeText(getApplicationContext(), preferences.getString("PrayerUserKey",""), Toast.LENGTH_SHORT).show();
        if(preferences.getAll().containsKey("PrayerUserKey")){
            Query query = UserRef.orderByChild("key").equalTo(preferences.getString("PrayerUserKey","")).limitToFirst(1);
            query.addValueEventListener(getUserEvent);
        }else{
            Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();

        }
    }

    public void NewDay(){
        //new Date Object
        date = new Date();

        //the User Key from the shared preferences
        final String userKey = preferences.getString("PrayerUserKey","");

        //the Prayer child String
        final String Prayers = "Prayers";

        //Create Date Key with the current Date
        final String Datekey = DateFormatter.format(date).toString();

        UserRef
                .child(userKey)//get the user Node Tree
                .child(Prayers)//get the Prayer Node
                .orderByKey()//Order The Prayer Node Children By the Key
                .equalTo(Datekey)//get the children with key equals to the DateKey(Current Date)
                .limitToFirst(1)//get the first One Only!
                .addValueEventListener(new ValueEventListener() {
                   @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot.exists());

                        //check if the current date node is exists
                        if(dataSnapshot.exists()){
                            //get all the children from the data snapshot
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                //the content of the CurrentDate Node is A Arraylist of Prayer

                                GenericTypeIndicator<ArrayList<Prayer>> ListPray = new GenericTypeIndicator<ArrayList<Prayer>>() {};
                                ArrayList<Prayer> prayers =  snapshot.getValue(ListPray);
                                //Iterate on them and print the Prayers that in the arraylist!
                                for (Prayer prayer : prayers){
                                    System.out.println(prayer);
                                }
                                setUp(prayers);
                            }
                        }else{
                            Toast.makeText(Home.this, "No such data from the datasnapshot is exixt", Toast.LENGTH_SHORT).show();
                            UserRef.child(userKey).child(Prayers).child(Datekey).setValue(PrayerController.NewDayPrayer(DateFormatter.format(date)));
                            Toast.makeText(Home.this, "New Child is Created !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Home.this, "something is wrong!", Toast.LENGTH_SHORT).show();
                    }
          });
        System.out.println("done!");
    }

    public void BuildCard(View view,Prayer prayer){
        System.out.println("BuildCard Method");
        CardView cardView =(CardView) view;
        LinearLayout linearLayout =(LinearLayout) cardView.getChildAt(0);
        if(prayer.isState()){
            BackgroundSwitch(linearLayout);
        }else{
            linearLayout.setBackgroundResource(R.drawable.disable_gradient);
        }
        cardView.setVisibility(View.VISIBLE);
    }

    public void BackgroundSwitch(LinearLayout linearLayout){
        switch (getResources().getResourceEntryName(linearLayout.getId())){
            case "LinearLayoutFajer" :
                linearLayout.setBackgroundResource(R.drawable.fajer_gradient);

                break;

            case "LinearLayoutDuhher" :
                linearLayout.setBackgroundResource(R.drawable.duhher_gradient);

                break;

            case "LinearLayoutAsser" :
                linearLayout.setBackgroundResource(R.drawable.asser_gradient);

                break;

            case "LinearLayoutMogreb" :
                linearLayout.setBackgroundResource(R.drawable.mogreb_gradient);

                break;

            case "LinearLayoutEsha" :
                linearLayout.setBackgroundResource(R.drawable.esha_gradient);

                break;

            default:
                linearLayout.setBackgroundResource(R.drawable.disable_gradient);
                break;
        }
    }


    public void setUp(ArrayList<Prayer> prayers){
        System.out.println("SetUp Method");
        System.out.println(prayers.size());
        for(int index = 0 ; index < prayers.size() ; index++){
            System.out.println(prayers.get(index));
            BuildCard(CardViewList.get(index),prayers.get(index));
        }
        CurrentPrayerData = prayers;
    }
}
