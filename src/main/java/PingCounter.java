import java.util.*;

/**
 * Created by astifel on 26.06.17.
 */
public class PingCounter {
	private static PingCounter ourInstance = new PingCounter();

	public Map<String, Integer> map = new HashMap<>();
	public Map<String, List<Object>> history = new HashMap<>();

	public static PingCounter getInstance() {
		return ourInstance;
	}

	public void addCount(String id) {
		int count = map.getOrDefault(id, 0);
		map.put(id, ++count);
	}

	public void intervalElapsed() {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			List<Object> historyList = history.getOrDefault(entry.getKey(), new ArrayList<>());
			historyList.add(entry.getValue());
			if (historyList.size() > 20) {
				historyList.remove(0);
			}
			history.put(entry.getKey(), historyList);
		}


		for(String s : map.keySet()){
		    map.put(s,0);
        }

	}

	private PingCounter() {
	}
}
