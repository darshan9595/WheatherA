package com.example.wheathera;

import android.widget.Toast;
import android.content.Context;
public class AlertDegreeMessage{
    private City city;
    private double temp ;
    private double wind;
    private double ressenti ;
    private double humidite ;
    private double pression;

    public AlertDegreeMessage(City city) {
        this.city = city;
        this.temp=temp;
        this.wind=wind;
    }

    public static boolean degreeCheck(double temp) {
        final double minTemp = -5; //par défault on a mis des valeurs
        final double maxTemp = 30;
            if(temp < minTemp || temp > maxTemp ) return true;
            else return false;
        }




    public static boolean windCheck(double wind) {
            if(wind > 40) return true;
            else return false;
    }


        public static boolean estAlerteTsunami(double temp, double ressenti, double humidite, double pression, double wind) {
            // Conditions pour déterminer s'il y a une alerte de tsunami
            if (temp > 30.0 && ressenti > 35.0 && humidite > 80.0 && pression < 950.0 && wind > 27.8) {
                return true; // Il y a une alerte de tsunami
            } else {
                return false; // Il n'y a pas d'alerte de tsunami
            }
        }




}
