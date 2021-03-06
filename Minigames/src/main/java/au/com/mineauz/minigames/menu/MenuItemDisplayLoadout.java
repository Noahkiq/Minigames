package au.com.mineauz.minigames.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.PlayerLoadout;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.TeamColor;
import au.com.mineauz.minigames.minigame.modules.LoadoutModule;

public class MenuItemDisplayLoadout extends MenuItem{
	
	private PlayerLoadout loadout = null;
	private Minigame mgm = null;
	private boolean allowDelete = true;
	private Menu altMenu = null;
	
	public MenuItemDisplayLoadout(String name, Material displayItem, PlayerLoadout loadout, Minigame minigame) {
		super(name, displayItem);
		this.loadout = loadout;
		mgm = minigame;
		if(!loadout.isDeleteable())
			allowDelete = false;
	}
	
	public MenuItemDisplayLoadout(String name, Material displayItem, PlayerLoadout loadout) {
		super(name, displayItem);
		this.loadout = loadout;
		if(!loadout.isDeleteable())
			allowDelete = false;
	}

	public MenuItemDisplayLoadout(String name, List<String> description, Material displayItem, PlayerLoadout loadout, Minigame minigame) {
		super(name, description, displayItem);
		this.loadout = loadout;
		mgm = minigame;
		if(!loadout.isDeleteable())
			allowDelete = false;
	}

	public MenuItemDisplayLoadout(String name, List<String> description, Material displayItem, PlayerLoadout loadout) {
		super(name, description, displayItem);
		this.loadout = loadout;
		if(!loadout.isDeleteable())
			allowDelete = false;
	}
	
	public void setAltMenu(Menu altMenu){
		this.altMenu = altMenu;
	}
	
