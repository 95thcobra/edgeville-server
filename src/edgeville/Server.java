package edgeville;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;

import edgeville.game.region.FileSystem;
import edgeville.net.NetworkConstants;
import edgeville.utility.LoggerUtils;


/**
 * The main class that will create and bind the {@link Bootstrap},
 * effectively putting the server online.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Server {

    /**
     * The name of this server.
     */
    public static final String NAME = "Asteria 3.0";

    /**
     * The flag that determines if debugging messages should be printed or not.
     */
    public static final boolean DEBUG = true;

    /**
     * The logger that will print important information.
     */
    private static Logger logger = LoggerUtils.getLogger(Server.class);

	public static FileSystem fileSystem;
    
    /**
     * The default constructor, will throw an
     * {@link UnsupportedOperationException} if instantiated.
     *
     * @throws UnsupportedOperationException
     *             if this class is instantiated.
     */
    private Server() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * The main method that will put the server online.
     */
    public static void main(String[] args) {
        try {
            Preconditions.checkState(args.length == 0, "No runtime arguments needed!");
           
            logger.info("Creating file system...");
            fileSystem = FileSystem.create("./data/cache");
            
            logger.info("Initializing the Bootstrap...");
            Bootstrap bootstrap = new Bootstrap(NetworkConstants.PORT);
            bootstrap.bind();
            logger.info("The Bootstrap has been bound, " + NAME + " is now online!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while binding the Bootstrap!", e);
            
            // No point in continuing server startup when the
            // bootstrap either failed to bind or was bound
            // incorrectly.
            System.exit(1);
        }
    }
}