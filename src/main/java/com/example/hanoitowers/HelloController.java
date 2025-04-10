package com.example.hanoitowers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.media.Media;

//import javax.print.attribute.standard.Media;
import java.awt.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

import static java.lang.Integer.parseInt;

public class HelloController {
    ArrayList<Button>[] Poles = new ArrayList[3];
    @FXML
    AnchorPane pane,startScreen, gameNamePanel,statsPanel;
    @FXML
    Slider N;
    int totalHeight=400,diskHeight;
    int fromPole=-1;
    Button demoButton;

    int[] from,to; int curStep=-1; int generationIndex;

    public void generateSteps()
    {
        from=new int[(int)Math.pow(2,N.getValue())-1];
        to=new int[(int)Math.pow(2,N.getValue())-1];
        curStep=-1; generationIndex=0;
        moveDisk((int)N.getValue(),0,2,1);

       /* for(int i=0;i<from.length;i++)
        {
            System.out.println(from[i]+" to "+to[i]);
        }*/
    }
    void moveDisk(int n,int from_rod, int to_rod,int buffer_rod)
    {
        if (n == 0) {
            return;
        }
        moveDisk(n - 1, from_rod, buffer_rod, to_rod);
        from[generationIndex]=from_rod;
        to[generationIndex++]=to_rod;
        moveDisk(n - 1, buffer_rod, to_rod, from_rod);
    }

    @FXML Button next,prev;

    @FXML CheckBox Auto;

    @FXML
    protected void prev()
    {
        next.setDisable(false);
        play.setDisable(false);
        Button b = Poles[to[curStep]].get(Poles[to[curStep]].size() - 1);
        
        TT=new TranslateTransition(Duration.millis(AnimTime/3),b);


        step=0;
        Timeline Anim = new Timeline(new KeyFrame(Duration.millis(AnimTime/3+AnimSlack), event -> {
            TT=new TranslateTransition(Duration.millis(AnimTime/3),b);
            switch (step) {
                case 0: {
                    TT.setToY(200 - diskHeight / 2f - b.getLayoutY()/*-720+ next.getPrefHeight()+b.getLayoutY()*/);
                    TT.play();

                }
                case 1: {
                    TT.setToX(228 - (b.getPrefWidth()) / 2 + 413 * (from[curStep+1]) -b.getLayoutX());
                    TT.play();

                }
                case 2: {
                    TT.setToY(-b.getLayoutY()+(720 - diskHeight * (Poles[from[curStep+1]].size())));
                    TT.play();
                }
            }
            System.out.println(step);
            step++;
        }));

        Anim.setCycleCount(3);
        Anim.play();
        Anim.setOnFinished(event ->
        {
            if (step == 3)
            {
                Timeline k = new Timeline(new KeyFrame(Duration.millis(AnimTime / 3), p ->
                {
                    dropPlayer.seek(Duration.ZERO);
                    dropPlayer.play();
                    Steps--;
                    StepLabel.setText(Steps+" Steps");
                }));
                k.setCycleCount(1);
                k.play();
            }
        });



        Poles[from[curStep]].add(b);
        Poles[to[curStep]].remove(b);
        //b.setLayoutX(228 - (b.getPrefWidth()) / 2 + 413 * (from[curStep]));
        //b.setLayoutY(720 - diskHeight * (Poles[from[curStep]].size()));
        System.out.println("bb");
        curStep--;
        if(curStep==-1)
        {
            prev.setDisable(true);
            rev.setDisable(true);
        }
    }
    TranslateTransition TT;
    float AnimTime=600, AnimSlack=50;

    @FXML AnchorPane autoPane;

    private static int step=0;
    @FXML Button rev,play;

