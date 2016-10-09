package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class ElectroFunCop22 extends ApplicationAdapter {
   private Texture dropImage;
   private Texture bucketImage;
   private SpriteBatch batch;
   private OrthographicCamera camera;
   private Rectangle bucket;
   private Array<Rectangle> raindrops;
   private Array<Rectangle> buckets;
   private long lastDropTime;

   @Override
   public void create() {
      // load the images for the droplet and the bucket, 64x64 pixels each
      dropImage = new Texture(Gdx.files.internal("droplet.png"));
      bucketImage = new Texture(Gdx.files.internal("bucket.png"));

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, 800, 480);
      batch = new SpriteBatch();
      // create the raindrops array and spawn the first raindrop
      raindrops = new Array<Rectangle>();
      buckets = new Array<Rectangle>();
   }

   private void spawnRaindrop() {
      Rectangle raindrop = new Rectangle();
      raindrop.x = MathUtils.random(0, 800-64);
      raindrop.y = MathUtils.random(0, 800-64);
      raindrop.width = 64;
      raindrop.height = 64;
      raindrops.add(raindrop);
   }
   private void spawnBucket() {
      Rectangle bucket = new Rectangle();
      bucket.x = MathUtils.random(0, 800-64);
      bucket.y = MathUtils.random(0, 800-64);
      bucket.width = 64;
      bucket.height = 64;
      buckets.add(bucket);
   }

   @Override
   public void render() {
      // clear the screen with a dark blue color. The
      // arguments to glClearColor are the red, green
      // blue and alpha component in the range [0,1]
      // of the color to be used to clear the screen.
      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      // tell the camera to update its matrices.
      camera.update();

      // tell the SpriteBatch to render in the
      // coordinate system specified by the camera.
      batch.setProjectionMatrix(camera.combined);

      // begin a new batch and draw the bucket and
      // all drops
      batch.begin();
      for(Rectangle bucket: buckets) {
         batch.draw(bucketImage, bucket.x, bucket.y);
      }
      for(Rectangle raindrop: raindrops) {
          batch.draw(dropImage, raindrop.x, raindrop.y);
      }
      batch.end();

      if(Gdx.input.isKeyPressed(Keys.A)){
    	  spawnBucket();
      }
      if(Gdx.input.isKeyPressed(Keys.B)){
    	  spawnRaindrop();
      }
      if(Gdx.input.isKeyPressed(Keys.C)){
    	  	Iterator<Rectangle> iter = buckets.iterator();
	        while(iter.hasNext()) {
	           iter.next();
	           iter.remove();
	           break;
	        }
      }
      if(Gdx.input.isKeyPressed(Keys.D)){
    	  Iterator<Rectangle> iter = raindrops.iterator();
	        while(iter.hasNext()) {
	           iter.next();
	           iter.remove();
	           break;
	        }
      }

   }

   @Override
   public void dispose() {
      // dispose of all the native resources
      dropImage.dispose();
      bucketImage.dispose();
      batch.dispose();
   }
}
