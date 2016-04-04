package at.stefl.multihead;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

// TODO: reduce redundancy with PciNameService 
// TODO: support device class (bottom of the hwdata file)
public class UsbNameService {

	// TODO: support gz version "/usr/share/hwdata/usb.ids.gz"
	private static final Path HWDATA_PATH = Paths.get("/usr/share/hwdata/usb.ids");
	private static final String COMMENT_PREFIX = "#";
	private static final String[] INTENTATION = { "", "\t", "\t\t" };
	private static final int ID_RADIX = 16;
	private static final int ID_LENGTH = 4;
	private static final String NAME_SEPARATOR = "  ";

	private static BufferedReader stream() throws IOException {
		return Files.newBufferedReader(HWDATA_PATH);
	}

	private static int getIntentation(String line) {
		for (int i = INTENTATION.length - 1; i >= 0; i--) {
			if (line.startsWith(INTENTATION[i]))
				return i;
		}
		return -1;
	}

	private static String find(BufferedReader in, int intentation, int id) throws IOException {
		while (true) {
			String line = in.readLine();
			if (line == null)
				return null;
			if (line.startsWith(COMMENT_PREFIX))
				continue;
			if (line.trim().isEmpty())
				continue;

			int currentIntentation = getIntentation(line);
			if (currentIntentation < 0)
				continue;
			if (currentIntentation < intentation)
				return null;
			if (currentIntentation > intentation)
				continue;

			int offset = INTENTATION[currentIntentation].length();
			int currentId = Integer.parseInt(line.substring(offset, offset + ID_LENGTH), ID_RADIX);
			if (currentId < id)
				continue;
			if (currentId > id)
				return null;

			return line;
		}
	}

	private static String extract(String line, int intentation) {
		int offset = INTENTATION[intentation].length() + ID_LENGTH + NAME_SEPARATOR.length();
		return line.substring(offset);
	}

	public static List<String> find(int... id) {
		if (id.length <= 0)
			throw new IllegalArgumentException("descriptor.length <= 0");
		if (id.length == 3)
			throw new IllegalArgumentException("descriptor.length == 3");
		if (id.length > 4)
			throw new IllegalArgumentException("descriptor.length > 4");

		List<String> result = new LinkedList<>();

		try {
			BufferedReader in = stream();

			for (int i = 0; i < id.length; i++) {
				String line = find(in, i, id[i]);
				if (line == null)
					break;
				result.add(extract(line, i));
			}
		} catch (IOException e) {
			throw new IllegalStateException("io exception", e);
		}

		return result;
	}

	private UsbNameService() {
	}

}
