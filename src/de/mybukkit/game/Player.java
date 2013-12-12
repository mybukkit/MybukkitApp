package de.mybukkit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

	/** the movement velocity */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2, gravity = 60 * 1.9f, increment;// animationTime
																	// = 0,

	private boolean canJump;

	private TiledMapTileLayer collisionLayer;

	private String blockedKey = "blocked";

	public String newlevelKey = "newlevel";

	private String climpKey = "climp";

	private boolean collisionYc;

	private boolean climb;

	private boolean collisionYco;

	private boolean collisionYcu;

	private boolean collisionYcm;

	private boolean climbdown;

	private boolean climbup;

	public static int next = 1;

	public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
		super(sprite);
		this.collisionLayer = collisionLayer;
		setSize(collisionLayer.getWidth() * 1.5F,
				collisionLayer.getHeight() * 1.5f);

	}

	@Override
	public void draw(Batch spritebatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spritebatch);
	}

	public void update(float delta) {
		// apply gravity
		velocity.y -= gravity * delta;

		// clamp velocity
		if (velocity.y > speed)
			velocity.y = speed;
		else if (velocity.y < -speed)
			velocity.y = -speed;

		// save old position
		float oldX = getX(), oldY = getY();
		boolean collisionX = false, collisionY = false;
		collisionYc = false;

		// move on x
		setX(getX() + velocity.x * delta);

		// calculate the increment for step in #collidesLeft() and
		// #collidesRight()
		increment = collisionLayer.getTileWidth();
		increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

		if (velocity.x < 0) // going left
			collisionX = collidesLeft();
		else if (velocity.x > 0) // going right
			collisionX = collidesRight();

		// react to x collision
		if (collisionX) {
			setX(oldX);
			velocity.x = 0;
		}
		collisionYco = collidesclimpoben();
		collisionYcu = collidesclimpunten();
		collisionYcm = collidesclimpmitte();

		// move on y
		setY(getY() + velocity.y * delta * 4);

		// calculate the increment for step in #collidesBottom() and
		// #collidesTop()
		increment = collisionLayer.getTileHeight();
		increment = getHeight() < increment ? getHeight() / 5 : increment / 5;

		if (velocity.y < 0) { // going down
			canJump = collisionY = collidesBottom();

		} else if (velocity.y > 0) { // going up
			collisionY = collidesTop();
		}
		if (collisionYcm == true) {
			if (climbup == false) {
				velocity.y = 0;
			}
			climb = true;
			gravity = 0;
			climbdown = true;
		} else if (collisionYco == true && collisionYcm == false) {
			gravity = 0;
			if (collisionY == false && (velocity.y >= 0 || velocity.y == 0)){
				collisionY = true;
				velocity.y = 0;
				setY(oldY);
				canJump = true;
			}
			if (collisionY == true && velocity.y == 0){
				collisionY = false;
			}
			
			climb = false;
			climbdown = true;
			

		} else if (collisionYco == true) {

			climbdown = true;
		} else {
			climb = false;
			gravity = 60 * 1.9f;
			climbdown = false;

		}

		// react to y collision
		if (collisionY) {
			setY(oldY);
			velocity.y = 0;
		}

	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell(
				(int) (x / collisionLayer.getTileWidth()),
				(int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(blockedKey);
	}

	private boolean isCellnextlevel(float x, float y) {
		Cell cell = collisionLayer.getCell(
				(int) (x / collisionLayer.getTileWidth()),
				(int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(newlevelKey);

	}

	private boolean isCellclimp(float x, float y) {
		Cell cell = collisionLayer.getCell(
				(int) (x / collisionLayer.getTileWidth()),
				(int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(climpKey);

	}

	public boolean collidesRight() {
		for (float step = 0; step <= getHeight(); step += increment)
			if (isCellBlocked(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesLeft() {
		for (float step = 0; step <= getHeight(); step += increment)
			if (isCellBlocked(getX(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesTop() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

	public boolean collidesBottom() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}

	private boolean collidesclimpoben() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellclimp(getX() + step, getY() - step))
				return true;
		return false;
	}

	private boolean collidesclimpunten() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellclimp(getX() + step, getY() + (getHeight() / 2)))
				return true;
		return false;
	}

	private boolean collidesclimpmitte() {
		for (float step = 0; step <= getWidth(); step += increment)
			if (isCellclimp(getX() + step, getY()))
				return true;
		return false;
	}

	public boolean collidesnewlevel() {
		if (isCellnextlevel(getX(), getY()))
			return true;
		return false;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public void canclimp() {

		if (climb == true) {
			velocity.y = speed / 2;

		}/*
		 * else { velocity.y = 0; gravity = 60 * 1.9f;
		 * 
		 * }
		 */

	}

	public void jump() {
		if (canJump == true && collisionYcm == false) {
			velocity.y = speed;
			canJump = false;
		}

	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W:
			canclimp();
			jump();
			climbup = true;
			break;
		case Keys.A:
			velocity.x = -speed;
			// animationTime = 0;
			break;
		case Keys.D:
			velocity.x = speed;
			// animationTime = 0;
			break;
		case Keys.S:
			if (climbdown == true) {
				velocity.y = -speed / 2;
				break;
			}
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {

		case Keys.W:

			if (climb == true) {
				velocity.y = 0;

			}
			break;
		case Keys.S:
			velocity.y = 0;
		case Keys.A:
		case Keys.D:
			velocity.x = 0;
			// animationTime = 0;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
