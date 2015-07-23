package au.com.mineauz.minigames.minigame.reward.scheme;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.stats.MinigameStats;
import au.com.mineauz.minigames.stats.StoredStats;

public class TimeRewardScheme extends HierarchyRewardScheme<Integer> {
	@Override
	protected Integer decrement(Integer value) {
		return value - 1;
	}
	
	@Override
	protected Integer increment(Integer value) {
		return value + 1;
	}
	
	@Override
	protected Integer loadValue(String key) {
		int value = Integer.valueOf(key);
		if (value <= 0) {
			throw new IllegalArgumentException();
		}
		
		return value;
	}
	
	@Override
	protected String getMenuItemDescName(Integer value) {
		return "Time: " + MinigameUtils.convertTime(value, true);
	}
	
	@Override
	protected Integer getValue(MinigamePlayer player, StoredStats data, Minigame minigame) {
		return (int)(data.getStat(MinigameStats.CompletionTime) / 1000);
	}
	
	@Override
	protected String getMenuItemName(Integer value) {
		return MinigameUtils.convertTime(value, true);
	}
}
