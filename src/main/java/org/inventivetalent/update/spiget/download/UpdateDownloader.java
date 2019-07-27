
/*
 *  Copyright (C) 2019. MineGaming - All Rights Reserved
 *  You may use, distribute and modify this code under the
 *  terms of the LGPLv3 license, which unfortunately won't be
 *  written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package org.inventivetalent.update.spiget.download;

import org.inventivetalent.update.spiget.ResourceInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class UpdateDownloader {

    public static final String RESOURCE_DOWNLOAD = "http://api.spiget.org/v2/resources/%s/download";

    public static Runnable downloadAsync(final ResourceInfo info, final File file, final String userAgent,
                                         final DownloadCallback callback) {
        return () -> {
            try {
                download(info, file, userAgent);
                callback.finished();
            } catch (Exception e) {
                callback.error(e);
            }
        };
    }

    public static void download(ResourceInfo info, File file) {
        download(info, file);
    }

    public static void download(ResourceInfo info, File file, String userAgent) {
        if (info.external) {
            throw new IllegalArgumentException("Cannot download external resource #" + info.id);
        }
        ReadableByteChannel channel;
        try {
            // https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(RESOURCE_DOWNLOAD, info.id))
                    .openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Download returned status #" + connection.getResponseCode());
            }
            channel = Channels.newChannel(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Download failed", e);
        }
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not save file", e);
        }
    }

}
