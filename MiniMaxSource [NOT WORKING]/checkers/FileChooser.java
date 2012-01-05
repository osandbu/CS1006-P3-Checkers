package checkers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * The FileChooser class lets the user open or save a file of a given file format.
 * 
 * @author 090010514
 */
public class FileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;
	private final String extension;
	private final String description;

	public FileChooser(String extension, String description) {
		super();
		this.extension = extension;
		this.description = description;
		setFileFilter(new ReplayFileFilter());
	}

	public File getSelectedFile() {
		File file = super.getSelectedFile();
		if (file != null && !file.getName().endsWith(extension))
			file = new File(file.getAbsolutePath() + extension);
		return file;
	}

	private class ReplayFileFilter extends FileFilter {
		public boolean accept(File file) {
			return file.isFile();
		}

		public String getDescription() {
			return description;
		}
	}
}
