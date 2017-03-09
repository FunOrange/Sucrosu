import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	private static Game game;
	private Thread thread;
	private boolean running = false;
	public static final int WIDTH = (int) (800); //  640
	public static final int HEIGHT = (int) (600); // 360
	public static final int INPUT_OFFSET = 50;
	
	private final double TIMESCALE =  0.7;
	
	private long startTime;
	public int localTime;
	
	private CircleHandler circleHandler;
	private ParticleHandler particleHandler;
	
	public String[] debugMessage = {"", "", "", "", "", ""};
	
	private Game() {
		Window window = new Window(this);
		Input input = new Input(this, window);
		circleHandler = new CircleHandler(this);
		particleHandler = new ParticleHandler();
		circleHandler.setDifficulty(9.0, 4.0, 0.0);
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
		
		debugMessage[0] = "Time: " + String.valueOf(localTime);
		debugMessage[3] = String.format("Circles on screen: %d", circleHandler.objectCount());
		circleHandler.tick();
	}
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		// draw background
		g.setColor(new Color(40, 42, 54));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		circleHandler.renderAll(g);
		particleHandler.renderAll(g);
		
		g.setColor(Color.WHITE);
		for (int i = 0; i < debugMessage.length-1; i++) {
			g.drawString(debugMessage[i], 12, 20+i*20);
		}
		g.drawString(debugMessage[5], WIDTH - 75, HEIGHT - 40);
		
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
				debugMessage[5] = "FPS: " + frames;
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
		
//		Random rand = new Random();
//		for (int i = 24; i >= 0; i--) {
////			HitCircle.add(60 + (i%6)*100, 65 + (i%4)*75, 2000 + i*1000);
//			HitCircle.add(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), 2000 + i*600);
//		}
		
	}
	
	public static int clamp(int target, int min, int max) {
		if (target < min)
			return min;
		if (target > max)
			return max;
		return target;
	}
	
	public static Game getInstance() {
		return game;
	}
	
	public CircleHandler getCircleHandler() {
		return circleHandler;
	}
	
	public ParticleHandler getParticleHandler() {
		return particleHandler;
	}
}