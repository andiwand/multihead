package at.stefl.multihead;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PciPath {

	public static final int NONE = -1;

	public static final int DOMAIN_MIN = 0x0000;
	public static final int DOMAIN_MAX = 0xffff;
	public static final int BUS_MIN = 0x00;
	public static final int BUS_MAX = 0xff;
	public static final int SLOT_MIN = 0x00;
	public static final int SLOT_MAX = 0x1f;
	public static final int FUNCTION_MIN = 0x0;
	public static final int FUNCTION_MAX = 0x7;

	public static final PciPath ROOT = new PciPath();

	private static final int LENGTH_MAX = 4;
	private static final int RADIX = 16;
	private static final int RANGE_MIN = 0;
	private static final int RANGE_MAX = 1;
	private static final int[][] RANGES = { { DOMAIN_MIN, DOMAIN_MAX }, { BUS_MIN, BUS_MAX }, { SLOT_MIN, SLOT_MAX },
			{ FUNCTION_MIN, FUNCTION_MAX } };
	private static final int INDEX_DOMAIN = 0;
	private static final int INDEX_BUS = 1;
	private static final int INDEX_SLOT = 2;
	private static final int INDEX_FUNCTION = 3;
	private static final Pattern PATTERN = Pattern
			.compile("(?:([0-9a-fA-F]{4})(?:\\:([0-9a-fA-F]{2})(?:\\:([0-9a-fA-F]{2})(?:\\.([0-9a-fA-F]))?)?)?)?");
	private static final String[] FORMAT = { "", "%04x", "%04x:%02x", "%04x:%02x:%02x", "%04x:%02x:%02x.%01x" };

	public static PciPath parse(String path) {
		Matcher matcher = PATTERN.matcher(path);
		if (!matcher.matches())
			return null;
		int[] nums = new int[LENGTH_MAX];
		int length = LENGTH_MAX;
		for (int i = 0; i < nums.length; i++) {
			String group = matcher.group(i + 1);
			if (group == null) {
				length = i;
				break;
			}
			nums[i] = Integer.parseInt(group, RADIX);
		}
		return new PciPath(nums, 0, length);
	}

	private final int[] path;

	public PciPath() {
		this(new int[] {});
	}

	public PciPath(int domain) {
		this(new int[] { domain });
	}

	public PciPath(int domain, int bus) {
		this(new int[] { domain, bus });
	}

	public PciPath(int domain, int bus, int slot) {
		this(new int[] { domain, bus, slot });
	}

	public PciPath(int domain, int bus, int slot, int function) {
		this(new int[] { domain, bus, slot, function });
	}

	public PciPath(int... path) {
		this(path, 0, path.length);
	}

	public PciPath(int[] path, int offset, int length) {
		if (offset < 0)
			throw new IllegalArgumentException("offset < 0");
		if (length < 0)
			throw new IllegalArgumentException("length < 0");
		if (offset + length > path.length)
			throw new IllegalArgumentException("array out of range");
		if (length > LENGTH_MAX)
			throw new IllegalArgumentException("path too long");
		this.path = new int[length];
		for (int i = 0; i < length; i++) {
			if ((path[offset + i] < RANGES[i][RANGE_MIN]) | (path[offset + i] > RANGES[i][RANGE_MAX]))
				throw new IllegalArgumentException("number out of definition");
			this.path[i] = path[offset + i];
		}
	}

	@Override
	public String toString() {
		Object[] args = Arrays.stream(this.path).boxed().toArray(Integer[]::new);
		return String.format(FORMAT[this.path.length], args);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.path);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PciPath other = (PciPath) obj;
		return Arrays.equals(this.path, other.path);
	}

	public int getLength() {
		return this.path.length;
	}

	public int getDomain() {
		if (this.path.length <= INDEX_DOMAIN)
			return NONE;
		return this.path[INDEX_DOMAIN];
	}

	public int getBus() {
		if (this.path.length <= INDEX_BUS)
			return NONE;
		return this.path[INDEX_BUS];
	}

	public int getSlot() {
		if (this.path.length <= INDEX_SLOT)
			return NONE;
		return this.path[INDEX_SLOT];
	}

	public int getFunction() {
		if (this.path.length <= INDEX_FUNCTION)
			return NONE;
		return this.path[INDEX_FUNCTION];
	}

	public boolean isLeave() {
		return this.path.length == LENGTH_MAX;
	}

	public PciPath parent() {
		if (this.path.length == 0)
			return null;
		return new PciPath(this.path, 0, this.path.length - 1);
	}

	public boolean parentOf(PciPath other) {
		if (this.path.length >= other.path.length)
			return false;
		for (int i = 0; i < this.path.length; i++) {
			if (this.path[i] != other.path[i])
				return false;
		}
		return true;
	}

	public boolean siblingOf(PciPath other) {
		if (this.path.length == 0)
			throw new IllegalStateException("root has no sibling");
		if (this.path.length != other.path.length)
			return false;
		for (int i = 0; i < this.path.length - 1; i++) {
			if (this.path[i] != other.path[i])
				return false;
		}
		return true;
	}

}
