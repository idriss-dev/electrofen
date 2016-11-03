package com.mygdx.game;
import com.fazecast.jSerialComm.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ElectroFunCop22 extends ApplicationAdapter {
	private static final int        FRAME_GIRL_COLS = 4;         // #1
    private static final int        FRAME_GIRL_ROWS = 4;         // #2
    
    private static final int        HEIGHT = 600;         
    private static final int        WIDTH = 600;
    
    private static final int        TILE_UNIT = 25;
    private static boolean 	isDialogEarthShown = false;
    private static boolean 	isDialogFireShown = false;
    
    private SerialPort comPort;
    
    Animation                       girlRightAnimation;  
    Animation                       girlLeftAnimation; 
    Animation                       girlTopAnimation; 
    Animation                       girlBottomAnimation; 
    
    Texture                         girlSheet;              // #4
    Texture                         grassImage; 
    Texture                         moulinImage;              // #4
    Texture                         houseImage;
    Texture                         waterImage;
    Texture                         earthImage;
    Texture                         fireImage;
    Texture							dialogGirlEarthImage;
    Texture							dialogGirlFireImage;
    
    TextureRegion[]                 girlRightFrames;   
    TextureRegion[]                 girlLeftFrames;
    TextureRegion[]                 girlTopFrames;
    TextureRegion[]                 girlBottomFrames;
    
    SpriteBatch                     spriteBatch;            
    TextureRegion                   currentGirlFrame;       
    
    Array<Rectangle> grassList;
    Array<Rectangle> waterList;
    Array<Rectangle> treeList;
    Array<Rectangle> lightList;
    
    Rectangle moulin;
    Rectangle house;
    Rectangle girl = new Rectangle();
    
    float girlSpeed = 100.0f; // 100 pixels per second.
    float girlX = HEIGHT / 2;
    float girlY = WIDTH / 2;
    
    private int lastActionNum = 0;
    private int lastActionFireNum = 0;

   @Override
   public void create() {
	   girlSheet = new Texture(Gdx.files.internal("girl.png")); // #9
	   grassImage = new Texture(Gdx.files.internal("grass.jpg"));
	   moulinImage = new Texture(Gdx.files.internal("moulin.png"));
	   houseImage = new Texture(Gdx.files.internal("house.png"));
	   waterImage = new Texture(Gdx.files.internal("water.png"));
	   
	   earthImage = new Texture(Gdx.files.internal("earth-1.png"));
	   fireImage = new Texture(Gdx.files.internal("fire-1.png"));
			   
	   dialogGirlEarthImage = new Texture(Gdx.files.internal("girl-dialog-earth.png"));
	   dialogGirlFireImage = new Texture(Gdx.files.internal("girl-dialog-fire.png"));
	   
       TextureRegion[][] tmp = TextureRegion.split(girlSheet, girlSheet.getWidth()/FRAME_GIRL_COLS, girlSheet.getHeight()/FRAME_GIRL_ROWS);              // #10
       
       girlRightFrames = new TextureRegion[FRAME_GIRL_ROWS];
       girlLeftFrames = new TextureRegion[FRAME_GIRL_ROWS];
       girlTopFrames = new TextureRegion[FRAME_GIRL_ROWS];
       girlBottomFrames = new TextureRegion[FRAME_GIRL_ROWS];
       
       int index = 0;
       for (int j = 0; j < FRAME_GIRL_COLS; j++) {
    	   girlBottomFrames[index++] = tmp[0][j];
       }
       index = 0;
       for (int j = 0; j < FRAME_GIRL_COLS; j++) {
    	   girlLeftFrames[index++] = tmp[1][j];
       }
       index = 0;
       for (int j = 0; j < FRAME_GIRL_COLS; j++) {
    	   girlRightFrames[index++] = tmp[2][j];
       }
       index = 0;
       for (int j = 0; j < FRAME_GIRL_COLS; j++) {
    	   girlTopFrames[index++] = tmp[3][j];
       }
       
       girlRightAnimation = new Animation(0.1f, girlRightFrames);      
       girlLeftAnimation = new Animation(0.1f, girlLeftFrames);
       girlBottomAnimation = new Animation(0.1f, girlBottomFrames);
       girlTopAnimation = new Animation(0.1f, girlTopFrames);
       
       grassList = new Array<Rectangle>();
       waterList = new Array<Rectangle>();
       treeList = new Array<Rectangle>();
       lightList = new Array<Rectangle>();
       
       renderGround();
       renderHouse();
       renderMoulin();
       renderWater();
          
       spriteBatch = new SpriteBatch();  
       
       try{
	    	//comPort = SerialPort.getCommPort("/com6");
    	    comPort = SerialPort.getCommPorts()[0];
		    comPort.openPort();
		    comPort.addDataListener(new SerialPortDataListener() {
		       @Override
		       public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
		       @Override
		       public void serialEvent(SerialPortEvent event)
		       {
		          if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
		             return;
		          comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 5000, 0);
		          InputStream in = comPort.getInputStream();
		          try
		          {
		        	 String arduinoData = "";
		        	 char nextChar;
		             for (int j = 0; j < 100; ++j){
		            	 nextChar = (char)in.read();
		            	 if(nextChar != '\r')
		            		 arduinoData += nextChar;
		            	 else
		            		 break;
		             } 
		             	             
		             try{
		            	 JSONObject obj = new JSONObject(arduinoData);
		            	 System.out.println("earth action number:" + obj.getInt("earth"));
		            	 renderRandomTree(obj.getInt("earth"));
		            	 renderRandomLight(obj.getInt("fire"));
		             }catch(Exception e){
		            	 System.out.println("bad value:" + arduinoData);
		             }
	            	 
		             arduinoData = "";
		             in.close();
		          } catch (Exception e) { e.printStackTrace(); }
		       }
		  });	    	
	    }catch(Exception e){
	    	System.out.println("arduino not connected");
	    }
   }

   private void renderRandomTree(int actionNum) {
	  isDialogEarthShown = true;
	  System.out.println(lastActionNum + " -- " + actionNum);
	  
	  if(lastActionNum < actionNum){
		  for(int i = lastActionNum; i < actionNum; i++){
			  Rectangle tree = new Rectangle();
		      tree.x = MathUtils.random(0, WIDTH-64);
		      tree.y = MathUtils.random(0, HEIGHT-64);
		      tree.width = 64;
		      tree.height = 64;
		      treeList.add(tree);
		  }
	  }else{
		  Iterator<Rectangle> iter = treeList.iterator();
	      while(iter.hasNext() && lastActionNum > actionNum) {
	           iter.next();
	           iter.remove();
	           lastActionNum--;
	      }
	  }
		  
	  lastActionNum = actionNum;
   }
   
   private void renderRandomLight(int actionNum) {
	  isDialogFireShown = true;
	  
	  System.out.println(lastActionFireNum + " -- " + actionNum);
	  
	  if(lastActionFireNum < actionNum){
		  for(int i = lastActionFireNum; i < actionNum; i++){
			  Rectangle light = new Rectangle();
			  light.x = MathUtils.random(0, WIDTH-64);
			  light.y = MathUtils.random(0, HEIGHT-64);
			  light.width = 64;
			  light.height = 64;
		      lightList.add(light);
		  }
	  }else{
		  Iterator<Rectangle> iter = lightList.iterator();
	      while(iter.hasNext() && lastActionFireNum > actionNum) {
	           iter.next();
	           iter.remove();
	           lastActionFireNum--;
	      }
	  }
		  
	  lastActionFireNum = actionNum;
   }
   
   private void renderGround() {
	   for(int i = 0; i < HEIGHT; i += TILE_UNIT){
		   for(int j = 0; j < WIDTH; j += TILE_UNIT){
			  Rectangle grass = new Rectangle();
		      grass.x = i;
		      grass.y = j;
		      grass.width = TILE_UNIT;
		      grass.height = TILE_UNIT;
		      grassList.add(grass);
		   }
	   }
      
   }
   
   private void renderWater() {
	   int startIdx = WIDTH/2;
	   int endIdx = startIdx + 20;
	   Rectangle water;
	   
	   for(int i = 0; i < HEIGHT; i += TILE_UNIT){
		   water = new Rectangle();
		   water.x = startIdx;
	       water.y = i;
	       water.width = TILE_UNIT;
	       water.height = TILE_UNIT;
	       waterList.add(water);
	       
	       water = new Rectangle();
		   water.x = startIdx + 25;
	       water.y = i;
	       water.width = TILE_UNIT;
	       water.height = TILE_UNIT;
	       waterList.add(water);
	   }
   }
   
   private void renderMoulin() {
	  moulin = new Rectangle();
	  moulin.x = 400;
	  moulin.y = 400;
   }
   
   private void renderHouse() {
	  house = new Rectangle();
	  house.width = 200;
	  house.height = 200;
	  house.x = 50;
	  house.y = 300;
   }
   

   @Override
   public void render() {
	    girl.width = 64;
	    girl.height = 64;
	    
      if(Gdx.input.isKeyPressed(Keys.A)){
    	 renderRandomTree(5);
   	  	 isDialogEarthShown = true;
      }
      
      if(Gdx.input.isKeyPressed(Keys.B)){
    	 renderRandomTree(2);
    	 isDialogEarthShown = true;
       }
      
      if(Gdx.input.isKeyPressed(Keys.C)){
     	 renderRandomLight(5);
     	isDialogFireShown = true;
       }
       
       if(Gdx.input.isKeyPressed(Keys.D)){
    	 renderRandomLight(2);
      	 isDialogFireShown = true;
       }
       
      if(Gdx.input.isKeyPressed(Keys.ENTER)){
    	  isDialogEarthShown = false;
    	  isDialogFireShown = false;
      }
	   
	   Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        
	   girlSpeed += Gdx.graphics.getDeltaTime();           
       
	   currentGirlFrame = girlBottomAnimation.getKeyFrame(girlSpeed, true);
       
       if(Gdx.input.isKeyPressed(Keys.DPAD_LEFT)){
    	   girlX -= Gdx.graphics.getDeltaTime() * girlSpeed;
    	   currentGirlFrame = girlLeftAnimation.getKeyFrame(girlSpeed, true);
    	   girl.x = girlX;
       }
	   if(Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)){
		   girlX += Gdx.graphics.getDeltaTime() * girlSpeed;
		   currentGirlFrame = girlRightAnimation.getKeyFrame(girlSpeed, true);
		   girl.x = girlX;
	   }
	   if(Gdx.input.isKeyPressed(Keys.DPAD_UP)){
		   girlY += Gdx.graphics.getDeltaTime() * girlSpeed;
		   currentGirlFrame = girlTopAnimation.getKeyFrame(girlSpeed, true);
		   girl.y = girlY;
	   }
	   if(Gdx.input.isKeyPressed(Keys.DPAD_DOWN)){
		   girlY -= Gdx.graphics.getDeltaTime() * girlSpeed;
		   currentGirlFrame = girlBottomAnimation.getKeyFrame(girlSpeed, true);
		   girl.y = girlY;
	   }
	   
       spriteBatch.begin();
       
       
       for(Rectangle grassItem: grassList) {
    	   spriteBatch.draw(grassImage, grassItem.x, grassItem.y);
	   }
       for(Rectangle waterItem: waterList) {
    	   spriteBatch.draw(waterImage, waterItem.x, waterItem.y);
	   }
       
       spriteBatch.draw(currentGirlFrame, girlX, girlY);
       
       spriteBatch.draw(moulinImage, moulin.x, moulin.y);
       spriteBatch.draw(houseImage, house.x, house.y);
       
       
       int earthImgIdx = 0;
       for(Rectangle earthImg: treeList) {
    	   spriteBatch.draw(earthImage, earthImg.x, earthImg.y);
    	   earthImgIdx++;
       }

       int fireImgIdx = 0;
       for(Rectangle fireImg: lightList) {
    	   spriteBatch.draw(fireImage, fireImg.x, fireImg.y);
    	   fireImgIdx++;
       }
       
       
       if(isDialogEarthShown){
    	   spriteBatch.draw(dialogGirlEarthImage, 0, 0);
       }
       else if(isDialogFireShown){
    	   spriteBatch.draw(dialogGirlFireImage, 0, 0);
       }
       spriteBatch.end();
       
   }
   
   public void showGirlThank(){
	   
   }

   @Override
   public void dispose() {
      
   }
}
