import org.junit.Test;
import static org.junit.Assert.*;

public class ARTest {
	private int radius = 0;
	
	int[] visible_duration = new int[] {
		1800,
		1680,
		1560,
		1440,
		1320,
		1200,
		1050,
		900,
		750,
		600,
		450
	};
	
	@Test
	public void testGetVisibleDuration() throws Exception {
		for (int AR = 0; AR <= 10; AR++) {
			assertEquals(visible_duration[AR], getVisibleDuration((double) AR));
		}
	}
	
	public int getVisibleDuration(double AR) {
		if (AR <= 5)
			return (int) (1800 - (120 * AR));
		else
			return (int) (1200 - (150 * (AR - 5)));
	}
	
	@Test
	public void testApproachRadius() throws Exception {
		double AR = 11;
		for (int eta = 2000; eta >= 0; eta -= 10) {
			if (eta < getVisibleDuration(AR))
				System.out.printf("%dms eta: \t %d approachRadius\n", eta, getApproachRadius(AR, eta));
		}
	}
	
	public int getApproachRadius(double AR, int eta) {
		return (int) (radius + (100 - radius) * ((double) eta / (double) getVisibleDuration(AR)));
	}
}
