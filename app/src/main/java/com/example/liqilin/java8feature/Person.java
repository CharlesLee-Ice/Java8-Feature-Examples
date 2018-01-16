package com.example.liqilin.java8feature;

import android.util.Log;

import java.time.LocalDate;

/**
 * Created by liqilin on 2017/10/18.
 */

public class Person {

    public enum Sex {
        MALE, FEMALE
    }

    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;

    public Person() {

    }

    public Person(String name, LocalDate birthday, Sex gender, String email) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.emailAddress = email;
    }

    public int getAge() {
         return LocalDate.now().getYear() - birthday.getYear();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void printPerson() {
        Log.i("liqilin", "name: "+name);
    }

    public static int compareByAge(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }

    public int compareByAge2(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }
}
