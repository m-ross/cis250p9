package lab09;

public class Element {
	private String key;
	private long num;

	public Element() { }

	public Element(String key, long num) {
		set(key, num);
	}

	public void set(String key, long num) {
		this.key = key;
		this.num = num;
	}

	public String getKey() {
		return key;
	}

	public long getNum() {
		return num;
	}

	public Element clone() {
		Element element;
		element = new Element(key, num);
		return element;
	}
}
