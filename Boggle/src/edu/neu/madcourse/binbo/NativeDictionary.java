package edu.neu.madcourse.binbo;

import android.content.res.AssetManager;

public class NativeDictionary {
	
	AssetManager mAssetManager = null;
	
	static {
		System.loadLibrary("huffman");
        System.loadLibrary("dictionary");
    }
	
	NativeDictionary(AssetManager assetManager) {
		// do nothing but make sure the library has been loaded before debugging,
		// or the breakpoints can not be added to native code.
		mAssetManager = assetManager;
		
		create();
	}

   /**
    * @param width the current view width
    * @param height the current view height
    */
	public native int create();
	public native int destroy();
    public native int load(String path, String name, AssetManager assetManager);
    public native int unload(String name);
    public native boolean isLoaded(String name);
    public native boolean lookupWord(String name);
}