    @FXML
    protected  void next()
    {
        curStep++;
        prev.setDisable(false);
        rev.setDisable(false);
        Button b = Poles[from[curStep]].get(Poles[from[curStep]].size() - 1);
        TT=new TranslateTransition(Duration.millis(AnimTime/3),b);

        //System.out.println(b.getLayoutX()+b.getPrefWidth()/2);

        step=0;
        Timeline Anim = new Timeline(new KeyFrame(Duration.millis(AnimTime/3+AnimSlack), event -> {
            TT=new TranslateTransition(Duration.millis(AnimTime/3),b);
            switch (step) {
                case 0: {
                    TT.setToY(200 - diskHeight / 2f - b.getLayoutY()/*-720+ next.getPrefHeight()+b.getLayoutY()*/);
                    TT.play();

                }
                case 1: {
                    TT.setToX(228 - (b.getPrefWidth()) / 2 + 413 * (to[curStep]) -b.getLayoutX());
                    TT.play();
                }
                case 2: {
                    TT.setToY(-b.getLayoutY()+(720 - diskHeight * (Poles[to[curStep]].size())));
                    TT.play();

                }
            }
            System.out.println(step);
            step++;
        }));
        Anim.setCycleCount(3);
        Anim.setOnFinished(event ->
        {
            if (step == 3)
            {
                Timeline k = new Timeline(new KeyFrame(Duration.millis(AnimTime / 3), p ->
                {
                    dropPlayer.seek(Duration.ZERO);
                    dropPlayer.play();
                    Steps++;
                    StepLabel.setText(Steps+" Steps");
                }));
                k.setCycleCount(1);
                k.play();
            }
        });
        Anim.play();

        //b.setLayoutY(0);



        System.out.println(Poles[0].size()+", "+Poles[1].size()+", "+Poles[2].size());



        /*TT.setToY(720 - diskHeight * (Poles[to[curStep]].size()));
        TT.play();*/
        Poles[to[curStep]].add(b);
        Poles[from[curStep]].remove(b);
        System.out.println(Poles[0].size()+", "+Poles[1].size()+", "+Poles[2].size()+"\n ----");
        // b.setLayoutX(228 - (b.getPrefWidth()) / 2 + 413 * (to[curStep]));
        //  b.setLayoutY(720 - diskHeight * (Poles[to[curStep]].size()));
        System.out.println("aa");
        if(curStep==generationIndex-1)
        {
            next.setDisable(true);
            play.setDisable(true);
        }
    }

    //@FXML
    //Button ErrorField;

    @FXML Label nView;

    int dX,dY,origX,origY;
    boolean moving;

    int Steps;
    Rectangle[] rects = new Rectangle[16];

    Timeline AutoTimeline=new Timeline();

    @FXML Button pause;
    @FXML
    protected void pause()
    {
        AutoTimeline.stop();
        pause.setVisible(false);
        next.setDisable(false);
        prev.setDisable(false);
    }

    @FXML
    protected void rev()
    {
        AutoTimeline = new Timeline(new KeyFrame(Duration.millis(AnimTime+4*AnimSlack), event -> {
            if(!prev.isDisabled()) {
                prev();
                pause.setVisible(true);
            }
            else
            {
                AutoTimeline.pause();
                pause.setVisible(false);
            }
        }));
        //AutoTimeline.setCycleCount((int)Math.pow(2,N.getValue())-curStep-2);
        AutoTimeline.setCycleCount(Timeline.INDEFINITE);
        if(AutoTimeline.getStatus()==Timeline.Status.STOPPED)
        {
            AutoTimeline.play();
        }


        //AutoTimeline.setDelay(Duration.millis(200));
    }

    boolean AI_Assisted=false;

    @FXML
    protected void play()
    {
        AutoTimeline = new Timeline(new KeyFrame(Duration.millis(AnimTime+4*AnimSlack), event -> {
            if(!next.isDisabled()) {
                next();
                pause.setVisible(true);
            }
            else
            {
                AutoTimeline.pause();
                pause.setVisible(false);
            }
        }));
        //AutoTimeline.setCycleCount((int)Math.pow(2,N.getValue())-curStep-2);
        AutoTimeline.setCycleCount(Timeline.INDEFINITE);
        if(AutoTimeline.getStatus()==Timeline.Status.STOPPED)
        {
            AutoTimeline.play();
        }
        //AutoTimeline.setDelay(Duration.millis(200));
    }


