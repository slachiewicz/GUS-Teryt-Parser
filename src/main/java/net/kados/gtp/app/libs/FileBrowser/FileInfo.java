package net.kados.gtp.app.libs.FileBrowser;

import java.io.File;

public class FileInfo
{
    private final File file;
    private final int lines;

    public FileInfo(File currentFile, int filelineNumber) 
    {
        this.file   = currentFile;
        this.lines  = filelineNumber;
    }

    /**
     * @return the file
     */
    public File getFile() 
    {
        return file;
    }

    /**
     * @return the lines
     */
    public int getLines() 
    {
        return lines;
    }
}