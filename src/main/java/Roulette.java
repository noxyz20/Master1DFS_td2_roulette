import java.util.Random;

public class Roulette {
    private boolean spinning = false;
    private final WheelObserver wheelObserver;
    private long spinForMs;
    private long currentMs = 0;
    private final Random random = new Random();

    public static void spin(final WheelObserver wheelObserver, final int spinDuration) {
        Roulette rouletteWheel = new Roulette(wheelObserver);
        rouletteWheel.spin(spinDuration);
    }

    Roulette(final WheelObserver wheelObserver) {
        this.wheelObserver = wheelObserver;
    }

    void spin(final long spinForMs) {
        this.spinning = true;
        this.spinForMs = spinForMs;
    }

    void tick(final long timeMs) {
        currentMs = timeMs;
        if (spinning && currentMs >= spinForMs)
        {
            spinning = false;
            final int location = random.nextInt(37);
            this.wheelObserver.stopped(location);
        }
    }
}