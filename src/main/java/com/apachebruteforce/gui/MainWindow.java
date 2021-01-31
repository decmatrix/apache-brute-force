package com.apachebruteforce.gui;

import com.apachebruteforce.core.AuthThread;
import com.apachebruteforce.core.Brute;
import com.apachebruteforce.core.Data;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class MainWindow extends JFrame {

    private AuthThread[] threads;
    private int threadCount = 15;

    /* flags */
    private boolean flag;
    private boolean fDig;
    private boolean fPause;

    /* text areas */
    private JTextArea textArea;
    private JTextArea uriText;
    private JTextArea serverText;
    private JTextArea portText;
    private JTextArea symbolsText;
    private JTextArea startText;
    private JTextArea endText;
    private JTextArea hostText;
    private JTextArea loginText;
    private JTextArea resultText;

    public JTextArea logTextArea;

    /* buttons */
    private JButton stop;
    private JButton start;
    private JButton pause;

    /* other components */
    private JCheckBox userId;
    private JRadioButton btnDict;
    private JLabel dictCount;
    private JPanel content;

    public MainWindow() {
        super("Apache brute force v.0.0.1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        initActions();

        textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("ssss");
        textArea.setEditable(false);
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(Color.GRAY);

        //for Logging
        logTextArea.setFont(new Font("Dialog",Font.PLAIN , 10));
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);

        stop.setEnabled(false);


        content.setLayout(new GridLayout(1, 2, 50, 50));

        JPanel dicPanel = new JPanel();
        dicPanel.setLayout(new GridLayout(4, 1, 1, 1));
        content.add(dicPanel);

        JPanel useDicPanel = new JPanel();
        useDicPanel.setLayout(new GridLayout(1, 2));
        useDicPanel.add(btnDict);
        useDicPanel.add(new JLabel("use dictionary"));

        dicPanel.add(useDicPanel);
        dicPanel.add(new JScrollPane(textArea));


        JPanel countDictPanel = new JPanel();
        countDictPanel.setLayout(new GridLayout(1, 2));
        countDictPanel.add(new JLabel("Dict count: "));
        countDictPanel.add(dictCount);
        dicPanel.add(countDictPanel);

//        JPanel timePanel = new JPanel();
//        timePanel.setLayout(new GridLayout(1, 2));
//        timePanel.add(new JLabel("Time execute: "));
//        dicPanel.add(timePanel);


        JPanel infPanel = new JPanel();
        infPanel.setLayout(new GridLayout(16, 1, 1, 5));
        content.add(infPanel);

        JPanel hostPanel = new JPanel();
        hostPanel.setLayout(new GridLayout(1, 2, 30, 1));
        hostPanel.add(new JLabel("Host: "));
        hostPanel.add(hostText);
        infPanel.add(hostPanel);

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(1, 2, 30, 1));
        userPanel.add(new JLabel("Login: "));
        userPanel.add(loginText);
        infPanel.add(userPanel);

        JPanel servPortTextPanel = new JPanel();
        servPortTextPanel.setLayout(new GridLayout(1, 2, 30, 1));
        servPortTextPanel.add(new JLabel("Server: "));
        servPortTextPanel.add(new JLabel("Port: "));
        infPanel.add(servPortTextPanel);

        JPanel servPortPanel = new JPanel();
        servPortPanel.setLayout(new GridLayout(1, 2, 30, 1));
        serverText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        servPortPanel.add(serverText);
        portText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        servPortPanel.add(portText);
        infPanel.add(servPortPanel);

        JPanel uriPanel = new JPanel();
        uriPanel.setLayout(new GridLayout(1, 2, 1, 1));
        uriPanel.add(new JLabel("URI: "));
        uriText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        uriPanel.add(uriText);
        infPanel.add(uriPanel);

        infPanel.add(new JLabel("Alphabet of password: "));
        symbolsText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        infPanel.add(symbolsText);

        JPanel passInfPanel = new JPanel();
        passInfPanel.setLayout(new GridLayout(1, 2, 30, 1));
        passInfPanel.add(new JLabel("Start pass:"));
        passInfPanel.add(new JLabel("End pass:"));
        infPanel.add(passInfPanel);

        JPanel passPanel = new JPanel();
        passPanel.setLayout(new GridLayout(1, 2, 30, 1));
        startText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passPanel.add(startText);
        endText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passPanel.add(endText);
        infPanel.add(passPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1, 2, 10, 1));
        btnPanel.add(start);
        btnPanel.add(stop);
        infPanel.add(btnPanel);

        JPanel resPanel = new JPanel();
        resPanel.setLayout(new GridLayout(1, 2, 30, 1));
        resPanel.add(new JLabel("Result password: "));
        resultText.setEnabled(false);
        resultText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        resPanel.add(resultText);
        infPanel.add(resPanel);


        //content.add(logTextArea);

        setContentPane(content);
        setSize(1000, 500);
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        /* init text areas */
        textArea = new JTextArea(20, 20);
        uriText = new JTextArea("/", 1, 20);
        serverText = new JTextArea("127.0.0.1", 1, 20);
        portText = new JTextArea("80", 1, 6);
        symbolsText = new JTextArea("0123456789", 1, 20);
        startText = new JTextArea("0000", 1, 6);
        endText = new JTextArea("9999", 1, 6);
        hostText = new JTextArea("localhost", 1, 6);
        loginText =  new JTextArea("useraaa", 1, 6);
        resultText = new JTextArea("", 1, 6);

        logTextArea = new JTextArea(80 ,50);

        /* init buttons */
        stop = new JButton("Stop");
        start = new JButton("Start");
        pause = new JButton("Pause");

        /* init another components */
        userId = new JCheckBox();
        btnDict = new JRadioButton();
        dictCount = new JLabel("0");
        content = new JPanel();
    }

    private void initActions() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                dictCount.setText(String.valueOf(textArea.getLineCount()));
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                dictCount.setText(String.valueOf(textArea.getLineCount()));
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {}
        });

        btnDict.addItemListener((ItemEvent e) -> {
            int state = e.getStateChange();

            if(state == ItemEvent.SELECTED) {
                textArea.setEditable(true);
                textArea.setForeground(Color.BLACK);
                textArea.setBackground(Color.WHITE);
            } else if(state == ItemEvent.DESELECTED) {
                textArea.setEditable(false);
                textArea.setBackground(Color.GRAY);
            }
        });

        pause.addActionListener((ActionEvent e) -> {
            fPause = true;
            pause.setEnabled(false);
            stop.setEnabled(false);
            start.setEnabled(true);
        });

        stop.addActionListener((ActionEvent e) -> {
            stop.setEnabled(false);
            Data.getInitInstance().passFound();
            start.setEnabled(true);
        });

        start.addActionListener((ActionEvent e) -> {
            start.setEnabled(false);

            Brute.getInitInstance().updateData();

            //todo change to btn orr flag
            fDig = userId.isSelected();
//
            if(fDig) {
                AuthThread.setTypeAuth(Brute.getInitInstance()::authDigRequest);
            } else {
                AuthThread.setTypeAuth(Brute.getInitInstance()::authRequest);
            }
            //todo create filed of thread count
            threads = new AuthThread[threadCount];

            for(var i = 0; i < threadCount; i++) {
                threads[i] = new AuthThread();
                threads[i].run();
            }

            stop.setEnabled(true);

//            fDig = userId.isSelected();
//
//            if(!fDig) {
//                Thread thread;
//
//                if(btnDict.isEnabled()) {
//                    thread = new Thread();
//                }
//            }
        });
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setEnabledStart(boolean f) {
        start.setEnabled(f);
    }

    public void setEnabledStop(boolean f) {
        stop.setEnabled(f);
    }

    public String getEndPassword() {
        return endText.getText();
    }

    public String getStartPassword() {
        return startText.getText();
    }

    public void setEnabledPause(boolean f) {
        pause.setEnabled(f);
    }

    public void setFlag(boolean f) {
        flag = f;
    }

    public void setFlagPause(boolean f) {
        fPause = f;
    }

    //get base inf
    public String getUri() {
        return uriText.getText();
    }

    public String getHost() {
        return hostText.getText();
    }

    public String getLogin() {
        return loginText.getText();
    }

    public String getPort() {
        return portText.getText();
    }

    public void setResultPass(String pass) {
        resultText.setText(pass);
    }


}
