package com.apachebruteforce.core;

public final class Data {
    private static Data data;
    private String endPassword;
    private String currPassword;
    private Brute brute;

    private int passCounter;
    private boolean isFind;

    private Data(String startPassword, String endPassword, Brute brute) {
        this.endPassword = endPassword;
        this.brute = brute;
        passCounter = 1;

        isFind = false;

        currPassword = startPassword;
    }

    public void updateData(String startPassword, String endPassword) {
        this.currPassword = startPassword;
        this.endPassword = endPassword;
    }

    public static Data getInstance(String startPassword, String endPassword, Brute brute) {
        if(data == null) {
            data = new Data(startPassword, endPassword, brute);
        }

        return data;
    }

    public static Data getInitInstance() {
        return data;
    }

    synchronized boolean isEndPass() {
        return endPassword.equals(currPassword);
    }

    synchronized boolean isFind() {
        return isFind;
    }

    public synchronized void passFound() {
        this.isFind = true;
    }

    public int getPassCounter() {
        return passCounter;
    }


    synchronized  String getCurrPassword() {
        String lastPassword = currPassword;
        currPassword = brute.nextPass(currPassword);

        passCounter++;

        return lastPassword;
    }
}
