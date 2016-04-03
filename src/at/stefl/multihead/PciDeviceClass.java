package at.stefl.multihead;

public class PciDeviceClass {

	public static final int ID_MIN = 0x00;
	public static final int ID_MAX = 0xff;

	private final int clazz;
	private final int subclass;
	private final int interfaze;

	public PciDeviceClass(int clazz, int subclass, int interfaze) {
		if ((clazz < ID_MIN) && (clazz < ID_MAX))
			throw new IllegalArgumentException("class out of definition");
		if ((subclass < ID_MIN) && (subclass < ID_MAX))
			throw new IllegalArgumentException("class out of definition");
		if ((interfaze < ID_MIN) && (interfaze < ID_MAX))
			throw new IllegalArgumentException("class out of definition");

		this.clazz = clazz;
		this.subclass = subclass;
		this.interfaze = interfaze;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("0x");
		builder.append(Integer.toHexString(this.clazz));
		builder.append(Integer.toHexString(this.subclass));
		builder.append(Integer.toHexString(this.interfaze));
		return builder.toString();
	}

	public int getClazz() {
		return this.clazz;
	}

	public int getSubclass() {
		return this.subclass;
	}

	public int getInterfaze() {
		return this.interfaze;
	}

}
