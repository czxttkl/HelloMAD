package edu.neu.madcourse.binbo;

public class PBPlayerInfo {
	
	protected String mName = "";
	protected String mStatus = "";
	protected int mScore = 0;
	protected int mBestScore = 0;
	protected long mUpdateTime = 0;	
	
	public void setName(String name) {  
        mName = name;  
	} 
	
	public void setStatus(String status) {  
		mStatus = status;  
	} 
	
	public void setScore(int score) {  
        mScore = score;  
	} 
	
	public void setBestScore(int bestScore) {  
		mBestScore = bestScore;  
	} 
	
	public void setUpdateTime(long updateTime) {  
		mUpdateTime = updateTime;  
	} 
	
	public String getName() {  
        return mName;  
	} 
	
	public String getStatus() {  
		return mStatus;  
	} 
	
	public int getScore() {  
        return mScore;  
	} 
	
	public int getBestScore() {  
		return mBestScore;  
	} 
	
	public long getUpdateTime() {  
		return mUpdateTime;  
	} 
}
