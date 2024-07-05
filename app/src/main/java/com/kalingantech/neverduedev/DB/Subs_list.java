package com.kalingantech.neverduedev.DB;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "subs_table")
public class Subs_list {

    @PrimaryKey(autoGenerate = true)
    public int uid;
    public String sub_image;
    double price,total_paid;
    Boolean reminder;
    int billingcycle_no,reminder_no;
    @TypeConverters(Converter.class)
    Date billing_next_date, billing_start_date;
    String name, notes,paymenttype, category, profile, billingcycle_step_type;

    public Subs_list(int uid, String sub_image, double price,double total_paid,String name,  Date billing_start_date,Date billing_next_date,  int billingcycle_no,String billingcycle_step_type, Boolean reminder,int reminder_no,  String notes, String paymenttype, String category, String profile ) {
        this.uid = uid;
        this.sub_image = sub_image;
        this.price = price;
        this.total_paid = total_paid;
        this.name = name;
        this.billing_start_date = billing_start_date;
        this.billing_next_date = billing_next_date;
        this.billingcycle_no = billingcycle_no;
        this.billingcycle_step_type = billingcycle_step_type;
        this.reminder = reminder;
        this.reminder_no = reminder_no;
        this.notes = notes;

        this.paymenttype = paymenttype;
        this.category = category;
        this.profile = profile;

    }

    @Ignore
    public Subs_list(String sub_image, double price,double total_paid,String name,  Date billing_start_date,Date billing_next_date,  int billingcycle_no,String billingcycle_step_type, Boolean reminder,int reminder_no,  String notes, String paymenttype, String category, String profile ) {
        this.sub_image = sub_image;
        this.price = price;
        this.total_paid = total_paid;
        this.name = name;
        this.billing_start_date = billing_start_date;
        this.billing_next_date = billing_next_date;
        this.billingcycle_no = billingcycle_no;
        this.billingcycle_step_type = billingcycle_step_type;
        this.reminder = reminder;
        this.reminder_no = reminder_no;

        this.notes = notes;

        this.paymenttype = paymenttype;
        this.category = category;
        this.profile = profile;

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSub_image() {
        return sub_image;
    }

    public void setSub_image(String sub_image) {
        this.sub_image = sub_image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(double total_paid) {
        this.total_paid = total_paid;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public int getBillingcycle_no() {
        return billingcycle_no;
    }

    public void setBillingcycle_no(int billingcycle_no) {
        this.billingcycle_no = billingcycle_no;
    }

    public int getReminder_no() {
        return reminder_no;
    }

    public void setReminder_no(int reminder_no) {
        this.reminder_no = reminder_no;
    }

    public Date getBilling_next_date() {
        return billing_next_date;
    }

    public void setBilling_next_date(Date billing_next_date) {
        this.billing_next_date = billing_next_date;
    }

    public Date getBilling_start_date() {
        return billing_start_date;
    }

    public void setBilling_start_date(Date billing_start_date) {
        this.billing_start_date = billing_start_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBillingcycle_step_type() {
        return billingcycle_step_type;
    }

    public void setBillingcycle_step_type(String billingcycle_step_type) {
        this.billingcycle_step_type = billingcycle_step_type;
    }
}