    @FXML
    protected void reset()
    {
        pause();
        //Auto.setSelected(false);
        Auto.setSelected(false);
        AutoTimeline=null;
        Exit();
        onHelloButtonClick();
    }

    @FXML
    protected void Exit()
    {
        butPlayer.seek(Duration.ZERO);
        butPlayer.setVolume(20f);
        butPlayer.play();
        butPlayer.setOnEndOfMedia(()->{
            gameplayPlayer.stop();
            mainMenuPlayer.play();
        });

        seconds=0;
        AI_Assisted=false;
        if(Timer!=null){Timer.stop(); Timer=null;}
        TimerLabel.setText("0:00:00");

        startScreen.setVisible(true);
        playButton.setText("Play!");
        statsPanel.setVisible(false);
        gameNamePanel.setVisible(true);
        demoButton.setVisible(false);
       // demoLabel.setVisible(false);
        for (int i = 0; i < 3; i++) {
            if(Poles[i]!=null) {
                for (Button b : Poles[i]) {
                    pane.getChildren().remove(b);
                }
            }
            Poles[i]=new ArrayList<Button>();
            Steps=0;
            StepLabel.setText("0 Steps");
            StepLabel1.setText("0 Steps");
        }

    }
    @FXML Slider speed;
  //  Label demoLabel;

    MediaPlayer errorMediaPlayer, victoryMediaPlayer,mainMenuPlayer,gameplayPlayer,butPlayer,dropPlayer;
    Media errorSound, victorySound,mainMenuSound,gameplaySound,butSound,dropSound;

    float vol=0.1f;

