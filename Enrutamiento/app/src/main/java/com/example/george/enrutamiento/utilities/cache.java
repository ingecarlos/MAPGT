package com.example.george.enrutamiento.utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.File;
import java.util.Date;

public class cache {

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    public static void trimCache(Context context) throws PackageManager.NameNotFoundException {


        File cachDir = context.getCacheDir();
        Log.v("Trim", "dir " + cachDir.getPath());

        if (cachDir!= null && cachDir.isDirectory()) {

            Log.v("Trim", "can read " + cachDir.canRead());
            String[] fileNames = cachDir.list();
            //Iterate for the fileName and delete
            for (String fileName:fileNames) {
                clearCacheFolder(new File(fileName),0);
            }
        }
    }

    static int clearCacheFolder(final File dir, final int numDays) {
        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
            try {
                for (File child:dir.listFiles()) {

                    //first delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }

                    //then delete the files and subdirectories in this dir
                    //only empty directories can be deleted, so subdirs have been done first
                    if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch(Exception e) {
                Log.e("ATTENTION!", String.format("Failed to clean the cache, error %s", e.getMessage()));
            }
        }
        return deletedFiles;
    }

    public static void clearCache(final Context context, final int numDays) {
        Log.i("ADVL", String.format("Starting cache prune, deleting files older than %d days", numDays));
        int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
        Log.i("ADVL", String.format("Cache pruning completed, %d files deleted", numDeletedFiles));
    }
}
