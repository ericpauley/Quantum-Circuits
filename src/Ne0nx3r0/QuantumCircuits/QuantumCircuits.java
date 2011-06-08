package Ne0nx3r0.QuantumCircuits;

import org.bukkit.util.config.Configuration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

public class QuantumCircuits extends JavaPlugin{
    private final QuantumCircuitsBlockListener blockListener = new QuantumCircuitsBlockListener(this);
    private final QuantumCircuitsPlayerListener playerListener = new QuantumCircuitsPlayerListener(this);
    public int MAX_LAG_TIME = 300;
    public boolean USE_PERMISSIONS = false;
    public PermissionHandler permissionHandler;
    
    public void onDisable(){
        System.out.println("[Quantum] Quantum Circuits Disabled");
    }

    public void onEnable(){
        Configuration config = getConfiguration();

        int iMaxLagTime = config.getInt("maxlagtime",-1);
        boolean usePermissions = config.getBoolean("usepermissions",false);

        //set default values if necessary
        if(iMaxLagTime == -1){
            System.out.println("[Quantum] Creating config file...");
            config.setProperty("maxlagtime",300);
            iMaxLagTime = 300;
            config.save();
        }
        
        if(!usePermissions){
        	config.setProperty("usepermissions", false);
        	config.save();
        }

        MAX_LAG_TIME = iMaxLagTime;
        USE_PERMISSIONS = usePermissions;
        
        if(USE_PERMISSIONS){
        	setupPermissions();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, playerListener, Priority.Normal, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        if(!USE_PERMISSIONS){
        	System.out.println("[Quantum] "+pdfFile.getName() + " version " + pdfFile.getVersion() + " ENABLED" );
        }else{
        	System.out.println("[Quantum] "+pdfFile.getName() + " version " + pdfFile.getVersion() + " ENABLED WITH PERMISSIONS" );
        }
    }
    
    private void setupPermissions() {
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

        if (permissionHandler == null) {
            if (permissionsPlugin != null) {
                permissionHandler = ((Permissions) permissionsPlugin).getHandler();
            } else {
                System.out.println("Permission system not detected, defaulting to OP");
            }
        }
    }
}