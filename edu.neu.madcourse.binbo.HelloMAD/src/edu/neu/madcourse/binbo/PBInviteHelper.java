package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class PBInviteHelper {
	
	public PBInviteHelper(){
	}
	
	public List<PBPlayerInfo> updatePlayerStatusList(List<PBPlayerInfo> oldList, List<PBPlayerInfo> newList){
		List<PBPlayerInfo> diffList = new ArrayList();
		for(int i=0; i<newList.size(); i++){
			PBPlayerInfo newInfo = newList.get(i);
			PBPlayerInfo oldInfo = null;
			if((oldInfo=findOldPlayerInfo(newInfo,oldList))==null){
				diffList.add(newInfo);
			}
			else{
				if(diffStatus(newInfo,oldInfo)){
					diffList.add(newInfo);
				}
			}
		}
		return null;
	}
	
	public PBPlayerInfo findOldPlayerInfo(PBPlayerInfo newInfo,List<PBPlayerInfo> oldList){
		for(int i=0; i<oldList.size(); i++){
			PBPlayerInfo oldInfo = oldList.get(i);
			if(newInfo.mName.equals(oldInfo.mName))
				return oldInfo;
		}
		return null;
	}
	
	public boolean diffStatus(PBPlayerInfo newInfo,PBPlayerInfo oldInfo){
		if(newInfo.mStatus.equals(oldInfo.mStatus)
				&&!newInfo.equals("online")
				&&!newInfo.equals("offline")
				&&!newInfo.equals("inviting")
				&&!newInfo.equals("playing"))
			return false;
		return true;
	}
	
	public Boolean isInvited(String userName, PBInviteInfo pbInviteInfo){
		return userName.equals(pbInviteInfo.mReceiver);
	}
	
}
