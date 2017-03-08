import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	private Thread thread;
	private boolean running = false;
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 360;
	
	private long startTime;
	public long localTime;
	public int mx, my;
	
	public Input input;
	public static String[] debugMessage = {"", "", "", "", "", ""};
	
	// Game initialization
	// TODO: move code from constructor to main()
	// reason: classes require a Game object before the constructor is finished running
	// TODO: make Game class singleton
	public Game() {
		new Window(WIDTH, HEIGHT, "Sucrosu", this);
		startTime = System.currentTimeMillis();
		
		input = new Input(this);
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
		// add hit circles
		for (int i = 0; i < 25; i++) {
			HitCircle.add(60 + (i%6)*100, 65 + (i%4)*75, 2000 + i*1000);
		}
		
		HitCircle.setGameInstance(this);
	}
	
	private void tick() {
		localTime = System.currentTimeMillis() - startTime;
		debugMessage[0] = "Time: " + String.valueOf(localTime);
		debugMessage[1] = String.format("Mouse: (%d, %d)", mx, my);
		HitCircle.tickAll();
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
		
		HitCircle.renderAll(g);
		
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
	}
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int clamp(int target, int min, int max) {
		if (target < min)
			return min;
		if (target > max)
			return max;
		return target;
	}
	
	public static void main(String[] args) {
		new Game();
	}
}