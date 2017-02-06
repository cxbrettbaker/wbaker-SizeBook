package ca.ualberta.cs.sizebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Brett on 2017-02-06.
 */
public class EntryData {


    private String name;
    private String date; //yyyy-mm-dd
    private Double neck = 0.0;
    private Double bust = 0.0;
    private Double chest = 0.0;
    private Double waist = 0.0;
    private Double hip = 0.0;
    private Double inseam = 0.0;
    private String comment;



    //    public EntryScreen(EditText commentText, EditText inseamText, EditText hipText, EditText waistText, EditText chestText, EditText bustText, EditText neckText, EditText dateText, EditText nameText) {
//        this.commentText = (EditText) findViewById(R.id.comment);
//        this.inseamText = (EditText) findViewById(R.id.inseam);;
//        this.hipText = (EditText) findViewById(R.id.hip);
//        this.waistText = (EditText) findViewById(R.id.waist);
//        this.chestText = (EditText) findViewById(R.id.chest);
//        this.bustText = (EditText) findViewById(R.id.bust);
//        this.neckText = (EditText) findViewById(R.id.neck);
//        this.dateText = (EditText) findViewById(R.id.date);
//        this.nameText = (EditText) findViewById(R.id.name);
//    }

    /**
     * Instantiates a new Entry data.
     *
     * @param name the name
     */
    public EntryData(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     * @throws InputDateException the input date exception
     */
    public void setDate(String date) throws InputDateException {

        try {
            if (date.length() != 0) {
                String[] parts;
                if (date.contains("-")) {
                    parts = date.split("-");
                }else{
                    throw new InputDateException();
                }
                //yyyy-mm-dd
                Integer year = Integer.parseInt(parts[0]);
                Integer month = Integer.parseInt(parts[1]);
                Integer day = Integer.parseInt(parts[2]);
                if ((year < 1000) || (year > 9999)) {
                    throw new InputDateException();
                }
                if ((month < 1) || (month > 12)) {
                    throw new InputDateException();
                }


                if (month == 2) {//feb
                    if ((day < 1) || (day > 29)) {
                        throw new InputDateException();
                    }
                } else if (month % 2 == 0) { //even
                    if ((day < 1) || (day > 30)) {
                        throw new InputDateException();
                    }
                } else if (month % 2 == 1) { //odd
                    if ((day < 1) || (day > 31)) {
                        throw new InputDateException();
                    }
                }

                this.date = date;

            }


        }catch (NullPointerException e){
            throw new InputDateException();
        }catch (NumberFormatException e){
            throw new InputDateException();

        }
    }

    /**
     * Gets neck.
     *
     * @return the neck
     */
    public Double getNeck() {
        return neck;
    }

    /**
     * Sets neck.
     *
     * @param neckStr the neck str
     * @throws InputException the input exception
     */
    public void setNeck(String neckStr) throws InputException {
        this.neck = parseString(neckStr);
    }

    /**
     * Gets bust.
     *
     * @return the bust
     */
    public Double getBust() {
        return bust;
    }

    /**
     * Sets bust.
     *
     * @param bustStr the bust str
     * @throws InputException the input exception
     */
    public void setBust(String bustStr) throws InputException {
        this.bust = parseString(bustStr);
    }

    /**
     * Gets chest.
     *
     * @return the chest
     */
    public Double getChest() {
        return chest;
    }

    /**
     * Sets chest.
     *
     * @param chestStr the chest str
     * @throws InputException the input exception
     */
    public void setChest(String chestStr) throws InputException {
        this.chest = parseString(chestStr);
    }

    /**
     * Gets waist.
     *
     * @return the waist
     */
    public Double getWaist() {
        return waist;
    }

    /**
     * Sets waist.
     *
     * @param waistStr the waist str
     * @throws InputException the input exception
     */
    public void setWaist(String waistStr) throws InputException {
        this.waist = parseString(waistStr);
    }

    /**
     * Gets hip.
     *
     * @return the hip
     */
    public Double getHip() {
        return hip;
    }

    /**
     * Sets hip.
     *
     * @param hipStr the hip str
     * @throws InputException the input exception
     */
    public void setHip(String hipStr) throws InputException {
        this.hip = parseString(hipStr);
    }

    /**
     * Gets inseam.
     *
     * @return the inseam
     */
    public Double getInseam() {
        return inseam;
    }

    /**
     * Sets inseam.
     *
     * @param inseamStr the inseam str
     * @throws InputException the input exception
     */
    public void setInseam(String inseamStr) throws InputException {
        this.inseam = parseString(inseamStr);
    }

    /**
     * Gets comment.
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets comment.
     *
     * @param comment the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Parse string double.
     *
     * @param parseStr the parse str
     * @return the double
     * @throws InputException the input exception
     */
    public Double parseString(String parseStr) throws InputException{
        Double returnNum = 0.0;
        try {
            if (parseStr.length() != 0) {
                Double parseNum = Double.parseDouble(parseStr);
                if (parseNum < 0) {
                    try {
                        throw new InputException();
                    } catch (InputException e) {
                        e.printStackTrace();
                    }
                }
                returnNum = (double) Math.round(parseNum * 10d) / 10d;
            }
        } catch (NumberFormatException e) {
            try {
                throw new InputException();
            } catch (InputException e1) {
                e1.printStackTrace();
            }
        }
        return returnNum;
    }

    @Override
    public String toString() {
        String outStr = "Name: "+this.name;
        if (this.date!= null){
            outStr += "\nDate: "+this.date;
        }
        if (this.neck > 0){
            outStr += "\nNeck: "+this.neck;
        }
        if (this.bust > 0){
            outStr += "\nBust: "+this.bust;
        }
        if (this.chest > 0){
            outStr += "\nChest: "+this.chest;
        }
        if (this.waist > 0){
            outStr += "\nWaist: "+this.waist;
        }
        if (this.inseam > 0){
            outStr += "\nInseam: "+this.inseam;
        }
        if (this.hip > 0){
            outStr += "\nHip: "+this.hip;
        }
        if (this.comment != "") {
            outStr += "\nComment: " + this.comment;
        }
        return outStr;
    }


    /**
     * To bundle bundle.
     *
     * @return the bundle
     */
    public Bundle toBundle(){

        Bundle bundle = new Bundle();
        bundle.putString("current_name",this.getName());
        bundle.putString("current_date",this.getDate());
        bundle.putString("current_neck",String.valueOf(this.getNeck()));
        bundle.putString("current_bust",String.valueOf(this.getBust()));
        bundle.putString("current_chest",String.valueOf(this.getChest()));
        bundle.putString("current_waist",String.valueOf(this.getWaist()));
        bundle.putString("current_hip",String.valueOf(this.getHip()));
        bundle.putString("current_inseam",String.valueOf(this.getInseam()));
        bundle.putString("current_comment",this.getComment());

        return bundle;

    }

}
