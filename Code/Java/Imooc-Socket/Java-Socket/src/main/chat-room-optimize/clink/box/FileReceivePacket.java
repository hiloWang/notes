package clink.box;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import clink.core.ReceivePacket;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/24 11:14
 */
public class FileReceivePacket extends ReceivePacket<FileOutputStream, File> {

    private final File mFile;

    public FileReceivePacket(long len, File file) {
        super(len);
        mFile = file;
    }

    /**
     * 从流转变为对应实体时直接返回创建时传入的File文件
     *
     * @param stream 文件传输流
     * @return File
     */
    @Override
    protected File buildEntity(FileOutputStream stream) {
        return mFile;
    }

    @Override
    public byte getType() {
        return TYPE_STREAM_FILE;
    }

    @Override
    protected FileOutputStream createStream() {
        try {
            return new FileOutputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
