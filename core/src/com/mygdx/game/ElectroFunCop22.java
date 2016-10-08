package com.mygdx.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.graphics.GL30;;

public class ElectroFunCop22 extends ApplicationAdapter {
	
	private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance modelInstance;
    private Environment environment;
    private AnimationController controller;
    private TextField txtUsername;
    Skin skin;
    
    private String yourName;
    BitmapFont yourBitmapFontName;
    
    public Stage stage;
    public Label label;
    public BitmapFont font;
	
	@SuppressWarnings("deprecation")
	@Override
	public void create () {
		stage = new Stage();
		font = new BitmapFont();
		camera = new PerspectiveCamera(75,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        camera.position.set(0f,100f,100f);
        camera.lookAt(0f,1f,0f);

        camera.near = 0.1f;
        camera.far = 300.0f;

        modelBatch = new ModelBatch();

        UBJsonReader jsonReader = new UBJsonReader();

        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.getFileHandle("data/old-man.g3db", FileType.Internal));
        modelInstance = new ModelInstance(model);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.8f,0.8f,0.8f,1.0f));
	
        controller = new AnimationController(modelInstance);
        controller.setAnimation("Default Take", -1, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animation) {
            	MyTextInputListener listener = new MyTextInputListener();
                Gdx.input.getTextInput(listener, "Give us your name", "", "");
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
                Gdx.app.log("INFO","Animation Ended");
            }
        });
        
        
		
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        controller.update(Gdx.graphics.getDeltaTime());

        modelBatch.begin(camera);
        modelBatch.render(modelInstance);
        modelBatch.end();
        
        stage.draw();
        
	}
	
	public class MyTextInputListener implements TextInputListener {
	   @Override
	   public void input (String text) {
		   
	       label = new Label("Welcome " + text, new Label.LabelStyle(font, Color.WHITE));
           stage.addActor(label);
           
	   }

	   @Override
	   public void canceled () {
	   }
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
        model.dispose();
	}
}
