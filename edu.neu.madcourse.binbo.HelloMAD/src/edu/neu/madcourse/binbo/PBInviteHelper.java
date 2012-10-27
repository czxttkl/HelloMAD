package edu.neu.madcourse.binbo;

import java.util.ArrayList;

public class PBInviteHelper {

	public PBInviteHelper() {
	}

	public ArrayList<PBPlayerInfo> updatePlayerStatusList(
			ArrayList<PBPlayerInfo> oldList, ArrayList<PBPlayerInfo> newList) {
		ArrayList<PBPlayerInfo> diffList = new ArrayList();
		for (int i = 0; i < newList.size(); i++) {
			PBPlayerInfo newInfo = newList.get(i);
			PBPlayerInfo oldInfo = null;
			if ((oldInfo = findOldPlayerInfo(newInfo, oldList)) == null) {
				diffList.add(newInfo);
			} else {
				if (diffStatus(newInfo, oldInfo)) {
					diffList.add(newInfo);
				}
			}
		}
		return diffList;
	}

	public PBPlayerInfo findOldPlayerInfo(PBPlayerInfo newInfo,
			ArrayList<PBPlayerInfo> oldList) {
		for (int i = 0; i < oldList.size(); i++) {
			PBPlayerInfo oldInfo = oldList.get(i);
			if (newInfo.mName.equals(oldInfo.mName))
				return oldInfo;
		}
		return null;
	}

	public boolean diffStatus(PBPlayerInfo newInfo, PBPlayerInfo oldInfo) {
		if (newInfo.mStatus.equals(oldInfo.mStatus)
				&& !newInfo.mStatus.equals("online") && !newInfo.mStatus.equals("offline")
				&& !newInfo.mStatus.equals("inviting") && !newInfo.mStatus.equals("playing"))
			return false;
		return true;
	}

	public Boolean isInvited(String userName, PBInviteInfo pbInviteInfo) {
		return userName.equals(pbInviteInfo.mReceiver);
	}
	
	public String convert2InviteStatus(PBPlayerInfo playerInfo) {
		if (!playerInfo.mStatus.toLowerCase().equals("online") &&
				!playerInfo.mStatus.toLowerCase().equals("offline") &&
				!playerInfo.mStatus.toLowerCase().equals("inviting") &&
				!playerInfo.mStatus.toLowerCase().equals("playing"))
			return "playing";
		return playerInfo.mStatus;
	}

}
