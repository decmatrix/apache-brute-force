package com.apachebruteforce.core;

import java.io.IOException;

public class AuthThread implements Runnable {
    private static Brute brute;
    private static Data data;
    private static AuthType authType;

    public static void setTypeAuth(AuthType authType) {
        AuthThread.authType = authType;
    }

    public static void staticInitClass(Brute brute, Data data) {
        AuthThread.brute = brute;
        AuthThread.data = data;
    }

    private boolean isNoAuth() {
        int response = 0;

        try {
            response = brute.nonAuthRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return !(response == 200);
    }

    public AuthThread() {}

    @Override
    public void run() {
        if(isNoAuth()) {
            //todo check end password
            while (!data.isEndPass() && !data.isFind()) {
                String password = data.getCurrPassword();
                int resRequest = 0;

                //todo: write common function interface
                try {
                     resRequest = authType.authFunc(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //todo: create window result
                if(resRequest == 200) {
                    data.passFound();
                    brute.setResult(password);
                }
            }
        }
    }
}
