import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class RouletteTest {
    @Test
    public void shouldNotifyStopped20SecondsAfterSpin() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        Roulette wheel = new Roulette(wheelObserver);

        wheel.spin(20000);
        wheel.tick(20000);

        verify(wheelObserver).stopped(anyInt());
    }


    @Test
    public void shouldProvideRandomBallLocationWhenStopped() {
        final boolean seenAll[] = new boolean[1];
        seenAll[0] = false;

        WheelObserver wheelObserver = new WheelObserver() {
            Set<Integer> seen = new HashSet<Integer>();
            public void stopped(final int location) {
                if (location < 0 || location > 36)
                    throw new IllegalArgumentException();
                seen.add(location);
                if (seen.size() == 37) seenAll[0] = true;
            }
        };
        Roulette wheel = new Roulette(wheelObserver);

        for (int x = 0; x < 1000; x++)
        {
            wheel.spin(0);
            wheel.tick(20000);
        }

        assertTrue(seenAll[0]);
    }

    @Test
    public void shouldSpecifyBallLocationWhenStopped() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        Roulette wheel = new Roulette(wheelObserver);


        long spinFor20s = 20000;
        wheel.spin(spinFor20s);
        wheel.tick(20000);

        verify(wheelObserver, times(1)).stopped(anyInt());
    }

    @Test
    public void shouldSpecifyBallLocationOnceWhenStopped() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        Roulette wheel = new Roulette(wheelObserver);

        long spinFor20s = 20000;
        wheel.spin(spinFor20s);
        wheel.tick(20000);
        wheel.tick(20001);

        verify(wheelObserver, times(1)).stopped(anyInt());
    }


    @Test
    public void shouldNotNotifyStoppedBeforeSpinEnd() {
        WheelObserver wheelObserver = mock(WheelObserver.class);
        Roulette wheel = new Roulette(wheelObserver);

        long spinFor20s = 20000;
        wheel.spin(spinFor20s);

        long timeEndMs = 10000;
        wheel.tick(timeEndMs);

        verify(wheelObserver, never() ).stopped(anyInt());
    }
}