package com.neko.txbot.core.msg;

import lombok.Getter;

import java.io.File;

@Getter
public class FileImgMsg extends BaseMsg {
    private File file;

    public FileImgMsg img(File file) {
        this.file = file;
        return this;
    }

    public static FileImgMsg builder() {
        return new FileImgMsg();
    }

    @Override
    public String build() {
        return this.file.toString();
    }
}
