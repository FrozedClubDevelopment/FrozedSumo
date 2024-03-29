package club.frozed.frozedsumo.utils.assemble;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Assemble {

	private JavaPlugin plugin;
	private AssembleAdapter adapter;
	private Map<UUID, AssembleBoard> boards;
	private AssembleThread thread;
	private AssembleListener listeners;
	private long ticks = 1;
	private boolean hook = false;
	private AssembleStyle assembleStyle = AssembleStyle.MODERN;

	public Assemble(JavaPlugin plugin, AssembleAdapter adapter) {
		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	private void setup() {
		listeners = new AssembleListener(this);
		//Register Events
		this.plugin.getServer().getPluginManager().registerEvents(listeners, this.plugin);

		//Ensure that the thread has stopped running
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		//Start Thread
		this.thread = new AssembleThread(this);
	}

	public void cleanup() {
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		if (listeners != null) {
			HandlerList.unregisterAll(listeners);
			listeners = null;
		}
	}

}
