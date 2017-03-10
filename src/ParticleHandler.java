import java.util.Iterator;
import java.util.LinkedList;

public class ParticleHandler {
	private LinkedList<Particle> particles = new LinkedList<>();
	
	public void add(Particle p) {
		particles.add(p);
	}
	
	public void tickAll() {
		// FIXME: this shit keeps breaking
		Iterator<Particle> iter = particles.iterator();
		while (iter.hasNext()) {
			Particle p = iter.next();
			if (p.deleteme)
				iter.remove();
			else
				p.tick();
		}
	}
	
	public void renderAll() {
		// draw in reverse order
		for (int i = particles.size()-1; i >= 0; i--) {
			particles.get(i).render();
		}
	}
}
