package com.helgi.flappybirdgame;


import com.helgi.framework.Game;
import com.helgi.framework.Graphics;
import com.helgi.framework.Graphics.ImageFormat;
import com.helgi.framework.Screen;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    // �egar kalla� er � loading screen er kalla� � �essa update a�fer� � s�fellu uns kalla� er � MainMenuScreen
    // ne�st � fallinu (�.e. �etta fall keyrist bara einu sinni)
    @Override
    public void update(float deltaTime) {
    	/* H�rna hl��um vi� inn skr�rnar sem ver�a � notkun � forritinu.
         * Allar skr�rnar �urfa a� vera � "assets" m�ppu verkefnisins (project).
         * 
         * Svona "loadast" leikurinn. Hl��um �llum myndum og hlj��um inn
         * � vi�fangsbreytur sem eru skilgreindar inn � Assets.java
    	 * 
    	 * B�um til eintak af �eim paint f�llum sem vi� �urfum a� nota.
    	 * LoadingScreen t�k inn "game" og viljum f� graphics f�llin sem
    	 * getGraphics() gefur.
    	 */
    	
        Graphics g = game.getGraphics();
        
        //////////////////
        // MainMenuScreen
        //////////////////
        Assets.background 		= g.newImage("Background.png", ImageFormat.RGB565);
        Assets.backgroundBase	= g.newImage("BackgroundBase.png", ImageFormat.RGB565);
        Assets.flappyBirdLogo 	= g.newImage("MainMenuScreen/FlappyBirdLogo.png", ImageFormat.ARGB4444);
        Assets.start 			= g.newImage("MainMenuScreen/Start.png", ImageFormat.RGB565);
        
        
        ///////////////
        // ScoreScreen
        ///////////////
        //Assets.back 			= g.newImage("ScoreScreen/Back.png", ImageFormat.RGB565);
        //Assets.score 			= g.newImage("MainMenuScreen/Score.png", ImageFormat.RGB565);
        //Assets.scoreSmall		= g.newImage("ScoreScreen/ScoreSmall.png", ImageFormat.RGB565);
        
        //////////////
        // GameScreen
        //////////////
        Assets.getReady 		= g.newImage("GameScreen/GetReady.png", ImageFormat.ARGB4444);
        Assets.getReadyTap		= g.newImage("GameScreen/GetReadyTap.png", ImageFormat.ARGB4444);
        Assets.pauseButton		= g.newImage("GameScreen/PauseButton.png", ImageFormat.RGB565);
        
        Assets.bird1 			= g.newImage("GameScreen/Bird/Bird1.png", ImageFormat.ARGB4444);
        Assets.bird2 			= g.newImage("GameScreen/Bird/Bird2.png", ImageFormat.ARGB4444);
        Assets.bird3 			= g.newImage("GameScreen/Bird/Bird3.png", ImageFormat.ARGB4444);
        Assets.pipe  			= g.newImage("GameScreen/Pipe.png", ImageFormat.ARGB4444);
        
        ////////////////
        // PausedScreen
        ////////////////
        Assets.playButton		= g.newImage("PausedScreen/PlayButton.png", ImageFormat.RGB565);
        Assets.paused			= g.newImage("PausedScreen/Paused.png", ImageFormat.ARGB4444);
        
        //////////////////
        // GameOverScreen
        //////////////////
        Assets.gameOver			= g.newImage("GameOverScreen/GameOver.png", ImageFormat.ARGB4444);
        Assets.GameOverScore	= g.newImage("GameOverScreen/GameOverScore.png", ImageFormat.RGB565);
        Assets.MedalNone		= g.newImage("GameOverScreen/MedalNone.png", ImageFormat.ARGB4444);
        Assets.MedalBronze		= g.newImage("GameOverScreen/MedalBronze.png", ImageFormat.ARGB4444);
        Assets.MedalSilver		= g.newImage("GameOverScreen/MedalSilver.png", ImageFormat.ARGB4444);
        Assets.MedalPremium		= g.newImage("GameOverScreen/MedalPremium.png", ImageFormat.ARGB4444);
        Assets.ok 				= g.newImage("GameOverScreen/Ok.png", ImageFormat.RGB565);
        Assets.portrait 		= g.newImage("GameOverScreen/portrait.png", ImageFormat.RGB565);

        // D�mi um a� b�a til hlj��
        //Assets.click = game.getAudio().createSound("explode.ogg");

        // H�rna runnar svo leikurinn eftir a� b�i� er a� hla�a inn �llum hlutunum.
        // �.e., hoppar yfir � MainMenuScreen.java
        game.setScreen(new MainMenuScreen(game));


    }


    @Override
    public void paint(float deltaTime) {
	    // Gerum svartan ferning svo bor�inn fyrir appi� birtist ekki
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
    	

    }
}
