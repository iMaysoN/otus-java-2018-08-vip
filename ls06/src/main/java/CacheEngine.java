import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CacheEngine<K, V> {
    private final int maxStoredElements;
    private final long lifeTimeOfElementMs;

    private final Map<K, SoftReference<V>> elements = new HashMap<>();
    private final Timer timer = new Timer();

    private int storedTotal = 0;
    private int returnedFromCache = 0;
    private int notFoundInCache = 0;

    public CacheEngine(int maxStoredElements, long lifeTimeOfElementMs) {
        this.maxStoredElements = maxStoredElements;
        this.lifeTimeOfElementMs = lifeTimeOfElementMs > 0 ? lifeTimeOfElementMs : 0;
    }

    public boolean store(K key, V element) {
        try {
            if (elements.size() >= maxStoredElements) {
                logCache("Cache contain maximum elements.");
                Map.Entry<K, SoftReference<V>> firstElement = elements.entrySet().iterator().next();
                elements.remove(firstElement.getKey());
                logCache("Removed first element: %s", firstElement.getKey().toString());
            }
            elements.put(key, new SoftReference<>(element));
            long elementAddedTime = System.currentTimeMillis();
            storedTotal++;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SoftReference<V> scheduledCheckedElement = elements.get(key);
                    long currentTime = System.currentTimeMillis();
                    if (scheduledCheckedElement != null || (currentTime > elementAddedTime + lifeTimeOfElementMs)) {
                        elements.remove(key);
                        logCache("The element '%s' has lived for too long and too good [from %s, till %s], EXTERMINATE!",
                                key.toString(), String.valueOf(elementAddedTime), String.valueOf(currentTime));
                        this.cancel();
                    }
                }
            }, lifeTimeOfElementMs + 42);
            logCache("Added new element: %s", key.toString());
        } catch (Exception e) {
            logCache("Some problems: ", e.getMessage());
            return false;
        }
        return true;
    }

    public V get(K key) {
        SoftReference<V> elementReference = elements.get(key);
        if (elementReference != null) {
            returnedFromCache++;
            return elementReference.get();
        } else {
            notFoundInCache++;
            return null;
        }
    }

    public int size() {
        return elements.size();
    }

    public void close() {
        System.out.printf("CacheEngine closed. Statistic: added - %s, requested from cache - %s, requested but not found - %s.%n",
                storedTotal, returnedFromCache, notFoundInCache);
    }

    private void logCache(String message, String... params) {
        System.out.printf("CACHE LOG: %s%n", String.format(message, params));
    }
}
