package com.mygdx.game;

import java.io.FileNotFoundException;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

public class ElectroFunCop22 extends ApplicationAdapter {
//	private OrthographicCamera camera;
//	private SpriteBatch batch;
//	private Texture dropImage;
//	private Texture bucketImage;
//	private Sound dropSound;
//	private Music rainMusic;
//	
//	private Array<Rectangle> raindrops;
//	private long lastDropTime;
//	
//	private Rectangle bucket;
//	
//	private Vector3 touchPos;
	
	VideoPlayer videoPlayer;
	Stage stage;

    boolean videoLoaded = false;
    
	@Override
	public void create () {
//		batch = new SpriteBatch();
//		   
//		// load the images for the droplet and the bucket, 64x64 pixels each
//	    dropImage = new Texture(Gdx.files.internal("droplet.png"));
//	    bucketImage = new Texture(Gdx.files.internal("bucket.png"));
//	
//	    // load the drop sound effect and the rain background "music"
//	    dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
//	    rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
//	
//	    // start the playback of the background music immediately
//	    rainMusic.setLooping(true);
//	    rainMusic.play();
//	    
//	    camera = new OrthographicCamera();
//	    camera.setToOrtho(false, 800, 480);
//	    
//	    bucket = new Rectangle();
//	    bucket.x = 800 / 2 - 64 / 2;
//	    bucket.y = 20;
//	    bucket.width = 64;
//	    bucket.height = 64;
//	    
//	    raindrops = new Array<Rectangle>();
//	    spawnRaindrop();
		
		stage = new Stage(new ScreenViewport());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        Label.LabelStyle lstyle =  new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Label l = new Label("Stage is here!", lstyle);
        Table t = new Table();
        t.add(l).expand().fill();
        t.setFillParent(true);

        stage.addActor(t);

        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        videoPlayer.setOnVideoSizeListener(new VideoPlayer.VideoSizeListener() {
            @Override
            public void onVideoSize(float v, float v2) {
                videoLoaded = true;
            }
        });

        try {
            FileHandle fh = Gdx.files.internal("simple.mp4");
            Gdx.app.log("TEST", "Loading file : " + fh.file().getAbsolutePath());
            videoPlayer.play(fh);
        } catch (FileNotFoundException e) {
            Gdx.app.log("TEST", "Err: " + e);
        }
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
//	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//	    
//	    camera.update();
//		
//		batch.setProjectionMatrix(camera.combined);
//	    batch.begin();
//	    batch.draw(bucketImage, bucket.x, bucket.y);
//	    for(Rectangle raindrop: raindrops) {
//	       batch.draw(dropImage, raindrop.x, raindrop.y);
//	    }
//	    batch.end();
//	    
//	    if(Gdx.input.isTouched()) {
//	        touchPos = new Vector3();
//	        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//	        camera.unproject(touchPos);
//	        bucket.x = touchPos.x - 64 / 2;
//	    }
//	    
//	    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
//	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
//	    if(bucket.x < 0) bucket.x = 0;
//	    if(bucket.x > 800 - 64) bucket.x = 800 - 64;
//	    
//	    if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
//	    
//	    Iterator<Rectangle> iter = raindrops.iterator();
//	    while(iter.hasNext()) {
//	       Rectangle raindrop = iter.next();
//	       raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
//	       if(raindrop.y + 64 < 0) iter.remove();
//	       if(raindrop.overlaps(bucket)) {
//		       dropSound.play();
//		       iter.remove();
//		   }
//	    }
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(videoLoaded) {
            if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
                try {
                    videoLoaded = false;
                    videoPlayer.play(Gdx.files.internal("simple.mp4"));
                    Gdx.app.log("TEST", "Started new video");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}
	
	@Override
	public void dispose () {
//	  dropImage.dispose();
//      bucketImage.dispose();
//      dropSound.dispose();
//      rainMusic.dispose();
//      batch.dispose();
	}
//	
//	private void spawnRaindrop() {
//      Rectangle raindrop = new Rectangle();
//      raindrop.x = MathUtils.random(0, 800-64);
//      raindrop.y = 480;
//      raindrop.width = 64;
//      raindrop.height = 64;
//      raindrops.add(raindrop);
//      lastDropTime = TimeUtils.nanoTime();
//   }
}
