import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	private static Game game;
	private Thread thread;
	private boolean running = false;
	public static final int WIDTH = (int) (800); //  640
	public static final int HEIGHT = (int) (600); // 360
	private Graphics2D g;
	
	private final double TIMESCALE = 1;
	
	private long startTime;
	
	private int localTime;
	
	private CircleHandler circleHandler;
	
	private ParticleHandler particleHandler;
	private Game() {
		Window window = new Window(this);
		Input input = new Input(this, window);
		circleHandler = new CircleHandler(this);
		particleHandler = new ParticleHandler();
		circleHandler.setDifficulty(8.0, 4.0, 1.0);
		addMouseListener(input);
		addMouseMotionListener(input);
		addKeyListener(input);
		
		
		start();
	}
	
	private void tick() {
		localTime = (int) ((int) (System.currentTimeMillis() - startTime) * TIMESCALE);
		
		circleHandler.tick();
		circleHandler.tickAll();
		particleHandler.tickAll();
		
		circleHandler.tick();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw background
		g.setColor(new Color(40, 42, 54));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		circleHandler.renderAll();
		particleHandler.renderAll();
		
		g.setColor(Color.WHITE);
		
		g.dispose();
		bs.show();
	}
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 120.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running)
				render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
			}
		}
		stop();
	}
	
	private synchronized void start() {
		System.out.println("Starting application...");
		thread = new Thread(this);
		thread.start();
		running = true;
		startTime = System.currentTimeMillis();
	}
	private synchronized void stop() {
		System.out.println("Ending application...");
		try {
			System.out.println("joining thread");
			thread.join();
			System.out.println("thread joined");
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		game = new Game();
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}
	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}
	public static int lerp(int from, int to, float progression) {
		progression = clamp(progression, 0, 1);
		return (int) (from + (to-from)*progression);
	}
	public static float lerp(float from, float to, float progression) {
		progression = clamp(progression, 0, 1);
		return from + (to-from)*progression;
	}
	public static double lerp(double from, double to, float progression) {
		progression = clamp(progression, 0, 1);
		return from + (to-from)*progression;
	}
	public static Game getInstance() {
		return game;
	}
	public CircleHandler getCircleHandler() {
		return circleHandler;
	}
	
	public Graphics2D getGraphics() {
		return g;
	}
	
	public ParticleHandler getParticleHandler() {
		return particleHandler;
	}
	
	public int getLocalTime() {
		return localTime;
	}
	
	
}