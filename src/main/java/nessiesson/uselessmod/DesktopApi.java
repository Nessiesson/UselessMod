package nessiesson.uselessmod;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// Stolen from mightypork from Stackoverflow.
// https://stackoverflow.com/questions/18004150/desktop-api-is-not-supported-on-the-current-platform/18004334#18004334

public class DesktopApi {

	public static void browse(URI uri) {
		openSystemSpecific(uri.toString());
	}

	private static void openSystemSpecific(String what) {
		EnumOS os = getOs();
		if (os.isLinux()) {
			if (!runCommand("kde-open", "%s", what) && !runCommand("gnome-open", "%s", what)) {
				runCommand("xdg-open", "%s", what);
			}
			return;
		}

		if (os.isMac()) {
			runCommand("open", "%s", what);
			return;
		}

		if (os.isWindows()) {
			runCommand("explorer", "%s", what);
		}

	}

	private static boolean runCommand(String command, String args, String file) {
		String[] parts = prepareCommand(command, args, file);

		try {
			Process p = Runtime.getRuntime().exec(parts);
			if (p == null) return false;

			try {
				int retval = p.exitValue();
				if (retval == 0) {
					return false;
				} else {
					return false;
				}
			} catch (IllegalThreadStateException itse) {
				return true;
			}
		} catch (IOException e) {
			return false;
		}
	}


	private static String[] prepareCommand(String command, String args, String file) {
		List<String> parts = new ArrayList<>();
		parts.add(command);

		if (args != null) {
			for (String s : args.split(" ")) {
				s = String.format(s, file); // put in the filename thing
				parts.add(s.trim());
			}
		}

		return parts.toArray(new String[parts.size()]);
	}

	private static EnumOS getOs() {
		String s = System.getProperty("os.name").toLowerCase();
		if (s.contains("win")) {
			return EnumOS.windows;
		}

		if (s.contains("mac")) {
			return EnumOS.macos;
		}

		if (s.contains("solaris")) {
			return EnumOS.solaris;
		}

		if (s.contains("sunos")) {
			return EnumOS.solaris;
		}

		if (s.contains("linux")) {
			return EnumOS.linux;
		}

		if (s.contains("unix")) {
			return EnumOS.linux;
		} else {
			return EnumOS.unknown;
		}
	}


	public static enum EnumOS {
		linux, macos, solaris, unknown, windows;

		public boolean isLinux() {
			return this == linux || this == solaris;
		}


		public boolean isMac() {
			return this == macos;
		}


		public boolean isWindows() {
			return this == windows;
		}
	}
}