package com.gloamframework.file.sharding.domain;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 文件块
 *
 * @author 晓龙
 */
@Data
public class Chunk implements Serializable {

    private static final long serialVersionUID = 5927187549949274445L;

    /**
     * 文件md5标识，用于标识文件
     */
    private String identificationHash;

    /**
     * 块的下标(表示第几块)
     */
    private int index;

    /**
     * 块的内容
     */
    private InputStream body;

}
