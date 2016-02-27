package at.stefl.multihead;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceDescriptor {

	public static final int VENDOR_MIN = 0;
	public static final int VENDOR_MAX = 0xffff;
	public static final int PRODUCT_MIN = 0;
	public static final int PRODUCT_MAX = 0xffff;

	private static final int LENGTH = 2;
	private static final int RADIX = 16;
	private static final int INDEX_VENDOR = 0;
	private static final int INDEX_PRODUCT = 1;
	private static final Pattern PATTERN = Pattern.compile("([0-9a-fA-F]{4})\\:([0-9a-fA-F]{4})");
	private static final String FORMAT = "%04x:%04x";

	private final int vendor;
	private final int product;

	public static DeviceDescriptor parse(String descriptor) {
		Matcher matcher = PATTERN.matcher(descriptor);
		if (!matcher.matches())
			throw new IllegalArgumentException("not matching");
		int[] nums = new int[LENGTH];
		for (int i = 0; i < nums.length; i++) {
			String group = matcher.group(i + 1);
			nums[i] = Integer.parseInt(group, RADIX);
		}
		return new DeviceDescriptor(nums[INDEX_VENDOR], nums[INDEX_PRODUCT]);
	}

	public DeviceDescriptor(int vendor, int product) {
		if ((vendor < VENDOR_MIN) | (vendor > VENDOR_MAX))
			throw new IllegalArgumentException("vendor not in definition");
		if ((product < PRODUCT_MIN) | (product > PRODUCT_MAX))
			throw new IllegalArgumentException("product not in definition");

		this.vendor = vendor;
		this.product = product;
	}

	@Override
	public String toString() {
		return String.format(FORMAT, this.vendor, this.product);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.product;
		result = prime * result + this.vendor;
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
		DeviceDescriptor other = (DeviceDescriptor) obj;
		if (this.product != other.product)
			return false;
		if (this.vendor != other.vendor)
			return false;
		return true;
	}

	public int getVendor() {
		return vendor;
	}

	public int getProduct() {
		return product;
	}

}
