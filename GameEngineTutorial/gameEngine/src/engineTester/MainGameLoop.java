package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {
	
	/**
	 *  @param args
	 */

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();

		
		//trees
		RawModel model = OBJLoader.loadObjFile("tree", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("tree"));
		TexturedModel texturedModel = new TexturedModel(model, texture); 
		texture.setShineDamper(20);
		texture.setReflectivity(1);
		
		//grass
		RawModel tallGrass = OBJLoader.loadObjFile("grassModel",loader);
		ModelTexture TGTex = new ModelTexture(loader.loadTexture("grassTexture"));
		TexturedModel tallGrassTex = new TexturedModel(tallGrass, TGTex);
		TGTex.setShineDamper(20);
		TGTex.setReflectivity(1);
		TGTex.setHasTransparency(true);
		TGTex.setHasFakeLighting(true);
		
		
		//fern
		RawModel fern = OBJLoader.loadObjFile("fern", loader);
		ModelTexture fernTex = new ModelTexture(loader.loadTexture("fern"));
		TexturedModel texturedFern = new TexturedModel(fern, fernTex);
		
		
		
		List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(texturedModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }
        
        for(int i=0 ; i<40 ; i++){
        	entities.add(new Entity(texturedFern, new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,1));
        }
        entities.add(new Entity(tallGrassTex, new Vector3f(0,0,-50),0,0,0,3));
        
		
		Terrain terrain = new Terrain(-1,-1,loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain1 = new Terrain(0,-1,loader, new ModelTexture(loader.loadTexture("grass")));

		Camera camera = new Camera();
        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()) {
			
			//game logic
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain1);
			for(Entity entity:entities){
                renderer.processEntity(entity);
            }
			camera.move();

			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}
}
