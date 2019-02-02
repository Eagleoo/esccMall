package com.helgi.flappybirdgame;

import android.graphics.Paint;

import com.helgi.framework.Game;
import com.helgi.framework.Graphics;
import com.helgi.framework.Input.TouchEvent;
import com.helgi.framework.Screen;

import java.util.List;

public class MainMenuScreen extends Screen {
	// Vi�fangsbreytur
	Paint font; // �etta er leturger�in � notkun
	
    public MainMenuScreen(Game game) {
        super(game);
        
        /* D�mi fyrir stillingar um font
        font = new Paint();
        font.setTextSize(30);
        font.setTextAlign(Paint.Align.CENTER);
        font.setAntiAlias(true);
        font.setColor(Color.WHITE);
        
        s��an er gert t.d.:
        Graphics g = game.getGraphics();
         g.drawString("Tap each side of the screen to", 400, 650, paint); */
    }


    /* �egar LoadingScreen kallar � MainMenuScreen �� keyrist �essi a�fer�. �a� er kalla� � s�fellu � �essa
     * update a�fer� �anga� til a� eitthva� gerist
     */
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // �egar �tt er einhverssta�ar � skj�inn ver�ur len > 0 og �v� keyrist forlykkjan
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
            	
            	/////////////////////////////////////////////////////////
            	// H�rna er �a� sem gerist �egar �tt er � takka o.s.frv.
            	/////////////////////////////////////////////////////////
            	
            	// Ef snertiskj�rinn skynjar snertingu � fleti sem hefur hornpunkt (0,0), breidd 250 og h�� 250
            	// �� er kalla� � GameScreen sem er leikurinn sj�lfur � keyrslu. (0,0) er efst til vinstri �
            	// glugga Android t�kisins.
            	// �urfum a� stilla �etta a� r�ttu gildi, hverjum takka o.s.frv.
            	
            	///////////////
            	// START takki
            	///////////////
                if (inBounds(event, gameWidth/6, gameHeight/1.33, res*230, res*77)) {
                	// Hefjum spilun
                    game.setScreen(new GameScreen(game));              
                }
                /*///////////////
              	// SCORE takki
              	///////////////
                  if (inBounds(event, gameWidth/1.8, gameHeight/1.33, res*230, res*77)) {
                  	// Hefjum spilun
                      game.setScreen(new ScoreScreen(game));              
                  }*/
            }
        }
    }


    private boolean inBounds(TouchEvent event, double x, double y, double width,
            double height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }


    @Override
    public void paint(float deltaTime) {
    	Graphics g = game.getGraphics();
    	
    	////////////////////////////////////////////////////////////////
    	// H�rna h�fum vi� �ll MainMenu elementin okkar, takka o.s.frv.
    	////////////////////////////////////////////////////////////////
    	
    	// Skalinn byrjar efst til vinstri � glugganum
        // �.e.a.s. hnitakerfi�. Skalinn er 800 x 1280 � S2
    	
        g.drawImage(Assets.background, matrix, 0, 0, 0, res*1.04, false);
        g.drawImage(Assets.backgroundBase, matrix, 0, gameHeight/1.1445, 0, res*1.04*0.9615, false);
        g.drawImage(Assets.flappyBirdLogo, matrix, gameWidth/13.71, gameHeight/3.9, 0, res, false);
        g.drawImage(Assets.portrait, matrix, gameWidth/1.247, gameHeight/3.70, 0, res, false);
        g.drawImage(Assets.start, matrix, gameWidth/6, gameHeight/1.33, 0, res, false);

        //g.drawImage(Assets.score, matrix, gameWidth/1.8, gameHeight/1.33, 0, res, false);
        
        // Teiknar fylltan kassa
        //g.drawRect(0, 0, 780, 1260, Color.BLACK);
        // �etta v�ri d�mi um �egar skrifa�ur er strengur � skj�inn.
        // Hva� stendur, sta�setning og letur. Letri� font er vi�fangsbreyta � �essum klasa
        // g.drawString("Tap to play.", 240, 400, font);
    }


    @Override
    public void pause() {
    }


    @Override
    public void resume() {


    }


    @Override
    public void dispose() {


    }


    @Override
    public void backButton() {
        //Display "Exit Game?" Box


    }
}
