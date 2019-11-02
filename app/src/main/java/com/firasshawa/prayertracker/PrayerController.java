package com.firasshawa.prayertracker;

import com.firasshawa.prayertracker.R;

import android.graphics.drawable.Drawable;
import android.text.style.BackgroundColorSpan;
import android.view.View;

import com.firasshawa.prayertracker.Models.Prayer;

import java.util.ArrayList;
import java.util.List;


public class PrayerController {

    private Prayer prayer ;

    private ArrayList<String> Names = new ArrayList<String>(){
        {
            add("Fajer");
            add("Duhher");
            add("Asser");
            add("Mogreb");
            add("Esha");
        }
    };

    private ArrayList<Integer> Backgrounds = new ArrayList<Integer>(){
        {
            add(R.drawable.fajer_gradient);
            add(R.drawable.duhher_gradient);
            add(R.drawable.asser_gradient);
            add(R.drawable.mogreb_gradient);
            add(R.drawable.esha_gradient);
        }
    };

    public static ArrayList<Prayer> NewDayPrayer(String Date){
        ArrayList<Prayer> Prayers = new ArrayList<>();

        Prayers.add(new Prayer("Fajer",Date));
        Prayers.add(new Prayer("Duher",Date));
        Prayers.add(new Prayer("Aser",Date));
        Prayers.add(new Prayer("Mogreb",Date));
        Prayers.add(new Prayer("Esha",Date));

        return Prayers;
    }

}
