package edu.neu.madcourse.binbo.rocketrush;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Canvas;

public class GameObject implements IDrawer {
	protected int mX = 0;
	protected int mY = 0;
	protected int mWidth = 0;
	protected int mHeight = 0;
	protected int mSpeedX = 0;
	protected int mSpeedY = 0;
	protected boolean mVisible = true;
	
	protected GameObject() {		
	}
	
	public void setX(int x) { mX = x; }
	
	public int getX() { return mX; }
	
	public void setY(int y) { mY = y; }
	
	public int getY() { return mY; }
	
	public void setWidth(int width) { mWidth = width; }
	
	public int getWidth() { return mWidth; }
	
	public void setHeight(int height) { mHeight = height; }
	
	public int getHeight() { return mHeight; }
	
	public void setSpeed(int x, int y) { setSpeedX(x); setSpeedY(y); }
	
	public void setSpeedX(int x) { mSpeedX = x; }
	
	public void setSpeedY(int y) { mSpeedY = y; }
	
	public int getSpeedX() { return mSpeedX; }
	
	public int getSpeedY() { return mSpeedY; }
	
	public void setVisible(boolean visible) { mVisible = visible; }
	
	public boolean getVisible() { return mVisible; }
	
	public String toJSONString() {
		JSONObject obj = new JSONObject();  	              

        try {
			obj.put("x", getX());
			obj.put("y", getY());
	        obj.put("width", getWidth()); 
	        obj.put("height", getHeight());
	        obj.put("speed_x", getSpeedX());
	        obj.put("spped_y", getSpeedY());
	        obj.put("visible", getVisible());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
        return obj.toString();
	}
    
	public void fromJSONString(String strJSON) {
		JSONObject obj;
		
		try {
			obj = new JSONObject(strJSON);
			setX(obj.getInt("x"));
			setY(obj.getInt("y"));
			setWidth(obj.getInt("width"));
			setHeight(obj.getInt("height"));
			setSpeed(obj.getInt("speed_x"), obj.getInt("speed_y"));
			setVisible(obj.getBoolean("visible"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}							
	}
	
	public void update() {}

	public int doDraw(Canvas c) {
		// TODO Auto-generated method stub
		return 0;
	}
}