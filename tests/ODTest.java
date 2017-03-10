import org.junit.Test;
import static org.junit.Assert.*;

public class ODTest {
	int[] window_300 = new int[11];
	int[] window_100 = new int[11];
	int[] window_50 = new int[11];
	
	int[] test_window_300 = new int[11];
	int[] test_window_100 = new int[11];
	int[] test_window_50 = new int[11];
	
	@Test
	public void testTimingWindows() throws Exception {
		window_300[0] = 78;
		window_300[1] = 72;
		window_300[2] = 66;
		window_300[3] = 60;
		window_300[4] = 54;
		window_300[5] = 48;
		window_300[6] = 42;
		window_300[7] = 36;
		window_300[8] = 30;
		window_300[9] = 24;
		window_300[10] = 18;
		
		window_100[0] = 138;
		window_100[1] = 130;
		window_100[2] = 122;
		window_100[3] = 114;
		window_100[4] = 106;
		window_100[5] = 98;
		window_100[6] = 90;
		window_100[7] = 82;
		window_100[8] = 74;
		window_100[9] = 66;
		window_100[10] = 58;
		
		window_50[0] = 198;
		window_50[1] = 188;
		window_50[2] = 178;
		window_50[3] = 168;
		window_50[4] = 158;
		window_50[5] = 148;
		window_50[6] = 138;
		window_50[7] = 128;
		window_50[8] = 118;
		window_50[9] = 108;
		window_50[10] = 98;
		
		for (int OD = 0; OD <= 10; OD++) {
			test_window_300[OD] = getTimingWindow_300((double) OD) * 2;
			test_window_100[OD] = getTimingWindow_100((double) OD) * 2;
			test_window_50[OD] = getTimingWindow_50((double) OD) * 2;
		}
		
		assertArrayEquals(window_300, test_window_300);
		assertArrayEquals(window_100, test_window_100);
		assertArrayEquals(window_50, test_window_50);
	}
	
	int getTimingWindow_300(double OD) {
		return (int) (78 - (6 * OD)) / 2;
	}
	
	int getTimingWindow_100(double OD) {
		return (int) (138 - (8 * OD)) / 2;
	}
	
	int getTimingWindow_50(double OD) {
		return (int) (198 - (10 * OD)) / 2;
	}
}