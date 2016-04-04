package at.stefl.multihead;

public class DeviceClass {

	public static final int ID_MIN = 0x00;
	public static final int ID_MAX = 0xff;

	private final byte clazz;
	private final byte subclass;
	private final byte interfaze;

	public DeviceClass(int number) {
		this((number >> 16) & 0xff, (number >> 8) & 0xff, (number >> 0) & 0xff);
	}

	public DeviceClass(int clazz, int subclass, int interfaze) {
		if ((clazz < ID_MIN) && (clazz < ID_MAX))
			throw new IllegalArgumentException("class out of definition");
		if ((subclass < ID_MIN) && (subclass < ID_MAX))
			throw new IllegalArgumentException("class out of definition");
		if ((interfaze < ID_MIN) && (interfaze < ID_MAX))
			throw new IllegalArgumentException("class out of definition");

		this.clazz = (byte) clazz;
		this.subclass = (byte) subclass;
		this.interfaze = (byte) interfaze;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("0x");
		builder.append(String.format("%02X", this.clazz));
		builder.append(String.format("%02X", this.subclass));
		builder.append(String.format("%02X", this.interfaze));
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.clazz;
		result = prime * result + this.interfaze;
		result = prime * result + this.subclass;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceClass other = (DeviceClass) obj;
		if (this.clazz != other.clazz)
			return false;
		if (this.interfaze != other.interfaze)
			return false;
		if (this.subclass != other.subclass)
			return false;
		return true;
	}

	public int getClazz() {
		return this.clazz & 0xff;
	}

	public int getSubclass() {
		return this.subclass & 0xff;
	}

	public int getInterfaze() {
		return this.interfaze & 0xff;
	}

	public int getNumber() {
		return ((this.clazz & 0xff) << 16) | ((this.subclass & 0xff) << 8) | ((this.interfaze & 0xff) << 0);
	}

}
