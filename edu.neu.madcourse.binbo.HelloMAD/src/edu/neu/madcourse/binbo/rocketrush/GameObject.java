package edu.neu.madcourse.binbo.rocketrush;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Canvas;

public class GameObject {
	public final static int UNKNOWN    = 0;
	public final static int ASTEROID   = 1;
	public final static int BACKGROUND = 2;
	public final static int ROCKET	   = 3;
	public final static int SPEEDBAR   = 4;
	
	protected int mX = 0;
	protected int mY = 0;
	protected int mWidth = 0;
	protected int mHeight = 0;
	protected int mSpeedX = 0;
	protected int mSpeedY = 0;
	protected int mKind   = UNKNOWN;
	protected int mZOrder = 0;
	protected boolean mVisible = true;
	protected boolean mMovable = false;
	
	protected Resources mRes = null;
	protected OnCollideListener mOnCollideListener = null;
	
	protected GameObject(Resources res) {	
		setResources(res);
	}
	
	public void setOnCollideListener(OnCollideListener listener) {
		mOnCollideListener = listener;
	}
	
	public OnCollideListener getOnCollideListener() {
		return mOnCollideListener;
	}
	
	public void release() {
		// release resources here
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
	
	public void setKind(int kind) { mKind = kind; }
	
	public int getKind() { return mKind; }
	
	public void setZOrder(int order) { mZOrder = order; }
	
	public int getZOrder() { return mZOrder; }
	
	public void setVisible(boolean visible) { mVisible = visible; }
	
	public boolean getVisible() { return mVisible; }
	
	public void setMovable(boolean movable) { mMovable = movable; }
	
	public boolean getMovable() { return mMovable; }
	
	public void setResources(Resources res) { mRes = res; }
	
	public String toJSONString() {
		JSONObject obj = new JSONObject();  	              

        try {
			obj.put("x", getX());
			obj.put("y", getY());
	        obj.put("width", getWidth()); 
	        obj.put("height", getHeight());
	        obj.put("speed_x", getSpeedX());
	        obj.put("spped_y", getSpeedY());
	        obj.put("z_order", getZOrder());
	        obj.put("visible", getVisible());
	        obj.put("movable", getMovable());
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
			setZOrder(obj.getInt("z_order"));
			setVisible(obj.getBoolean("visible"));
			setMovable(obj.getBoolean("movable"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}							
	}
	
	public boolean isVisible() { return mVisible; }
	
	public boolean isMovable() { return mMovable; }
	
	public void operate(GameCtrl ctrl) {}
	
	public void detectCollision(List<GameObject> objects) {}
	
	public void update() {}

	public void doDraw(Canvas c) {
		// TODO Auto-generated method stub
	}
	
	public void onSizeChanged(int width, int height) {
		// invoked when the surface size is changed
	}
	
	public interface IDrawer {
		void doDraw(Canvas c);
	}
	
	public interface OnCollideListener {
		void onCollide(GameObject obj, List<GameObject> collideWith);
	}
}