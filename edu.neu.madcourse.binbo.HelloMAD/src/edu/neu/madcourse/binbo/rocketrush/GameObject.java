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
	
	protected float mX = 0;
	protected float mY = 0;
	protected int mWidth = 0;
	protected int mHeight = 0;
	protected float mSpeedX = 0;
	protected float mSpeedY = 0;
	protected float mMaxSpeedX = 0;
	protected float mAccSpeedX = 0;
	protected float mMaxSpeedY = 0;	
	protected float mAccSpeedY = 0;
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
	
	public void setX(float x) { mX = x; }
	
	public float getX() { return mX; }
	
	public void setY(float y) { mY = y; }
	
	public float getY() { return mY; }
	
	public void setWidth(int width) { mWidth = width; }
	
	public int getWidth() { return mWidth; }
	
	public void setHeight(int height) { mHeight = height; }
	
	public int getHeight() { return mHeight; }
	
	public void setSpeed(float x, float y) { setSpeedX(x); setSpeedY(y); }
	
	public void setSpeedX(float x) { mSpeedX = x; }
	
	public void setSpeedY(float y) { mSpeedY = y; }
	
	public float getSpeedX() { return mSpeedX; }
	
	public float getSpeedY() { return mSpeedY; }
	
	public void setMaxSpeed(float x, float y) { setMaxSpeedX(x); setMaxSpeedY(y); }
	
	public void setMaxSpeedX(float x) { mMaxSpeedX = x; }
	
	public void setMaxSpeedY(float y) { mMaxSpeedY = y; }
	
	public float getMaxSpeedX() { return mMaxSpeedX; }
	
	public float getMaxSpeedY() { return mMaxSpeedY; }
	
	public void setAccSpeed(float x, float y) { setAccSpeedX(x); setAccSpeedY(y); }
	
	public void setAccSpeedX(float x) { mAccSpeedX = x; }
	
	public void setAccSpeedY(float y) { mAccSpeedY = y; }
	
	public float getAccSpeedX() { return mAccSpeedX; }
	
	public float getAccSpeedY() { return mAccSpeedY; }
	
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
			obj.put("x", mX);
			obj.put("y", mY);
	        obj.put("width", mWidth); 
	        obj.put("height", mHeight);
	        obj.put("speed_x", mSpeedX);
	        obj.put("speed_y", mSpeedY);
	        obj.put("max_speed_x", mMaxSpeedX);
	        obj.put("max_speed_y", mMaxSpeedY);
	        obj.put("acc_speed_x", mAccSpeedX);
	        obj.put("acc_speed_y", mAccSpeedY);
	        obj.put("z_order", mZOrder);
	        obj.put("visible", mVisible);
	        obj.put("movable", mMovable);
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
			mX = (float)obj.getDouble("x");
			mY = (float)obj.getDouble("y");
			mWidth  = obj.getInt("width");
			mHeight = obj.getInt("height");
			mSpeedX = (float)obj.getDouble("speed_x");
			mSpeedY = (float)obj.getDouble("speed_y");
			mMaxSpeedX = (float)obj.getDouble("max_speed_x");
			mMaxSpeedY = (float)obj.getDouble("max_speed_y");
			mAccSpeedX = (float)obj.getDouble("acc_speed_x");
			mAccSpeedY = (float)obj.getDouble("acc_speed_y");
			mZOrder  = obj.getInt("z_order");
			mVisible = obj.getBoolean("visible");
			mMovable = obj.getBoolean("movable");
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