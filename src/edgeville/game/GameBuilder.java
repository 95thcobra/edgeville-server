package edgeville.game;

import java.util.ArrayDeque;
import java.util.Queue;

import edgeville.Bootstrap;
import edgeville.game.character.player.content.RestoreStatTask;
import edgeville.game.character.player.minigame.MinigameHandler;
import edgeville.game.character.player.serialize.PlayerSerialization;
import edgeville.game.item.ItemNodeManager;
import edgeville.game.region.ObjectDef;
import edgeville.game.region.Region;
import edgeville.load.EquipmentInfo;
import edgeville.net.ConnectionHandler;
import edgeville.service.ServiceQueue;
import edgeville.utility.BackgroundLoader;
import edgeville.utility.json.EquipmentRequirementLoader;
import edgeville.utility.json.ItemDefinitionLoader;
import edgeville.utility.json.ItemNodeLoader;
import edgeville.utility.json.MessageOpcodeLoader;
import edgeville.utility.json.MessageSizeLoader;
import edgeville.utility.json.NpcDefinitionLoader;
import edgeville.utility.json.NpcDropCacheLoader;
import edgeville.utility.json.NpcDropTableLoader;
import edgeville.utility.json.NpcNodeLoader;
import edgeville.utility.json.ObjectNodeLoader;
import edgeville.utility.json.ObjectNodeRemoveLoader;
import edgeville.utility.json.ShopLoader;
import edgeville.utility.json.WeaponAnimationLoader;
import edgeville.utility.json.WeaponInterfaceLoader;
import edgeville.utility.json.WeaponPoisonLoader;

/**
 * Initializes game {@link Object}s and prepares them to be created by the
 * {@link Bootstrap}.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class GameBuilder {

    /**
     * The background loader that will load various utilities in the background
     * while the bootstrap is preparing the server.
     */
    private final BackgroundLoader backgroundLoader = new BackgroundLoader();

    /**
     * The service queue that will run the {@link GameService}.
     */
    private final ServiceQueue queue = new ServiceQueue();

    /**
     * Initializes this game builder effectively preparing the background
     * startup tasks and game processing.
     *
     * @throws Exception
     *             if any issues occur while starting the network.
     */
    public void initialize() throws Exception {
        backgroundLoader.start(createBackgroundTasks());
        queue.submit(World.getService());
        World.submit(new ItemNodeManager());
        World.submit(new RestoreStatTask());
        World.submit(new MinigameHandler());
        PlayerSerialization.getCache().init();
        if (!backgroundLoader.awaitCompletion())
            throw new IllegalStateException("Background load did not complete normally!");
        
        // New Other
        Region.load();
        ObjectDef.loadConfig();
        
    }

    /**
     * Returns a queue containing all of the background tasks that will be
     * executed by the background loader. Please note that the loader may use
     * multiple threads to load the utilities concurrently, so utilities that
     * depend on each other <b>must</b> be executed in the same task to ensure
     * thread safety.
     *
     * @return the queue of background tasks.
     */
    public Queue<Runnable> createBackgroundTasks() {
        Queue<Runnable> tasks = new ArrayDeque<>();
        tasks.add(new NpcDefinitionLoader());
        tasks.add(new ItemDefinitionLoader());
        tasks.add(new WeaponPoisonLoader());
        tasks.add(new MessageOpcodeLoader());
        tasks.add(new MessageSizeLoader());
        tasks.add(ConnectionHandler::parseIPBans);
        tasks.add(new NpcNodeLoader());
        tasks.add(new ShopLoader());
        tasks.add(new ItemNodeLoader());
        tasks.add(new ObjectNodeLoader());
        tasks.add(new NpcDropTableLoader());
        tasks.add(new WeaponAnimationLoader());
        tasks.add(new WeaponInterfaceLoader());
        tasks.add(new EquipmentRequirementLoader());
        tasks.add(new ObjectNodeRemoveLoader());
        tasks.add(new NpcDropCacheLoader());
        //tasks.add(World.getPlugins()::init);
        
        EquipmentInfo.loadAll();
        
        return tasks;
    }
}