    public void initialize()
    {
        dropSound=new Media(getClass().getResource("/com/example/hanoitowers/drop.mp3").toString());//"C:/Users/Sadra Khaleghi Bavil/IdeaProjects/HanoiTowers/src/main/resources/com/example/hanoitowers/off.wav").toURI().toString());
        dropPlayer=new MediaPlayer(dropSound);
        dropPlayer.setVolume(10);

        butSound= new Media(getClass().getResource("/com/example/hanoitowers/But.mp3").toString());//"C:/Users/Sadra Khaleghi Bavil/IdeaProjects/HanoiTowers/src/main/resources/com/example/hanoitowers/off.wav").toURI().toString());
        butPlayer= new MediaPlayer(butSound);


        gameplaySound=new Media(getClass().getResource("/com/example/hanoitowers/Gameplay.mp3").toString());//"C:/Users/Sadra Khaleghi Bavil/IdeaProjects/HanoiTowers/src/main/resources/com/example/hanoitowers/off.wav").toURI().toString());
        gameplayPlayer=new MediaPlayer(gameplaySound);
        gameplayPlayer.setVolume(vol/2);

        mainMenuSound=new Media(getClass().getResource("/com/example/hanoitowers/MainMenu.mp3").toString());//"C:/Users/Sadra Khaleghi Bavil/IdeaProjects/HanoiTowers/src/main/resources/com/example/hanoitowers/off.wav").toURI().toString());
        mainMenuPlayer=new MediaPlayer(mainMenuSound);
        mainMenuPlayer.setVolume(vol/2);

        victorySound= new Media(getClass().getResource("/com/example/hanoitowers/Victory.mp3").toString());//"C:/Users/Sadra Khaleghi Bavil/IdeaProjects/HanoiTowers/src/main/resources/com/example/hanoitowers/off.wav").toURI().toString());
        victoryMediaPlayer= new MediaPlayer(victorySound);
        victoryMediaPlayer.setVolume(vol);

        errorSound = new Media(getClass().getResource("/com/example/hanoitowers/error.wav").toString());//"C:/Users/Sadra Khaleghi Bavil/IdeaProjects/HanoiTowers/src/main/resources/com/example/hanoitowers/off.wav").toURI().toString());
        errorMediaPlayer = new MediaPlayer(errorSound);
        errorMediaPlayer.setVolume(vol*2);

        tt1.setShowDelay(Duration.millis(100));
        tt2.setShowDelay(Duration.millis(100));
        tt3.setShowDelay(Duration.millis(100));
        tt4.setShowDelay(Duration.millis(100));
        tt5.setShowDelay(Duration.millis(100));
        tt5.setShowDelay(Duration.millis(100));
        tts.setShowDelay(Duration.millis(100));
        tt1.setStyle("-fx-font-size: 14");
        tt2.setStyle("-fx-font-size: 14");
        tt3.setStyle("-fx-font-size: 14");
        tt4.setStyle("-fx-font-size: 14");
        tt5.setStyle("-fx-font-size: 14");
        tts.setStyle("-fx-font-size: 14");
        mainMenuPlayer.seek(Duration.ZERO);
        mainMenuPlayer.play();
        //ttd.setStyle("-fx-font-size: 14");

        tooltip.setShowDelay(Duration.millis(0));

        pause.setVisible(false);
        statsPanel.setVisible(false);
        gameNamePanel.setVisible(true);
        demoButton=new Button("");
       // demoLabel=new Label("✘");
       // pane.getChildren().add(demoLabel);
        pane.getChildren().add(demoButton);
        demoButton.setVisible(false);
       // demoLabel.setVisible(false);
        startScreen.setVisible(true);
        for(int i=0;i<rects.length;i++)
        {
            rects[i]=new Rectangle();
            rects[i].setLayoutX(474);
            rects[i].setLayoutY(252-16*i);
            rects[i].setHeight(16);
            rects[i].setWidth(109);
            rects[i].setStrokeWidth(1.5);
            rects[i].setFill(javafx.scene.paint.Paint.valueOf(i%4>1?i%2==0?"Blue":"Yellow":i%2==0?"Red":"Lime"));
            rects[i].setArcHeight(3); rects[i].setArcWidth(3);
            rects[i].setStroke(javafx.scene.paint.Paint.valueOf("black"));
            startScreenS.getChildren().add(rects[i]);
            rects[i].toBack();
        }

        Auto.selectedProperty().addListener((observable, oldValue, newValue)-> {
            if(newValue.booleanValue())
            {
                onHelloButtonClick();
                TimerLabel.setText("AI Assisted");
                AI_Assisted=true;
                //reset();
            }
            tooltip.setText(newValue.booleanValue()?"Disable AutoSolve & continue on your own":"Warning! This action will reset your progress!");
            tooltip.setShowDelay(Duration.millis(newValue.booleanValue()?500:0));
            //AI_Assisted=newValue.booleanValue();
            autoPane.setDisable(!newValue.booleanValue());
        });

        speed.valueProperty().addListener((observable, oldValue, newValue) -> {
            AnimTime=(int)speed.getValue();
            AnimSlack=AnimTime/4f;
        });

        N.valueProperty().addListener((observable, oldValue, newValue) -> {
            int i=0;
            for(Rectangle r : rects)
            {
                r.setWidth(Math.max((float)(newValue.intValue()-i)/newValue.intValue()*94+15,0));
                r.setLayoutX(484+55-r.getWidth()/2);
                nView.setText(newValue.intValue()+"");
                r.setVisible(i<newValue.intValue());
                i++;
            }
            //startScreen.setsize
        });
        N.setValue(3);
    }

    Timeline Timer; int seconds;
    @FXML Label TimerLabel,StepLabel,TimerLabel1,StepLabel1;
    @FXML Button playButton;
    @FXML AnchorPane warning, startScreenS;
    @FXML
    Tooltip tooltip,tt1,tt2,tt3,tt4,tt5,tts;

