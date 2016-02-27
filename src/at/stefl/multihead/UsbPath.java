package at.stefl.multihead;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsbPath {

	public static final int NONE = -1;

	public static final int BUS_MIN = 1;
	public static final int PORT_MIN = 0;
	public static final int CONFIG_MIN = 1;
	public static final int INTERFACE_MIN = 0;

	public static final UsbPath ROOT = new UsbPath();

	private static final int INDEX_BUS = 0;
	private static final Pattern PATTERN = Pattern
			.compile("(\\d+)\\-((?:(?:\\d+)\\.)+(?:\\d+))\\:(\\d+)\\.(\\d+)");

	public static UsbPath parse(String path) {
		Matcher matcher = PATTERN.matcher(path);
		if (!matcher.matches())
			throw new IllegalArgumentException("not matching");
		if (matcher.group(1) == null)
			return ROOT;
		int bus = Integer.parseInt(matcher.group(1));
		if (matcher.group(2) == null)
			return new UsbPath(bus);
		String[] ports = matcher.group(2).split("\\.");
		int[] nums = new int[1 + ports.length];
		nums[0] = bus;
		for (int i = 0; i < ports.length; i++) {
			nums[1 + i] = Integer.parseInt(ports[i]);
		}
		if (matcher.group(3) == null)
			return new UsbPath(nums);
		int config = Integer.parseInt(matcher.group(3));
		int interfaze = Integer.parseInt(matcher.group(4));
		return new UsbPath(nums, config, interfaze);
	}

	private final int[] path;
	private final int config;
	private final int interfaze;

	public UsbPath() {
		this(new int[0], 0, 0, NONE, NONE);
	}

	public UsbPath(int... path) {
		this(path, 0, path.length, NONE, NONE);
	}

	public UsbPath(int[] path, int config, int interfaze) {
		this(path, 0, path.length, config, interfaze);
	}

	public UsbPath(int[] path, int offset, int length, int config, int interfaze) {
		if (offset < 0)
			throw new IllegalArgumentException("offset < 0");
		if (length < 0)
			throw new IllegalArgumentException("length < 0");
		if (offset + length > path.length)
			throw new IllegalArgumentException("array out of range");

		if ((config != NONE) && (config < CONFIG_MIN))
			throw new IllegalArgumentException("illegal config");
		if ((interfaze != NONE) && (interfaze < INTERFACE_MIN))
			throw new IllegalArgumentException("interface config");
		if ((config == NONE) && (interfaze != NONE))
			throw new IllegalArgumentException("config not set");
		if ((config != NONE) && (interfaze == NONE))
			throw new IllegalArgumentException("interface not set");
		if ((length < 2) & (config != NONE))
			throw new IllegalArgumentException("cannot have a config");

		this.path = new int[length];
		if (length > 0) {
			if (path[INDEX_BUS] < BUS_MIN)
				throw new IllegalArgumentException("port out of definition");
			this.path[INDEX_BUS] = path[offset];
			for (int i = 1; i < length; i++) {
				if (path[i] < PORT_MIN)
					throw new IllegalArgumentException("port out of definition");
				this.path[i] = path[offset + i];
			}
		}

		this.config = config;
		this.interfaze = interfaze;
	}

	@Override
	public String toString() {
		if (this.path.length == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(this.path[0]);
		if (this.path.length > 1) {
			builder.append("-");
			for (int i = 1; i < this.path.length; i++) {
				builder.append(this.path[i]);
				if (i < this.path.length - 1)
					builder.append(".");
			}
			if (this.config != NONE) {
				builder.append(":");
				builder.append(this.config);
				builder.append(".");
				builder.append(this.interfaze);
			}
		}
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.config;
		result = prime * result + this.interfaze;
		result = prime * result + Arrays.hashCode(this.path);
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
		UsbPath other = (UsbPath) obj;
		if (this.config != other.config)
			return false;
		if (this.interfaze != other.interfaze)
			return false;
		if (!Arrays.equals(path, other.path))
			return false;
		return true;
	}

	public int getLength() {
		int result = this.path.length;
		if (this.config != NONE)
			result++;
		return result;
	}

	public int getBus() {
		if (this.path.length <= INDEX_BUS)
			return NONE;
		return path[INDEX_BUS];
	}

	public int getConfig() {
		return this.config;
	}

	public int getInterface() {
		return this.interfaze;
	}

	public boolean isLeave() {
		return this.config != NONE;
	}

	public UsbPath parent() {
		if (this.path.length == 0)
			return null;
		if (this.config != NONE)
			return new UsbPath(this.path);
		return new UsbPath(this.path, 0, this.path.length - 1);
	}

	public boolean parentOf(UsbPath other) {
		if (this.config != NONE)
			return false;
		if (this.getLength() >= other.getLength())
			return false;
		for (int i = 0; i < this.path.length; i++) {
			if (this.path[i] != other.path[i])
				return false;
		}
		return true;
	}

	public boolean siblingOf(UsbPath other) {
		if (this.path.length == 0)
			throw new IllegalStateException("root has no sibling");
		if (this.path.length != other.path.length)
			return false;
		if (this.isLeave() != other.isLeave())
			return false;
		int end = (this.isLeave()) ? this.path.length : this.path.length - 1;
		for (int i = 0; i < end; i++) {
			if (this.path[i] != other.path[i])
				return false;
		}
		return true;
	}

}