	@Override
	public ItemStack onClick(){
		Menu loadoutMenu = new Menu(5, loadout.getName(false), getContainer().getViewer());
		Menu loadoutSettings = new Menu(6, loadout.getName(false), getContainer().getViewer());
		loadoutSettings.setPreviousPage(loadoutMenu);
		
		List<MenuItem> mItems = new ArrayList<MenuItem>();
		if(!loadout.getName(false).equals("default"))
			mItems.add(new MenuItemBoolean("Use Permissions", MinigameUtils.stringToList("Permission:;minigame.loadout." + loadout.getName(false).toLowerCase()), 
					Material.GOLD_INGOT, loadout.getUsePermissionsCallback()));
		MenuItemString disName = new MenuItemString("Display Name", Material.PAPER, loadout.getDisplayNameCallback());
		disName.setAllowNull(true);
		mItems.add(disName);
		mItems.add(new MenuItemBoolean("Allow Fall Damage", Material.LEATHER_BOOTS, loadout.getFallDamageCallback()));
		mItems.add(new MenuItemBoolean("Allow Hunger", Material.APPLE, loadout.getHungerCallback()));
		mItems.add(new MenuItemInteger("XP Level", MinigameUtils.stringToList("Use -1 to not;use loadout levels"), Material.EXP_BOTTLE, loadout.getLevelCallback(), -1, null));
		mItems.add(new MenuItemBoolean("Lock Inventory", Material.DIAMOND_SWORD, loadout.getInventoryLockedCallback()));
		mItems.add(new MenuItemBoolean("Lock Armour", Material.DIAMOND_CHESTPLATE, loadout.getArmourLockedCallback()));
		mItems.add(new MenuItemBoolean("Allow Offhand", Material.SHIELD, loadout.getAllowOffHandCallback()));
		mItems.add(new MenuItemBoolean("Display in Loadout Menu", Material.THIN_GLASS, loadout.getDisplayInMenuCallback()));
		List<String> teams = new ArrayList<String>();
		teams.add("None");
		for(TeamColor col : TeamColor.values())
			teams.add(MinigameUtils.capitalize(col.toString()));
		mItems.add(new MenuItemList("Lock to Team", Material.LEATHER_CHESTPLATE, loadout.getTeamColorCallback(), teams));
		loadoutSettings.addItems(mItems);
		if(mgm == null){
			MenuItemDisplayLoadout dl = new MenuItemDisplayLoadout("Back", Material.REDSTONE_TORCH_ON, loadout);
			dl.setAltMenu(getContainer());
			loadoutSettings.addItem(dl, getContainer().getSize() - 9);
		}
		else{
			MenuItemDisplayLoadout dl = new MenuItemDisplayLoadout("Back", Material.REDSTONE_TORCH_ON, loadout, mgm);
			dl.setAltMenu(getContainer());
			loadoutSettings.addItem(dl, getContainer().getSize() - 9);
		}
		
		LoadoutModule.addAddonMenuItems(loadoutSettings, loadout);
		
		Menu potionMenu = new Menu(5, getContainer().getName(), getContainer().getViewer());
		
		potionMenu.setPreviousPage(loadoutMenu);
		potionMenu.addItem(new MenuItemPotionAdd("Add Potion", Material.ITEM_FRAME, loadout), 44);
		if(mgm == null){
			MenuItemDisplayLoadout dl = new MenuItemDisplayLoadout("Back", Material.REDSTONE_TORCH_ON, loadout);
			dl.setAltMenu(getContainer());
			potionMenu.addItem(dl, 45 - 9);
		}
		else{
			MenuItemDisplayLoadout dl = new MenuItemDisplayLoadout("Back", Material.REDSTONE_TORCH_ON, loadout, mgm);
			dl.setAltMenu(getContainer());
			potionMenu.addItem(dl, 45 - 9);
		}
		
		List<String> des = new ArrayList<String>();
		des.add("Shift + Right Click to Delete");
		List<MenuItem> potions = new ArrayList<MenuItem>();
		
		for(PotionEffect eff : loadout.getAllPotionEffects()){
			potions.add(new MenuItemPotion(MinigameUtils.capitalize(eff.getType().getName().replace("_", " ")), des, Material.POTION, eff, loadout));
		}
		potionMenu.addItems(potions);
		
		loadoutMenu.setAllowModify(true);
		if(altMenu == null)
			loadoutMenu.setPreviousPage(getContainer());
		else
			loadoutMenu.setPreviousPage(altMenu);
		
		loadoutMenu.addItem(new MenuItemSaveLoadout("Loadout Settings", Material.CHEST, loadout, loadoutSettings), 42);
		loadoutMenu.addItem(new MenuItemSaveLoadout("Edit Potion Effects", Material.POTION, loadout, potionMenu), 43);
		loadoutMenu.addItem(new MenuItemSaveLoadout("Save Loadout", Material.REDSTONE_TORCH_ON, loadout), 44);
		
		for(int i = 40; i < 42; i++){
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
	public ItemStack onShiftRightClick(){
		if(allowDelete){
			MinigamePlayer ply = getContainer().getViewer();
			ply.setNoClose(true);
			ply.getPlayer().closeInventory();
			ply.sendMessage("Delete the " + loadout.getName(false) + " loadout from " + getName() + "? Type \"Yes\" to confirm.", null);
			ply.sendMessage("The menu will automatically reopen in 10s if nothing is entered.");
			ply.setManualEntry(this);
			getContainer().startReopenTimer(10);
			return null;
		}
		
		return getItem();
	}
	
	@Override
	public void checkValidEntry(String entry){
		if(entry.equalsIgnoreCase("yes")){
			String loadoutName = loadout.getName(false);
			if(mgm != null)
				LoadoutModule.getMinigameModule(mgm).deleteLoadout(loadoutName);
			else
				Minigames.plugin.mdata.deleteLoadout(loadoutName);
			getContainer().removeItem(getSlot());
			getContainer().cancelReopenTimer();
			getContainer().displayMenu(getContainer().getViewer());
			getContainer().getViewer().sendMessage(loadoutName + " has been deleted.", null);
			return;
		}
		getContainer().getViewer().sendMessage(loadout.getName(false) + " was not deleted.", "error");
		getContainer().cancelReopenTimer();
		getContainer().displayMenu(getContainer().getViewer());
	}
	
	public void setAllowDelete(boolean bool){
		allowDelete = bool;
	}
}
