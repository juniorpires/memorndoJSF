/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpe.memorando.util;

import java.util.Calendar;

/**
 *
 * @author casa01
 */
public class DateUtil {
    
    
    public static String getCurrentYear(){
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);
        return String.valueOf(year);
    }
}
