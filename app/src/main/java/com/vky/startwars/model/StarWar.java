package com.vky.startwars.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * Created by Praveena on 15-10-2016.
 */

public class StarWar {

    public List<Results> results;
    public String count;
    public String next;

    public class Results implements Comparable<Results> {
        public String[] films;
        public String name;
        public String cost_in_credits;
        public String title;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compareTo(Results results) {
            return Long.parseLong(cost_in_credits) < Long.parseLong(results.cost_in_credits) ? 1 : -1;
        }
    }
    public class Film {
        public String title;
    }
}
