package magnata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
	String magnata = "";
	double maiorValor = 0.0;
	
	public void getMagnata() {
		JSONObject jsonObject;
        JSONParser parser = new JSONParser();
        
        try {
        	 jsonObject = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "\\plugins\\Magnata\\config.json"));
        	 magnata = (String) jsonObject.get("magnata");
        	 maiorValor = (double) jsonObject.get("dinheiro");
        	 getLogger().info("[Magnata v1.0] Arquivo de configurações carregado.");
        } catch (FileNotFoundException e) {
        	getLogger().info("[Magnata v1.0] Não foi possível encontrar o arquivo de configurações. Criando...");
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (ParseException e) {
        	e.printStackTrace();
        }
	}
	
	JSONObject jsonObject;
	JSONParser parser = new JSONParser();
	
	
	
	private static Economy econ = null;
	
	@Override
	
	public  void onDisable() {
	    getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
	}


	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		JSONObject jsonObject = new JSONObject();
		FileWriter writeFile = null;
		
		getMagnata();
		
		jsonObject.put("magnata", magnata);
		jsonObject.put("dinheiro", maiorValor);
		
		try{
			writeFile = new FileWriter("plugins\\Magnata\\config.json");
			writeFile.write(jsonObject.toJSONString());
			writeFile.close();
		}
		catch(IOException e){
		}
		jsonObject = null;
		File folder = new File("plugins\\Magnata");
		folder.mkdirs();
	    if (!setupEconomy() ) {
	        getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
	        getServer().getPluginManager().disablePlugin(this);
	        return;
	    }

	}

	@SuppressWarnings("unchecked")
	public void setMagnata(String magnata, double maiorValor) {
		JSONObject jsonObject = new JSONObject();
		FileWriter writeFile = null;
		
		getMagnata();
		
		jsonObject.put("magnata", magnata);
		jsonObject.put("dinheiro", maiorValor);
		
		try{
			writeFile = new FileWriter("plugins\\Magnata\\config.json");
			writeFile.write(jsonObject.toJSONString());
			writeFile.close();
		}
		catch(IOException e){
		}
		jsonObject = null;
		
		getServer().dispatchCommand(Bukkit.getConsoleSender(), "manuaddv "+magnata+" prefix &f&l[&6&lMAGNATA&f&l]") ;
	}

	private boolean setupEconomy() {
	    if (getServer().getPluginManager().getPlugin("Vault") == null) {
	        return false;
	    }
	    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	    if (rsp == null) {
	        return false;
	    }
	    econ = rsp.getProvider();
	    return econ != null;
	}


	public static Economy getEconomy() {
	    return econ;
	}

	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("magnata")) {
			
			if(sender instanceof Player) {
				
				Economy economy = getEconomy(); 
				for(Player jogador:  Bukkit.getServer().getOnlinePlayers()){
					
					double dinheiro = economy.getBalance(jogador);
					
					if(dinheiro > maiorValor) {
						maiorValor = dinheiro;
						magnata = jogador.getName();
//					if(novoMagnata != magnata) {
//						Bukkit.broadcastMessage(ChatColor.GREEN + "Parabéns " + novoMagnata + " você se tornou o Magnata!");						
//					} if(novoMagnata != jogador.getName()) {
//						jogador.sendMessage(ChatColor.RED + "Você ainda não está com dinheiro suficiente!");
//					} if (novoMagnata == magnata && jogador.getName() == magnata) {
//						jogador.sendMessage(ChatColor.RED + "Você já é o Magnata, continue assim!");
//					}
						
					}
					
				}
				
				setMagnata(magnata, maiorValor);
				Bukkit.broadcastMessage(ChatColor.GREEN + "Parabéns " + magnata + " você se tornou o Magnata!");
				
			}
		}
		return true;
	}
}
