package edu.neu.madcourse.binbo;

import org.json.JSONException;
import org.json.JSONObject;

public class GameObject {
	protected int mX = 0;
	protected int mY = 0;
	protected int mWidth = 0;
	protected int mHeight = 0;
	protected int mSpeed = 0;
	protected boolean mVisible = true;	
	
	public void setX(int x) { mX = x; }
	
	public int getX() { return mX; }
	
	public void setY(int y) { mY = y; }
	
	public int getY() { return mY; }
	
	public void setWidth(int width) { mWidth = width; }
	
	public int getWidth() { return mWidth; }
	
	public void setHeight(int height) { mHeight = height; }
	
	public int getHeight() { return mHeight; }
	
	public void setSpeed(int speed) { mSpeed = speed; }
	
	public int getSpeed() { return mSpeed; }
	
	public void setVisible(boolean visible) { mVisible = visible; }
	
	public boolean getVisible() { return mVisible; }
	
	public String toJSONString() throws JSONException {
		JSONObject obj = new JSONObject();  	              

        obj.put("x", getX());
        obj.put("y", getY());
        obj.put("width", getWidth()); 
        obj.put("height", getHeight());
        obj.put("speed", getSpeed());
        obj.put("visible", getVisible());
        
        return obj.toString();
	}
    
	public void fromJSONString(String strJSON) throws JSONException {
		JSONObject obj = new JSONObject(strJSON);		
		
		setX(obj.getInt("x"));
		setY(obj.getInt("y"));
		setWidth(obj.getInt("width"));
		setHeight(obj.getInt("height"));
		setSpeed(obj.getInt("speed"));
		setVisible(obj.getBoolean("visible"));	
	}
}