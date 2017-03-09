import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	private static Game game;
	private Thread thread;
	private boolean running = false;
	public static final int WIDTH = (int) (800); //  640
	public static final int HEIGHT = (int) (600); // 360
	
	private long startTime;
	public int localTime;
	public int mx, my;
	
	private CircleHandler handler;
	
	public String[] debugMessage = {"", "", "", "", "", ""};
	
	private Game() {
		new Window(WIDTH, HEIGHT, "Sucrosu", this);
		Input input = new Input(this);
		handler = new CircleHandler(this);
		handler.setDifficulty(9.0, 10.0, 4.0);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		start();
	}
	
	private void tick() {
		localTime = (int) (System.currentTimeMillis() - startTime);
		
		handler.tick();
		handler.tickAll();
		
		debugMessage[0] = "Time: " + String.valueOf(localTime);
		debugMessage[1] = String.format("Mouse: (%d, %d)", mx, my);
		debugMessage[3] = String.format("Circles on screen: %d", handler.objectCount());
		handler.tick();
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
		
		handler.renderAll(g);
		
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
		double amountOfTicks = 60.0;
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
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		startTime = System.currentTimeMillis();
	}
	public synchronized void stop() {
		try {
			thread.join();
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
		return handler;
	}
}