package de.mybukkit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Play implements Screen {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Player player;
	private int[] background = new int[] {0}, foreground = new int[] {1};
	
	private ShapeRenderer sr;
	private int level = Player.next;
	
		
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
		camera.update();
		renderer.setView(camera);
		renderer.render();
		//player
		renderer.render(background);
		
		renderer.getSpriteBatch().begin();
	  	player.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		
		//renderer.render(foreground);
		
//		sr.setProjectionMatrix(camera.combined);
//		for(MapObject object : map.getLayers().get("objects").getObjects())
//			if(object instanceof RectangleMapObject) {
//				RectangleMapObject rectObject = (RectangleMapObject) object;
//				Rectangle rect = rectObject.getRectangle();
//				if(rectObject.getProperties().containsKey("gid")) { // if it contains the gid key, it's an image object from Tiled
//					int gid = rectObject.getProperties().get("gid", Integer.class);
//					TiledMapTile tile = map.getTileSets().getTile(gid);
//					renderer.getSpriteBatch().begin();
//					renderer.getSpriteBatch().draw(tile.getTextureRegion(), rect.x, rect.y);
//					renderer.getSpriteBatch().end();
//				} else { // otherwise, it's a normal RectangleMapObject
//					sr.begin(ShapeType.Filled);
//					sr.rect(rect.x, rect.y, rect.width, rect.height);
//					sr.end();
//				}
//			} else if(object instanceof CircleMapObject) {
//				Circle circle = ((CircleMapObject) object).getCircle();
//				sr.begin(ShapeType.Filled);
//				sr.circle(circle.x, circle.y, circle.radius);
//				sr.end();
//			} else if(object instanceof EllipseMapObject) {
//				Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
//				sr.begin(ShapeType.Filled);
//				sr.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
//				sr.end();
//			} else if(object instanceof PolylineMapObject) {
//				Polyline line = ((PolylineMapObject) object).getPolyline();
//				sr.begin(ShapeType.Line);
//				sr.polyline(line.getTransformedVertices());
//				sr.end();
//			} else if(object instanceof PolygonMapObject) {
//				Polygon poly = ((PolygonMapObject) object).getPolygon();
//				sr.begin(ShapeType.Line);
//				sr.polygon(poly.getTransformedVertices());
//				sr.end();
//			}
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 1.5f;
		camera.viewportHeight = height / 1.5f;
		
	}

	@Override
	public void show() {
		
		map = new TmxMapLoader().load("maps/map"+level +".tmx");

		renderer = new OrthogonalTiledMapRenderer(map);
//		sr = new ShapeRenderer();
//		sr.setColor(Color.CYAN);
		Gdx.gl.glLineWidth(3);
		player = new Player(new Sprite(new Texture("player/player.png")), (TiledMapTileLayer) map.getLayers().get(0));

		camera = new OrthographicCamera();
		
		player.setPosition(1 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 37) * player.getCollisionLayer().getTileHeight());

		Gdx.input.setInputProcessor(player);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("background");
		
		

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		sr.dispose();
		

	}

}