    @FXML
    protected void onHelloButtonClick()
    {
        butPlayer.seek(Duration.ZERO);
        butPlayer.setVolume(20f);
        butPlayer.play();
        butPlayer.setOnEndOfMedia(()->{
            mainMenuPlayer.stop();
            gameplayPlayer.seek(Duration.ZERO);
            gameplayPlayer.play();
        });


        AI_Assisted=false;
        seconds=0;
        if(Timer!=null)Timer.stop();
        TimerLabel.setText(Auto.isSelected()?"AI Assisted":"0:00:00");
        next.setDisable(false);
        play.setDisable(false);
        prev.setDisable(true);
        rev.setDisable(true);


        generateSteps();
        startScreen.setVisible(false);

        demoButton.setVisible(false);
       // demoLabel.setVisible(false);
        for (int i = 0; i < 3; i++) {
            if(Poles[i]!=null) {
                for (Button b : Poles[i]) {
                    pane.getChildren().remove(b);
                }
            }
            Poles[i]=new ArrayList<Button>();
            Steps=0;
            StepLabel.setText("0 Steps");
            StepLabel1.setText("0 Steps");
        }


        diskHeight=(int)(450/(N.getValue()+1));
        for(int i=0; i<N.getValue(); i++)
        {
            Button b = new Button();

            b.setOnMouseDragged(e-> {
                if(!Auto.isSelected()) {
                    b.setLayoutX(b.getLayoutX()+b.getTranslateX());
                    b.setLayoutY(b.getLayoutY()+b.getTranslateY());
                    b.setTranslateX(0);
                    b.setTranslateY(0);

                    if (!moving) {
                        if (Timer == null) {
                            Timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                                seconds++;
                                int hours = seconds / 3600;
                                int minutes = (seconds % 3600) / 60;
                                int secs = seconds % 60;

                                String timeString = String.format("%01d:%02d:%02d", hours, minutes, secs);
                              if(!AI_Assisted) {
                                  TimerLabel.setText(timeString);
                                  TimerLabel1.setText(timeString);
                              }

                            }));
                            Timer.setCycleCount(Timeline.INDEFINITE);
                            Timer.play();
                        }
                        origX = (int) b.getLayoutX()+(int)b.getTranslateX();
                        origY = (int) b.getLayoutY()+(int)b.getTranslateY();
                        dX = (int) (e.getSceneX() - b.getLayoutX());
                        dY = (int) (e.getSceneY() - b.getLayoutY());
                        for (int p = 0; p < 3; p++) {
                            if (Poles[p].size() > 0) {
                                if (Poles[p].get(Poles[p].size() - 1).getText() == b.getText()) {
                                    fromPole = p;
                                    moving = true;
                                    b.toFront();
                                }
                            }
                        }
                    } else {
                        if (fromPole != -1) {
                            boolean ok = false;
                            for (int p = 0; p < 3; p++) {
                                if (Math.abs(e.getSceneX() - dX+b.getTranslateX() + (b.getWidth() / 2) - (413 * p) - 217) < b.getPrefWidth() / 2) {
                                    if (p != fromPole) {
                                        ok = true;
                                        demoButton.setLayoutX(228 - (b.getPrefWidth()) / 2 + 413 * (p));
                                        demoButton.setLayoutY(720 - diskHeight * (Poles[p].size() + 1));
                                        //demoLabel.setLayoutX(demoButton.getLayoutX()-demoLabel.getWidth()/2);
                                        //demoLabel.setLayoutY(demoButton.getLayoutY()-demoLabel.getHeight()/2);

                                        demoButton.setMinSize(b.getPrefWidth(), b.getPrefHeight());
                                        demoButton.setMaxSize(b.getPrefWidth(), b.getPrefHeight());
                                        demoButton.setPrefSize(b.getPrefWidth(), b.getPrefHeight());

                                       // demoButton.setTextAlignment(TextAlignment.LEFT);
                                        System.out.println(demoButton.getHeight()+";"+demoButton.getPrefHeight());


                                        if (Poles[p].size() > 0 ? parseInt(b.getText()) < parseInt(Poles[p].get(Poles[p].size() - 1).getText()) : true) {

                                            System.out.println(b.getHeight());

                                            demoButton.setStyle("-fx-background-color: #ffffffc0; -fx-font-size: " + (b.getWidth()>100?b.getHeight()*0.4:0) + "; -fx-border-color: "+getButtonColor(b)+"; -fx-text-fill: lime; -fx-border-radius: 5; -fx-border-width: 5px; -fx-alignment: CENTER");
                                            //demoButton.setStyle("-fx-background-color: "+""+"; -fx-font-size: " + b.getPrefHeight() * .45 + "; -fx-border-color: Green; -fx-text-fill: black; -fx-border-radius: 5; -fx-alignment: LEFT");
                                            demoButton.setText((b.getText().equals("1"))?"":""/*✔"*/);
                                        } else {
                                            /*cross*/demoButton.setStyle("-fx-background-color: #ffffffc0; -fx-font-size: " + Math.min(b.getPrefHeight(),b.getPrefWidth()) * .4 + "; -fx-border-color: "+getButtonColor(b)+"; -fx-text-fill: red; -fx-border-radius: 5; -fx-border-width: 5px; -fx-alignment: CENTER");
                                            //demoButton.setStyle(CSS((int)demoButton.getWidth()/10)+" -fx-font-size: " + Math.min(b.getPrefHeight(),b.getPrefWidth()) * .40 + "; -fx-border-color: "+getButtonColor(b)+"; -fx-text-fill: red; -fx-border-radius: 5; -fx-border-width: 5px; -fx-alignment: CENTER");
                                            //old//demoButton.setStyle("-fx-background-color: #FF000088; -fx-font-size: " + b.getPrefHeight() * .45 + "; -fx-border-color: Red; -fx-text-fill: black; -fx-border-radius: 5; -fx-alignment: LEFT");
                                            demoButton.setText("✘"); //❗

                                        }
                                    }
                                }
                            }
                            demoButton.setVisible(ok);
                           // demoLabel.setVisible(ok);
                        }

                        b.setLayoutX(e.getSceneX() - dX);
                        b.setLayoutY(e.getSceneY() - dY);
                    }
                }
            });

            b.setOnMouseReleased(e->{
                if(!Auto.isSelected()) {
                    boolean moveSuccess = false;
                    if (fromPole != -1) {
                        for (int p = 0; p < 3; p++) {
                            if (Math.abs(e.getSceneX() - dX + (b.getWidth() / 2) - (413 * p) - 217) < b.getPrefWidth() / 2) {
                                System.out.println(p+"/");
                                if (p != fromPole) {
                                    if (Poles[p].size() > 0 ? parseInt(b.getText()) < parseInt(Poles[p].get(Poles[p].size() - 1).getText()) : true) {
                                        Poles[p].add(b);
                                        Poles[fromPole].remove(b);
                                        System.out.println(b.getText() + " from " + fromPole + " to " + p + " ( x = " + (217 + 413 * (p)) + " )");
                                        //b.setLayoutX(228 - (b.getPrefWidth()) / 2 + 413 * (p));
                                        //b.setTranslateY(720 - diskHeight * (Poles[p].size()));
                                        b.setTranslateX(0);
                                        b.setTranslateY(0);
                                        b.setLayoutX(demoButton.getLayoutX());
                                        b.setLayoutY(demoButton.getLayoutY());
                                        //b.setLayoutY(720 - diskHeight * (Poles[p].size()));

                                        moveSuccess = true;
                                        Steps++;
                                        StepLabel.setText(Steps + " Steps");
                                        StepLabel1.setText(Steps + " Steps");
                                        dropPlayer.seek(Duration.ZERO);
                                        dropPlayer.play();
                                    } else {
                                        //ErrorField.setText("Wrong move AHole!!!!");
                                        warning.setVisible(true);
                                        //play error sound
                                        {

                                            errorMediaPlayer.seek(Duration.ZERO);
                                            errorMediaPlayer.setVolume(20);
                                            errorMediaPlayer.play();
                                        }
                                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(1), q -> {
                                            warning.setVisible(false);
                                        }));
                                        t.setCycleCount(1); // Run once
                                        t.playFromStart();
                                    }
                                }
                            }
                        }
                        moving = false;
                        demoButton.setVisible(false);
                        if (!moveSuccess) {
                            b.setLayoutX(origX);
                            b.setLayoutY(origY);
                        }

                        if (Poles[0].isEmpty() && (Poles[1].isEmpty() || Poles[2].isEmpty())) {
                            gameplayPlayer.stop();
                            victoryMediaPlayer.seek(Duration.ZERO);
                            victoryMediaPlayer.play();
                            victoryMediaPlayer.setOnEndOfMedia(()->{
                                mainMenuPlayer.seek(Duration.ZERO);
                            mainMenuPlayer.play();
                            });
                            Timer.stop();
                            startScreen.setVisible(true);
                            playButton.setText("Re-Play!");
                            startScreen.toFront();
                            gameNamePanel.setVisible(false);
                            statsPanel.setVisible(true);
                            TimerLabel1.setText(TimerLabel.getText());
                            StepLabel1.setText(StepLabel.getText()+" (Min steps: "+(int)(Math.pow(2,N.getValue())-1)+")");
                            Auto.setSelected(false);
                            AI_Assisted=false;
                            if(Timer!=null){Timer.stop();Timer=null;}
                            //ErrorField.setText("WinnerWinner, ChickenDinner\nCompleted in " + Steps + " steps (Min=" + ((int) Math.pow(2, N.getValue()) - 1) + ")");
                        }
                    }
                    fromPole = -1;
                }
            });

            int x0=228;
            b.setLayoutX(228-(100+i/(N.getValue()-1)*260)/2);
            b.setLayoutY(720-(diskHeight)*((int)N.getValue()-i));
            //colorful

            int rgb = Color.HSBtoRGB((float)(i/(N.getValue())), 1.0f, 1.0f);

            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            float luminance = (0.299f * red + 0.587f * green + 0.114f * blue);

            String col= String.format("#%02X%02X%02X", red, green, blue);
            String textCol = (luminance > 128) ? "#000000" : "#FFFFFF";
            //colorful

            b.setMaxSize((100+i/(N.getValue()-1)*260),diskHeight);
            b.setMinSize((100+i/(N.getValue()-1)*260),diskHeight);
            b.setPrefSize((100+i/(N.getValue()-1)*260),diskHeight);
            //b.setStyle("-fx-pref-height: "+diskHeight+"; -fx-max-height: "+diskHeight+"; -fx-pref-width: "+(100+i/(N.getValue()-1)*260)+"; -fx-background-color: "+col+"; -fx-text-fill: "+textCol+"; -fx-font-size:"+(13+30*(16-N.getValue())/13));
            b.setStyle("-fx-background-color: "+col+"; -fx-text-fill: "+textCol+"; -fx-font-size: "+b.getPrefHeight()*.45+"; -fx-text-alignment: center; -fx-alignment: center; ");

            System.out.println(Bindings.concat(
                    "-fx-font-size: ", b.heightProperty().multiply(0.45).asString("%.0f"), "px;" // Adjust 0.3
            ));
           /* b.styleProperty().bind(Bindings.concat(
                    "-fx-font-size: ", b.heightProperty().multiply(0.45).asString("%.0f"), "px;" // Adjust 0.3
            ));*/

            b.setText((1+i)+"");
            Poles[0].addFirst(b);
            pane.getChildren().add(b);
        }
    }
    String getButtonColor(Button b) {
        String style =b.getStyle();
        for (String rule : style.split(";")) {
            if (rule.contains("-fx-background-color")) {
                return rule.split(":")[1].trim();
            }
        }
        return "";
    }

    String CSS(int stripeCount) {
        StringBuilder gradient = new StringBuilder(" -fx-background-color: linear-gradient( to right bottom, ");

        double step = 100.0 / (stripeCount * 2); // Divide the gradient into equal parts
        for (int i = 0; i < stripeCount; i++) {
            double start = i * step * 2;
            double mid = start + step;
            double end = mid + step;
            gradient.append("#FF8000 ").append(start).append("%, #FF8000 ").append(mid).append("%, ")
                    .append("#ffffffc0 ").append(mid).append("%, #ffffffc0 ").append(end).append("%, ");
        }
        gradient.append("#FF8000 100%);");
        return gradient.toString();
    }
}