package com.pauldavdesign.mineauz.minigames.menu;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;
import com.pauldavdesign.mineauz.minigames.PlayerLoadout;
import com.pauldavdesign.mineauz.minigames.minigame.Minigame;

public class MenuItemDisplayLoadout extends MenuItem{
	
	private PlayerLoadout loadout = null;
	private Minigame mgm = null;
	private boolean allowDelete = true;
	
	public MenuItemDisplayLoadout(String name, Material displayItem, PlayerLoadout loadout, Minigame minigame) {
		super(name, displayItem);
		this.loadout = loadout;
		mgm = minigame;
	}

	public MenuItemDisplayLoadout(String name, List<String> description, Material displayItem, PlayerLoadout loadout, Minigame minigame) {
		super(name, description, displayItem);
		this.loadout = loadout;
		mgm = minigame;
	}
	
	@Override
	public ItemStack onClick(){
		Menu loadoutMenu = new Menu(5, loadout.getName(), getContainer().getViewer());
		
		loadoutMenu.setAllowModify(true);
		loadoutMenu.setPreviousPage(getContainer());

		loadoutMenu.addItem(new MenuItemDisplayPotions("Edit Potion Effects", Material.POTION, loadout), 43);
		loadoutMenu.addItem(new MenuItemSaveLoadout("Save Loadout", Material.REDSTONE_TORCH_ON, loadout), 44);
		
		for(int i = 40; i < 43; i++){
			loadoutMenu.addItem(new MenuItem("", null), i);
		}
		loadoutMenu.displayMenu(getContainer().getViewer());
		
		for(Integer item : loadout.getItems()){
			if(item < 100)
				loadoutMenu.addItemStack(loadout.getItem(item), item);
			else if(item == 100)
				loadoutMenu.addItemStack(loadout.getItem(item), 39);
			else if(item == 101)
				loadoutMenu.addItemStack(loadout.getItem(item), 38);
			else if(item == 102)
				loadoutMenu.addItemStack(loadout.getItem(item), 37);
			else if(item == 103)
				loadoutMenu.addItemStack(loadout.getItem(item), 36);
		}
		
		return null;
	}
	
	@Override
	public ItemStack onRightClick(){
		if(allowDelete){
			MinigamePlayer ply = getContainer().getViewer();
			ply.setNoClose(true);
			ply.getPlayer().closeInventory();
			ply.sendMessage("Delete the " + loadout.getName() + " loadout from " + getName() + "? Type \"Yes\" to confirm.", null);
			ply.sendMessage("The menu will automatically reopen in 10s if nothing is entered.");
			ply.setManualEntry(this);
			getContainer().startReopenTimer(10);
		}
		
		return null;
	}
	
	@Override
	public void checkValidEntry(String entry){
		if(entry.equalsIgnoreCase("yes")){
			String loadoutName = loadout.getName();
			mgm.deleteLoadout(loadoutName);
			getContainer().removeItem(getSlot());
			getContainer().cancelReopenTimer();
			getContainer().displayMenu(getContainer().getViewer());
			getContainer().getViewer().sendMessage(loadoutName + " has been deleted from " + mgm.getName(), null);
			return;
		}
		getContainer().getViewer().sendMessage(loadout.getName() + " was not deleted.", "error");
		getContainer().cancelReopenTimer();
		getContainer().displayMenu(getContainer().getViewer());
	}
	
	public void setAllowDelete(boolean bool){
		allowDelete = bool;
	}
}